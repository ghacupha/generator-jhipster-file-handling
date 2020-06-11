/**
 * Copyright 2013-2017 the Edwin Njeru, Pascal Grimaud and the respective JHipster contributors.
 *
 * This file is part of the JHipster project, see http://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const chalk = require('chalk');
const semver = require('semver');
const BaseGenerator = require('generator-jhipster/generators/generator-base');
const jhipsterConstants = require('generator-jhipster/generators/generator-constants');
const packagejs = require('../../package.json');
const UtilJdl = require('./utilJdl.js');
const utilProps = require('./utilProperties.js');
const genUtils = require('./genUtils.js');

// jdl scripts
const FILE_UPLOADS_JDL = 'fileUploads';
const GENERAL_FILE_UPLOADS_JDL = 'fileUploads-general';
const MICROSERVICE_FILE_UPLOADS_JDL = 'fileUploads-microservice';

// other constants
const GENERAL_CLIENT_ROOT_FOLDER = 'fileUploads';
const EXAMPLE_FILE_MODEL_TYPES = 'SERVICE_OUTLETS,CURRENCY_LIST,FX_RATES,SCHEME_LIST,SECTORS,LEDGERS';
const RABBITMQ = 'RabbitMQ';
const KAFKA = 'Kafka';
const DEFAULT_BROKER_TYPE = RABBITMQ;
const DEFAULT_RABBITMQ_MESSAGE_NAME = 'message';

// MQ dependencies
const STREAM_RABBIT_VERSION = '1.3.0.RELEASE';
// const STREAM_KAFKA_VERSION = '1.3.0.RELEASE';
const STREAM_CLOUD_DEPENDENCY_VERSION = 'Chelsea.SR2';
const STREAM_CLOUD_STREAM_VERSION = '1.3.0.RELEASE';

module.exports = class extends BaseGenerator {
    get initializing() {
        return {
            init(args) {
                if (args === 'default') {
                    // do something when argument is 'default'
                    this.message = 'default message';
                    this.gatewayMicroserviceName = 'Main';
                }
            },
            readConfig() {
                this.jhipsterAppConfig = this.getAllJhipsterConfig();
                if (!this.jhipsterAppConfig) {
                    this.error('Cannot read .yo-rc.json');
                }
            },
            displayLogo() {
                // it's here to show that you can use functions from generator-jhipster
                // this function is in: generator-jhipster/generators/generator-base.js
                // this.printJHipsterLogo();

                // Have Yeoman greet the user.
                this.log(
                    `\nWelcome to the ${chalk.bold.yellow('JHipster file-handling')} generator! ${chalk.yellow(`v${packagejs.version}\n`)}`
                );
            },
            checkJhipster() {
                const currentJhipsterVersion = this.jhipsterAppConfig.jhipsterVersion;
                const minimumJhipsterVersion = packagejs.dependencies['generator-jhipster'];
                if (!semver.satisfies(currentJhipsterVersion, minimumJhipsterVersion)) {
                    this.warning(
                        `\nYour generated project used an old JHipster version (${currentJhipsterVersion})... you need at least (${minimumJhipsterVersion})\n`
                    );
                }
            },
            checkServerFramework() {
                if (this.jhipsterAppConfig.skipServer) {
                    this.env.error(`${chalk.red.bold('ERROR!')} This module works only for server...`);
                }
            },
            checkGit() {
                this.log('Experience has shown that this kind of thing really needs a working knowledge of git. So beware');
            }
        };
    }

    prompting() {
        const prompts = [
            {
                when: () =>
                    (typeof this.gatewayMicroserviceName === 'undefined' && this.jhipsterAppConfig.applicationType === 'microservice') ||
                    this.jhipsterAppConfig.applicationType === 'gateway',
                type: 'input',
                name: 'gatewayMicroserviceName',
                message: 'What is the microservice name for the file handling workflow?',
                default: `${this.jhipsterAppConfig.baseName}Main`
            },
            {
                when: () =>
                    (typeof this.addFieldAndClassPrefix === 'undefined' && this.jhipsterAppConfig.applicationType === 'microservice') ||
                    this.jhipsterAppConfig.applicationType === 'gateway',
                type: 'input',
                name: 'addFieldAndClassPrefix',
                message: 'Do you want to prefix fields and classes for the microservice file handling workflows? (true/false)',
                default: false
            },
            {
                when: () =>
                    typeof this.generalClientRootFolder === 'undefined' && this.jhipsterAppConfig.applicationType !== 'microservice',
                type: 'input',
                name: 'generalClientRootFolder',
                // eslint-disable-next-line quotes
                message: "What is the general client folder's name for the file handling workflow?",
                default: GENERAL_CLIENT_ROOT_FOLDER
            },
            {
                when: () => typeof this.fileModelTypes === 'undefined',
                type: 'input',
                name: 'fileModelTypes',
                message: 'What file model types would you like to represent?',
                default: EXAMPLE_FILE_MODEL_TYPES
            },
            {
                type: 'list',
                name: 'messageBrokerType',
                message: `Which ${chalk.yellow('*type*')} of message broker would you like to add?`,
                choices: [
                    {
                        value: RABBITMQ,
                        name: 'RabbitMQ message broker (recommended for simple projects)'
                    },
                    {
                        value: KAFKA,
                        name: 'Kafka message broker (recommended for advanced projects) not implemented yet'
                    }
                ],
                store: true,
                default: DEFAULT_BROKER_TYPE
            },
            {
                when: response => response.messageBrokerType === RABBITMQ,
                type: 'input',
                name: 'rabbitMqNameOfMessage',
                message: 'Please choose the name of the message class use by rabbit',
                default: DEFAULT_RABBITMQ_MESSAGE_NAME,
                store: true
            }
        ];

        const done = this.async();
        if (this.defaultOptions) {
            this.messageBrokerType = DEFAULT_BROKER_TYPE;
            this.rabbitMqNameOfMessage = DEFAULT_RABBITMQ_MESSAGE_NAME;
            done();
        } else {
            this.prompt(prompts).then(answers => {
                this.promptAnswers = answers;
                // To access props answers use this.promptAnswers.someOption;
                done();
            });
        }
    }

    writing() {
        // read config from .yo-rc.json
        this.baseName = this.jhipsterAppConfig.baseName;
        this.packageName = this.jhipsterAppConfig.packageName;
        this.packageFolder = this.jhipsterAppConfig.packageFolder;
        this.clientFramework = this.jhipsterAppConfig.clientFramework;
        this.clientPackageManager = this.jhipsterAppConfig.clientPackageManager;
        this.buildTool = this.jhipsterAppConfig.buildTool;

        // use function in generator-base.js from generator-jhipster
        this.angularAppName = this.getAngularAppName();

        // Get the application name for use with main class
        this.appName = genUtils.capitalizeFLetter(this.baseName);

        // use constants from generator-constants.js
        this.javaDir = `${jhipsterConstants.SERVER_MAIN_SRC_DIR + this.packageFolder}/`;
        this.javaTestDir = `${jhipsterConstants.SERVER_TEST_SRC_DIR + this.packageFolder}/`;
        this.resourceDir = jhipsterConstants.SERVER_MAIN_RES_DIR;
        this.webappDir = jhipsterConstants.CLIENT_MAIN_SRC_DIR;
        this.javaTemplateDir = 'src/main/java/package';
        this.javaTemplateTestDir = 'src/test/java/package';

        // variable from questions
        if (typeof this.gatewayMicroserviceName === 'undefined') {
            this.gatewayMicroserviceName = this.promptAnswers.gatewayMicroserviceName;
        }

        if (typeof this.generalClientRootFolder === 'undefined') {
            this.generalClientRootFolder = this.promptAnswers.generalClientRootFolder;
        }

        if (typeof this.fileModelTypes === 'undefined') {
            this.fileModelTypes = this.promptAnswers.fileModelTypes;
        }

        if (typeof this.addFieldAndClassPrefix === 'undefined') {
            this.addFieldAndClassPrefix = this.promptAnswers.addFieldAndClassPrefix;
        }

        if (typeof this.messageBrokerType === 'undefined') {
            this.messageBrokerType = this.promptAnswers.messageBrokerType;
        }

        // show all variables
        this.log('\n--- some config read from config ---');
        this.log(`baseName=${this.baseName}`);
        this.log(`packageName=${this.packageName}`);
        this.log(`clientFramework=${this.clientFramework}`);
        this.log(`clientPackageManager=${this.clientPackageManager}`);
        this.log(`buildTool=${this.buildTool}`);

        this.log('\n--- some function ---');
        this.log(`angularAppName=${this.angularAppName}`);

        this.log('\n--- some const ---');
        this.log(`javaDir=${this.javaDir}`);
        this.log(`resourceDir=${this.resourceDir}`);
        this.log(`webappDir=${this.webappDir}`);

        this.log('\n--- variables from questions ---');
        this.log(`create client code?=${this.message}`);
        this.log(`Gateway microservice name?=${this.gatewayMicroserviceName}`);
        this.log('------\n');

        this.template = function(source, destination) {
            this.fs.copyTpl(this.templatePath(source), this.destinationPath(destination), this);
        };

        // setup field and class names
        if (this.jhipsterAppConfig.applicationType === 'microservice') {
            this.fieldNamesPrefix = this.gatewayMicroserviceName;
            this.classNamesPrefix = genUtils.capitalizeFLetter(this.gatewayMicroserviceName);
        }

        const wroteFiles = this.async();
        if (this.addFieldAndClassPrefix && this.jhipsterAppConfig.applicationType === 'microservice') {
            this.template(`${MICROSERVICE_FILE_UPLOADS_JDL}.jdl.ejs`, `.jhipster/${MICROSERVICE_FILE_UPLOADS_JDL}.jdl`);
        }

        if (!this.addFieldAndClassPrefix && this.jhipsterAppConfig.applicationType === 'microservice') {
            this.template(`${FILE_UPLOADS_JDL}.jdl.ejs`, `.jhipster/${FILE_UPLOADS_JDL}.jdl`);
        }

        if (this.jhipsterAppConfig.applicationType !== 'microservice') {
            this.template(`${GENERAL_FILE_UPLOADS_JDL}.jdl.ejs`, `.jhipster/${GENERAL_FILE_UPLOADS_JDL}.jdl`);
        }
        wroteFiles();

        // Write the other files
        this._installServerCode(this, this.javaTemplateDir, this.javaTemplateTestDir, this.javaDir, this.javaTestDir, this.resourceDir);

        // optional installs for either kafka or rabbitMQ
        if (this.messageBrokerType === RABBITMQ) {
            // todo update the arguments
            this._installRabbitMq(this.resourceDir, this.javaDir, this, this.buildTool, this.messageBrokerType, this.rabbitMqNameOfMessage);
        } else if (this.messageBrokerType === KAFKA) {
            this._installKafka(this.resourceDir, this.javaDir, this);
        }

        // install jdl entities
        this._useJdlExecution();
    }

    /**
     * Install rabbitMq-specific dependencies and templates
     *
     * @param {String} resourceDir
     * @param {String} javaDir
     * @param {object} generator
     * @param {String} buildTool
     * @param {String} messageBrokerType
     * @param {String} rabbitMqNameOfMessage
     * @private
     */
    _installRabbitMq(resourceDir, javaDir, generator, buildTool, messageBrokerType, rabbitMqNameOfMessage) {
        this.log(`\n message broker type = ${messageBrokerType}`);
        // add dependencies
        if (buildTool === 'maven') {
            if (typeof this.addMavenDependencyManagement === 'function') {
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-starter-stream-rabbit');
            } else {
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-starter-stream-rabbit', STREAM_RABBIT_VERSION);
            }
        } else if (buildTool === 'gradle') {
            if (typeof this.addGradleDependencyManagement === 'function') {
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-starter-stream-rabbit');
            } else {
                this.addGradleDependency(
                    'compile',
                    'org.springframework.cloud',
                    'spring-cloud-starter-stream-rabbit',
                    STREAM_RABBIT_VERSION
                );
            }
        }

        // add docker-compose file
        this.template('src/main/docker/_rabbitmq.yml', 'src/main/docker/rabbitmq.yml');
        const messageName = genUtils.capitalizeFLetter(rabbitMqNameOfMessage);
        this.rabbitMessageName = messageName;
        this.rabbitMessageNameNonUcFirst = genUtils.unCapitalizeFLetter(messageName);

        utilProps.updateAppProperties(generator, resourceDir);
    }

    /**
     * Install kafka-specific dependencies and templates
     *
     * @param {String} resourceDir
     * @param {String} javaDir
     * @param {object} generator
     * @private
     */
    _installKafka(resourceDir, javaDir, generator) {
        this.log('Work in progress');
    }

    _useJdlExecution(_callback) {
        // run jdl script
        const jdlProcessor = new UtilJdl(this.jhipsterAppConfig.applicationType, this.skipClient, this.addFieldAndClassPrefix);
        jdlProcessor.executeJdlScript();

        _callback();
    }

    /**
     * This install back-end java code and liquibase migration configuration
     *
     * @param {String} javaTemplateDir path of the template in this module
     * @param {String} javaTemplateTestDir
     * @param {String} javaDir path of the project's java directory
     * @param {String} javaTestDir path of the project's java test directory
     * @param {String} resourceDir  path of the main resource directory
     * @private
     */
    _installServerCode(gen, javaTemplateDir, javaTemplateTestDir, javaDir, javaTestDir, resourceDir) {
        // todo copy config file
        // eslint-disable-next-line no-unused-vars
        const packageName = gen.packageName;
        // eslint-disable-next-line no-unused-vars
        const classNamesPrefix = gen.classNamesPrefix;
        // eslint-disable-next-line no-unused-vars
        const fieldNamesPrefix = gen.fieldNamesPrefix;
        // collect files to copy
        const files = [
            {
                from: `${javaTemplateDir}/config/_CloudMessagingConfiguration.java`,
                to: `${javaDir}/config/CloudMessagingConfiguration.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/currencyTable/CurrencyTableBatchConfig.java`,
                to: `${javaDir}/internal/batch/currencyTable/CurrencyTableBatchConfig.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/currencyTable/CurrencyTableListItemReader.java`,
                to: `${javaDir}/internal/batch/currencyTable/CurrencyTableListItemReader.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/currencyTable/package-info.java`,
                to: `${javaDir}/internal/batch/currencyTable/package-info.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/BatchConfigs.java`,
                to: `${javaDir}/internal/batch/BatchConfigs.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/ListPartition.java`,
                to: `${javaDir}/internal/batch/ListPartition.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/package-info.java`,
                to: `${javaDir}/internal/batch/package-info.java`
            },
            {
                from: `${javaTemplateDir}/internal/batch/PersistenceJobListener.java`,
                to: `${javaDir}/internal/batch/PersistenceJobListener.java`
            },
            {
                from: `${javaTemplateDir}/internal/excel/deserializer/DefaultExcelFileDeserializer.java`,
                to: `${javaDir}/internal/excel/deserializer/DefaultExcelFileDeserializer.java`
            },
            {
                from: `${javaTemplateDir}/internal/excel/deserializer/DeserializationUtils.java`,
                to: `${javaDir}/internal/excel/deserializer/DeserializationUtils.java`
            },
            {
                from: `${javaTemplateDir}/internal/excel/ExcelDeserializerContainer.java`,
                to: `${javaDir}/internal/excel/ExcelDeserializerContainer.java`
            },
            {
                from: `${javaTemplateDir}/internal/excel/ExcelFileDeserializer.java`,
                to: `${javaDir}/internal/excel/ExcelFileDeserializer.java`
            },
            {
                from: `${javaTemplateDir}/internal/excel/PoijiOptionsConfig.java`,
                to: `${javaDir}/internal/excel/PoijiOptionsConfig.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/DeleteMessageDTO.java`,
                to: `${javaDir}/internal/messaging/DeleteMessageDTO.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/MessageServiceContainer.java`,
                to: `${javaDir}/internal/messaging/MessageServiceContainer.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/StringTokenValueSearch.java`,
                to: `${javaDir}/internal/messaging/StringTokenValueSearch.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/TokenValueSearch.java`,
                to: `${javaDir}/internal/messaging/TokenValueSearch.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotification.java`,
                to: `${javaDir}/internal/messaging/fileNotification/FileNotification.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotificationListener.java`,
                to: `${javaDir}/internal/messaging/fileNotification/FileNotificationListener.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotificationStreams.java`,
                to: `${javaDir}/internal/messaging/fileNotification/FileNotificationStreams.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotificationStreamsConfig.java`,
                to: `${javaDir}/internal/messaging/fileNotification/FileNotificationStreamsConfig.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/BatchSupportedFileUploadProcessor.java`,
                to: `${javaDir}/internal/messaging/fileNotification/processors/BatchSupportedFileUploadProcessor.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/FileUploadProcessor.java`,
                to: `${javaDir}/internal/messaging/fileNotification/processors/FileUploadProcessor.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/FileUploadProcessorChain.java`,
                to: `${javaDir}/internal/messaging/fileNotification/processors/FileUploadProcessorChain.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/FileUploadProcessorContainer.java`,
                to: `${javaDir}/internal/messaging/fileNotification/processors/FileUploadProcessorContainer.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/StreamSupportedFileUploadProcessor.java`,
                to: `${javaDir}/internal/messaging/fileNotification/processors/StreamSupportedFileUploadProcessor.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/package-info.java`,
                to: `${javaDir}/internal/messaging/fileNotification/processors/package-info.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/jsonStrings/GsonUtils.java`,
                to: `${javaDir}/internal/messaging/jsonStrings/GsonUtils.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/jsonStrings/JsonStringStreams.java`,
                to: `${javaDir}/internal/messaging/jsonStrings/JsonStringStreams.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/jsonStrings/JsonStringStreamsConfig.java`,
                to: `${javaDir}/internal/messaging/jsonStrings/JsonStringStreamsConfig.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/jsonStrings/JsonStringStreamsListener.java`,
                to: `${javaDir}/internal/messaging/jsonStrings/JsonStringStreamsListener.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/jsonStrings/StringMessageDTO.java`,
                to: `${javaDir}/internal/messaging/jsonStrings/StringMessageDTO.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/MessageService.java`,
                to: `${javaDir}/internal/messaging/platform/MessageService.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/MessageStreams.java`,
                to: `${javaDir}/internal/messaging/platform/MessageStreams.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/MuteListener.java`,
                to: `${javaDir}/internal/messaging/platform/MuteListener.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/ResponsiveListener.java`,
                to: `${javaDir}/internal/messaging/platform/ResponsiveListener.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/StringTokenMessageService.java`,
                to: `${javaDir}/internal/messaging/platform/StringTokenMessageService.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/Tokenizable.java`,
                to: `${javaDir}/internal/messaging/platform/Tokenizable.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/platform/TokenizableMessage.java`,
                to: `${javaDir}/internal/messaging/platform/TokenizableMessage.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/sample/Greetings.java`,
                to: `${javaDir}/internal/messaging/sample/Greetings.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/sample/GreetingsContainer.java`,
                to: `${javaDir}/internal/messaging/sample/GreetingsContainer.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/sample/GreetingsListener.java`,
                to: `${javaDir}/internal/messaging/sample/GreetingsListener.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/sample/GreetingsStreams.java`,
                to: `${javaDir}/internal/messaging/sample/GreetingsStreams.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/sample/GreetingsStreamsConfig.java`,
                to: `${javaDir}/internal/messaging/sample/GreetingsStreamsConfig.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/DeleteMessageDTO.java`,
                to: `${javaDir}/internal/messaging/DeleteMessageDTO.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/MessageServiceContainer.java`,
                to: `${javaDir}/internal/messaging/MessageServiceContainer.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/StringTokenValueSearch.java`,
                to: `${javaDir}/internal/messaging/StringTokenValueSearch.java`
            },
            {
                from: `${javaTemplateDir}/internal/messaging/TokenValueSearch.java`,
                to: `${javaDir}/internal/messaging/TokenValueSearch.java`
            },
            {
                from: `${javaTemplateDir}/internal/model/package-info.java`,
                to: `${javaDir}/internal/model/package-info.java`
            },
            {
                from: `${javaTemplateDir}/internal/report/QueryTools.java`,
                to: `${javaDir}/internal/report/QueryTools.java`
            },
            {
                from: `${javaTemplateDir}/internal/report/Report.java`,
                to: `${javaDir}/internal/report/Report.java`
            },
            {
                from: `${javaTemplateDir}/internal/resource/decorator/FileUploadResourceDecorator.java`,
                to: `${javaDir}/internal/resource/decorator/FileUploadResourceDecorator.java`
            },
            {
                from: `${javaTemplateDir}/internal/resource/decorator/IFileUploadResource.java`,
                to: `${javaDir}/internal/resource/decorator/IFileUploadResource.java`
            },
            {
                from: `${javaTemplateDir}/internal/resource/AppFileUploadResource.java`,
                to: `${javaDir}/internal/resource/AppFileUploadResource.java`
            },
            {
                from: `${javaTemplateDir}/internal/resource/ReportList.java`,
                to: `${javaDir}/internal/resource/ReportList.java`
            },
            {
                from: `${javaTemplateDir}/internal/service/BatchService.java`,
                to: `${javaDir}/internal/service/package-info.java`
            },
            {
                from: `${javaTemplateDir}/internal/util/TokenGenerator.java`,
                to: `${javaDir}/internal/util/TokenGenerator.java`
            },
            {
                from: `${javaTemplateDir}/internal/AppConstants.java`,
                to: `${javaDir}/internal/AppConstants.java`
            },
            {
                from: `${javaTemplateDir}/internal/Mapping.java`,
                to: `${javaDir}/internal/Mapping.java`
            },
            {
                from: `${javaTemplateDir}/internal/package-info.java`,
                to: `${javaDir}/internal/package-info.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/excel/ExcelFileUtilsIT.java`,
                to: `${javaTestDir}/internal/excel/ExcelFileUtilsIT.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/excel/ExcelFileUtilsTest.java`,
                to: `${javaTestDir}/internal/excel/ExcelFileUtilsTest.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/excel/ExcelTestUtil.java`,
                to: `${javaTestDir}/internal/excel/ExcelTestUtil.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/messaging/FileNotificationControllerIT.java`,
                to: `${javaTestDir}/internal/messaging/FileNotificationControllerIT.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/messaging/GreetingsControllerIT.java`,
                to: `${javaTestDir}/internal/messaging/GreetingsControllerIT.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/messaging/GsonUtilsTest.java`,
                to: `${javaTestDir}/internal/messaging/GsonUtilsTest.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/messaging/JsonStringsControllerIT.java`,
                to: `${javaTestDir}/internal/messaging/JsonStringsControllerIT.java`
            },
            {
                from: `${javaTemplateTestDir}/internal/messaging/TokenGeneratorTest.java`,
                to: `${javaTestDir}/internal/messaging/TokenGeneratorTest.java`
            }
        ];

        genUtils.copyFiles(this, files);

        // TODO Add liquibase config for spring batch
        // Add liquibase resources
        this.changelogDate = this.dateFormatForLiquibase();
        // eslint-disable-next-line prettier/prettier
        this.template(
            'src/main/resources/config/liquibase/changelog/_added_springbatch_schema.xml',
            `${resourceDir}config/liquibase/changelog/${this.changelogDate}_added_springbatch_schema.xml`
        );
        this.addChangelogToLiquibase(`${this.changelogDate}_added_springbatch_schema.xml`);

        // update maven dependencies
        this._installServerDependencies();
    }

    /**
     * Install server libraries for handling excel and their DTOs
     */
    _installServerDependencies() {
        const OZLERHAKAN_POIJI_VERSION = '1.20.0';
        const LOMBOK_VERSION = '1.18.6';

        // const STREAM_CLOUD_DEPENDENCY_VERSION

        if (this.buildTool === 'maven') {
            if (typeof this.addMavenDependencyManagement === 'function') {
                this.addMavenDependencyManagement(
                    'org.springframework.cloud',
                    'spring-cloud-stream-dependencies',
                    STREAM_CLOUD_DEPENDENCY_VERSION,
                    'pom',
                    'import'
                );
                this.addMavenDependency('com.github.ozlerhakan', 'poiji', OZLERHAKAN_POIJI_VERSION);
                this.addMavenDependency('org.projectlombok', 'lombok', LOMBOK_VERSION);
                this.addMavenDependency('org.springframework.boot', 'spring-boot-starter-batch');
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream');
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream-binder-test');
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream-test-support');
                this.addMavenDependency(
                    'org.springframework.cloud',
                    'spring-cloud-stream-test-support-internal',
                    STREAM_CLOUD_STREAM_VERSION
                );
            } else {
                this.addMavenDependency('com.github.ozlerhakan', 'poiji', OZLERHAKAN_POIJI_VERSION);
                this.addMavenDependency('org.projectlombok', 'lombok', LOMBOK_VERSION);
                this.addMavenDependency('org.springframework.boot', 'spring-boot-starter-batch');
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream-binder-test', STREAM_CLOUD_STREAM_VERSION);
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream-test-support', STREAM_CLOUD_STREAM_VERSION);
                this.addMavenDependency(
                    'org.springframework.cloud',
                    'spring-cloud-stream-test-support-internal',
                    STREAM_CLOUD_STREAM_VERSION
                );
            }
            this.addMavenAnnotationProcessor('org.projectlombok', 'lombok', LOMBOK_VERSION);
            this.addMavenProperty('lombok.version', LOMBOK_VERSION);
            this.addMavenProperty('poiji.version', OZLERHAKAN_POIJI_VERSION);
        } else if (this.buildTool === 'gradle') {
            if (typeof this.addGradleDependencyManagement === 'function') {
                this.addGradleDependencyManagement(
                    'mavenBom',
                    'org.springframework.cloud',
                    'spring-cloud-stream-dependencies',
                    STREAM_CLOUD_DEPENDENCY_VERSION
                );
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream');
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream-binder-test');
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream-test-support');
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream-test-support-internal');
                this.addGradleDependency('compile', 'com.github.ozlerhakan', 'poiji');
                this.addGradleDependency('compile', 'org.projectlombok', 'lombok');
                this.addGradleDependency('compile', 'org.springframework.boot', 'spring-boot-starter-batch');
            } else {
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
                this.addGradleDependency(
                    'compile',
                    'org.springframework.cloud',
                    'spring-cloud-stream-binder-test',
                    STREAM_CLOUD_STREAM_VERSION
                );
                this.addGradleDependency(
                    'compile',
                    'org.springframework.cloud',
                    'spring-cloud-stream-test-support',
                    STREAM_CLOUD_STREAM_VERSION
                );
                this.addGradleDependency(
                    'compile',
                    'org.springframework.cloud',
                    'spring-cloud-stream-test-support-internal',
                    STREAM_CLOUD_STREAM_VERSION
                );
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
                this.addGradleDependency('compile', 'com.github.ozlerhakan', 'poiji', OZLERHAKAN_POIJI_VERSION);
                this.addGradleDependency('compile', 'org.springframework.boot', 'spring-boot-starter-batch', LOMBOK_VERSION);
            }
            this.addAnnotationProcessor('org.projectlombok', 'lombok', LOMBOK_VERSION);
            this.addGradleProperty('poiji.version', OZLERHAKAN_POIJI_VERSION);
        }
    }

    install() {
        // todo make wait for the jdlExecution
        this._useJdlExecution(function() {
            // So am new to js... Call backs just don't make sense
        });

        const logMsg = `To install your dependencies manually, run: ${chalk.yellow.bold(`${this.clientPackageManager} install`)}`;

        const injectDependenciesAndConstants = err => {
            if (err) {
                this.warning('Install of dependencies failed!');
                this.log(logMsg);
            }
        };
        const installConfig = {
            bower: false,
            npm: this.clientPackageManager !== 'yarn',
            yarn: this.clientPackageManager === 'yarn',
            callback: injectDependenciesAndConstants
        };
        if (this.options['skip-install']) {
            this.log(logMsg);
        } else {
            this.installDependencies(installConfig);
        }
    }

    end() {
        this.log('End of file-handling generator');
    }
};
