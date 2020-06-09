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
const { spawn } = require('child_process');
const packagejs = require('../../package.json');

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
                this.printJHipsterLogo();

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
            checkGateway() {
                this.log(
                    'Gateways are not supported, its instead assumed that the gateway is formed once the microservice is complete using the entity files in the .jhipster folder'
                );
                if (this.jhipsterAppConfig.applicationType === 'gateway') {
                    throw new Error('This module is not for gateways');
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
            }
        ];

        const done = this.async();
        this.prompt(prompts).then(answers => {
            this.promptAnswers = answers;
            // To access props answers use this.promptAnswers.someOption;
            done();
        });
    }

    /**
     *
     * @param {String} string the string to capitalze
     * @return {String} string with camel-case
     */
    _capitalizeFLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
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
        this.appName = this.baseName.charAt(0).toUpperCase() + this.baseName.slice(1);

        // use constants from generator-constants.js
        const javaDir = `${jhipsterConstants.SERVER_MAIN_SRC_DIR + this.packageFolder}/`;
        const javaTestDir = `${jhipsterConstants.SERVER_TEST_SRC_DIR + this.packageFolder}/`;
        const resourceDir = jhipsterConstants.SERVER_MAIN_RES_DIR;
        const webappDir = jhipsterConstants.CLIENT_MAIN_SRC_DIR;

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
        this.log(`javaDir=${javaDir}`);
        this.log(`resourceDir=${resourceDir}`);
        this.log(`webappDir=${webappDir}`);

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
            this.classNamesPrefix = this._capitalizeFLetter(this.gatewayMicroserviceName);
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
        this._installServerCode(javaDir, javaTestDir, resourceDir);

        switch (this.messageBrokerType) {
            case RABBITMQ:
                this._installRabbitMq();
                break;
            case KAFKA:
                this._installKafka();
                break;
            default:
                break;
        }

        this._useJdlExecution();
    }

    _installRabbitMq() {
        const STREAM_RABBIT_VERSION = '1.3.0.RELEASE';
        const STREAM_CLOUD_DEPENDENCY_VERSION = 'Chelsea.SR2';
        const STREAM_CLOUD_STREAM_VERSION = '1.3.0.RELEASE';
        // read config from .yo-rc.json
        this.baseName = this.jhipsterAppConfig.baseName;
        this.packageName = this.jhipsterAppConfig.packageName;
        this.packageFolder = this.jhipsterAppConfig.packageFolder;
        this.clientFramework = this.jhipsterAppConfig.clientFramework;
        this.clientPackageManager = this.jhipsterAppConfig.clientPackageManager;
        this.buildTool = this.jhipsterAppConfig.buildTool;

        // use function in generator-base.js from generator-jhipster
        this.angularAppName = this.getAngularAppName();

        // const webappDir = jhipsterConstants.CLIENT_MAIN_SRC_DIR;
        this.log(`\nmessage broker type = ${this.messageBrokerType}`);
        this.log(`\nmessage broker type = ${this.rabbitMqNameOfMessage}`);
        this.log('------\n');

        // use constants from generator-constants.js
        const javaDir = `${jhipsterConstants.SERVER_MAIN_SRC_DIR + this.packageFolder}/`;
        const resourceDir = jhipsterConstants.SERVER_MAIN_RES_DIR;
        // add dependencies
        if (this.buildTool === 'maven') {
            if (typeof this.addMavenDependencyManagement === 'function') {
                this.addMavenDependencyManagement(
                    'org.springframework.cloud',
                    'spring-cloud-stream-dependencies',
                    STREAM_CLOUD_DEPENDENCY_VERSION,
                    'pom',
                    'import'
                );
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream');
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-starter-stream-rabbit');
            } else {
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-starter-stream-rabbit', STREAM_RABBIT_VERSION);
            }
        } else if (this.buildTool === 'gradle') {
            if (typeof this.addGradleDependencyManagement === 'function') {
                this.addGradleDependencyManagement(
                    'mavenBom',
                    'org.springframework.cloud',
                    'spring-cloud-stream-dependencies',
                    STREAM_CLOUD_DEPENDENCY_VERSION
                );
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream');
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-starter-stream-rabbit');
            } else {
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
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
        const messageName = this.rabbitMqNameOfMessage.charAt(0).toUpperCase() + this.rabbitMqNameOfMessage.slice(1);
        this.rabbitMessageName = messageName;
        this.rabbitMessageNameNonUcFirst = messageName.charAt(0).toLowerCase() + messageName.slice(1);

        // add Java classes
        this.template(
            'src/main/java/package/config/_CloudMessagingConfiguration.java',
            `${javaDir}config/CloudMessagingConfiguration.java`
        );

        // application-dev.yml
        const yamlAppDevProperties = {};
        utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.default.contentType', this, 'application/json');
        utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.bindings.input.destination', this, 'topic-jhipster');
        utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.bindings.output.destination', this, 'topic-jhipster');
        utilYaml.updatePropertyInArray(
            yamlAppDevProperties,
            'spring.cloud.stream.bindings.rabbit.bindings.output.producer.routingKeyExpression',
            this,
            'headers.title'
        );
        utilYaml.updateYamlProperties(`${resourceDir}config/application-dev.yml`, yamlAppDevProperties, this);

        // application-prod.yml
        const yamlAppProdProperties = yamlAppDevProperties;
        utilYaml.updatePropertyInArray(
            yamlAppProdProperties,
            'spring.cloud.stream.bindings.rabbit.bindings.output.producer.routingKeyExpression',
            this,
            'payload.title'
        );
        utilYaml.updateYamlProperties(`${resourceDir}config/application-prod.yml`, yamlAppProdProperties, this);
    }

    _installKafka() {
        const STREAM_RABBIT_VERSION = '1.3.0.RELEASE';
        const STREAM_CLOUD_DEPENDENCY_VERSION = 'Chelsea.SR2';
        const STREAM_CLOUD_STREAM_VERSION = '1.3.0.RELEASE';
        // read config from .yo-rc.json
        this.baseName = this.jhipsterAppConfig.baseName;
        this.packageName = this.jhipsterAppConfig.packageName;
        this.packageFolder = this.jhipsterAppConfig.packageFolder;
        this.clientFramework = this.jhipsterAppConfig.clientFramework;
        this.clientPackageManager = this.jhipsterAppConfig.clientPackageManager;
        this.buildTool = this.jhipsterAppConfig.buildTool;

        // use function in generator-base.js from generator-jhipster
        this.angularAppName = this.getAngularAppName();

        // const webappDir = jhipsterConstants.CLIENT_MAIN_SRC_DIR;
        this.log(`\nmessage broker type = ${this.messageBrokerType}`);
        this.log(`\nmessage broker type = ${this.rabbitMqNameOfMessage}`);
        this.log('------\n');

        // use constants from generator-constants.js
        const javaDir = `${jhipsterConstants.SERVER_MAIN_SRC_DIR + this.packageFolder}/`;
        const resourceDir = jhipsterConstants.SERVER_MAIN_RES_DIR;
        // add dependencies
        if (this.buildTool === 'maven') {
            if (typeof this.addMavenDependencyManagement === 'function') {
                this.addMavenDependencyManagement(
                    'org.springframework.cloud',
                    'spring-cloud-stream-dependencies',
                    STREAM_CLOUD_DEPENDENCY_VERSION,
                    'pom',
                    'import'
                );
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream');
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-starter-stream-rabbit');
            } else {
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
                this.addMavenDependency('org.springframework.cloud', 'spring-cloud-starter-stream-rabbit', STREAM_RABBIT_VERSION);
            }
        } else if (this.buildTool === 'gradle') {
            if (typeof this.addGradleDependencyManagement === 'function') {
                this.addGradleDependencyManagement(
                    'mavenBom',
                    'org.springframework.cloud',
                    'spring-cloud-stream-dependencies',
                    STREAM_CLOUD_DEPENDENCY_VERSION
                );
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream');
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-starter-stream-rabbit');
            } else {
                this.addGradleDependency('compile', 'org.springframework.cloud', 'spring-cloud-stream', STREAM_CLOUD_STREAM_VERSION);
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
        const messageName = this.rabbitMqNameOfMessage.charAt(0).toUpperCase() + this.rabbitMqNameOfMessage.slice(1);
        this.rabbitMessageName = messageName;
        this.rabbitMessageNameNonUcFirst = messageName.charAt(0).toLowerCase() + messageName.slice(1);

        // add Java classes
        this.template(
            'src/main/java/package/config/_CloudMessagingConfiguration.java',
            `${javaDir}config/CloudMessagingConfiguration.java`
        );

        // application-dev.yml
        const yamlAppDevProperties = {};
        utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.default.contentType', this, 'application/json');
        utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.bindings.input.destination', this, 'topic-jhipster');
        utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.bindings.output.destination', this, 'topic-jhipster');
        utilYaml.updatePropertyInArray(
            yamlAppDevProperties,
            'spring.cloud.stream.bindings.rabbit.bindings.output.producer.routingKeyExpression',
            this,
            'headers.title'
        );
        utilYaml.updateYamlProperties(`${resourceDir}config/application-dev.yml`, yamlAppDevProperties, this);

        // application-prod.yml
        const yamlAppProdProperties = yamlAppDevProperties;
        utilYaml.updatePropertyInArray(
            yamlAppProdProperties,
            'spring.cloud.stream.bindings.rabbit.bindings.output.producer.routingKeyExpression',
            this,
            'payload.title'
        );
        utilYaml.updateYamlProperties(`${resourceDir}config/application-prod.yml`, yamlAppProdProperties, this);
    }

    _useJdlExecution(_callback) {
        // run jdl script
        this._executeJdlScript(this.jhipsterAppConfig.applicationType === 'microservice');

        _callback();
    }

    /**
     *
     * @param {Boolean} skipClient
     */
    _executeJdlScript(skipClient) {
        if (this.jhipsterAppConfig.applicationType === 'microservice' && this.addFieldAndClassPrefix) {
            this._runMicroserviceScript(skipClient, `${MICROSERVICE_FILE_UPLOADS_JDL}`);
        }

        if (this.jhipsterAppConfig.applicationType === 'microservice' && !this.addFieldAndClassPrefix) {
            this._runMicroserviceScript(skipClient, `${FILE_UPLOADS_JDL}`);
        }

        if (this.jhipsterAppConfig.applicationType !== 'microservice') {
            this._runGeneralScript(skipClient, `${GENERAL_FILE_UPLOADS_JDL}`);
        }
    }

    /**
     *
     * @param {Boolean} skipClient
     * @param {String} jdlScriptFile
     */
    _runMicroserviceScript(skipClient, jdlScriptFile) {
        const jdlHasRan = this.async();
        const jdlRan = spawn(
            'jhipster',
            ['import-jdl', `.jhipster/${jdlScriptFile}.jdl`, '--fluent-methods=true ', `--skip-client=${skipClient} `],
            { stdio: 'inherit', shell: true, windowsVerbatimArguments: true, windowsHide: true }
        );
        jdlRan.on('error', error => {
            // Hopeful that this gives informative error messages
            this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
        });
        jdlRan.stdout.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stdout : ${data.message} \n`);
        });
        jdlRan.stderr.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stderr : ${data.message} \n`);
        });
        jdlHasRan();
    }

    /**
     * @deprecated todo replace this method with script-name args
     * @param {Boolean} skipClient
     * @param {String} jdlScriptFile
     */
    _runGeneralScript(skipClient, jdlScriptFile) {
        const generalClientRootFolder = GENERAL_CLIENT_ROOT_FOLDER;
        const jdlHasRan = this.async();
        const jdlRan = spawn(
            'jhipster',
            [
                'import-jdl',
                `.jhipster/${jdlScriptFile}.jdl`,
                '--fluent-methods=true ',
                `--skip-client=${skipClient} `,
                `--client-root-folder=${generalClientRootFolder}`
            ],
            { stdio: 'inherit', shell: true, windowsVerbatimArguments: true, windowsHide: true }
        );
        jdlRan.on('error', error => {
            // Hopeful that this gives informative error messages
            this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
        });
        jdlRan.stdout.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stdout : ${data.message} \n`);
        });
        jdlRan.stderr.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stderr : ${data.message} \n`);
        });
        jdlHasRan();
    }

    /**
     * This install back-end java code and liquibase migration configuration
     *
     * @param {String} javaDir path of the project's java directory
     * @param {String} javaTestDir path of the project's java test directory
     * @param {String} resourceDir  path of the main resource directory
     */
    _installServerCode(javaDir, javaTestDir, resourceDir) {
        // utility function to write templates
        this.template = function(source, destination) {
            this.fs.copyTpl(this.templatePath(source), this.destinationPath(destination), this);
        };

        this._installServerDependencies();

        // add Java source code
        this.template('src/main/java/package/internal/', `${javaDir}internal/`);

        // Add test code
        this.template('src/test/java/package/internal/', `${javaTestDir}internal/`);

        // TODO Add liquibase config for spring batch
        // Add liquibase resources
        this.changelogDate = this.dateFormatForLiquibase();
        this.template(
            'src/main/resources/config/liquibase/changelog/_added_springbatch_schema.xml',
            `${resourceDir}config/liquibase/changelog/${this.changelogDate}_added_springbatch_schema.xml`
        );
        this.addChangelogToLiquibase(`${this.changelogDate}_added_springbatch_schema.xml`);
    }

    /**
     * Install server libraries for handling excel and their DTOs
     */
    _installServerDependencies() {
        const OZLERHAKAN_POIJI_VERSION = '1.20.0';
        const OZLERHAKAN_POIJI_VERSION_PROPERTY = '${poiji.version}';
        const LOMBOK_VERSION = '1.18.6';
        const LOMBOK_VERSION_PROPERTY = '${lombok.version}';

        if (this.buildTool === 'maven') {
            if (typeof this.addMavenDependencyManagement === 'function') {
                this.addMavenDependency('com.github.ozlerhakan', 'poiji', OZLERHAKAN_POIJI_VERSION_PROPERTY);
                this.addMavenDependency('org.projectlombok', 'lombok', LOMBOK_VERSION_PROPERTY);
            } else {
                this.addMavenDependency('com.github.ozlerhakan', 'poiji', OZLERHAKAN_POIJI_VERSION_PROPERTY);
                this.addMavenDependency('org.projectlombok', 'lombok', LOMBOK_VERSION_PROPERTY);
            }
            this.addMavenAnnotationProcessor('org.projectlombok', 'lombok', LOMBOK_VERSION_PROPERTY);
            this.addMavenProperty('lombok.version', LOMBOK_VERSION);
            this.addMavenProperty('poiji.version', OZLERHAKAN_POIJI_VERSION);
        } else if (this.buildTool === 'gradle') {
            if (typeof this.addGradleDependencyManagement === 'function') {
                this.addGradleDependency('compile', 'com.github.ozlerhakan', 'poiji');
                this.addGradleDependency('compile', 'org.projectlombok', 'lombok');
            } else {
                this.addGradleDependency('compile', 'com.github.ozlerhakan', 'poiji', OZLERHAKAN_POIJI_VERSION_PROPERTY);
                this.addGradleDependency('compile', 'org.projectlombok', 'lombok', LOMBOK_VERSION_PROPERTY);
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
