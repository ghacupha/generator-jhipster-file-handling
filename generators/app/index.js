/* eslint-disable prettier/prettier */
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

        // use constants from generator-constants.js
        const javaDir = `${jhipsterConstants.SERVER_MAIN_SRC_DIR + this.packageFolder}/`;
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

        const createdJdl = this.async();
        if (this.addFieldAndClassPrefix && this.jhipsterAppConfig.applicationType === 'microservice') {
            this.template(`${MICROSERVICE_FILE_UPLOADS_JDL}.jdl.ejs`, `.jhipster/${MICROSERVICE_FILE_UPLOADS_JDL}.jdl`);
        }

        if (!this.addFieldAndClassPrefix && this.jhipsterAppConfig.applicationType === 'microservice') {
            this.template(`${FILE_UPLOADS_JDL}.jdl.ejs`, `.jhipster/${FILE_UPLOADS_JDL}.jdl`);
        }

        if (this.jhipsterAppConfig.applicationType !== 'microservice') {
            this.template(`${GENERAL_FILE_UPLOADS_JDL}.jdl.ejs`, `.jhipster/${GENERAL_FILE_UPLOADS_JDL}.jdl`);
        }
        createdJdl();

        this._useJdlExecution();
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
