/* eslint-disable prettier/prettier */
const chalk = require('chalk');
const semver = require('semver');
const BaseGenerator = require('generator-jhipster/generators/generator-base');
const jhipsterConstants = require('generator-jhipster/generators/generator-constants');
const { spawn } = require('child_process');
const packagejs = require('../../package.json');

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
            checkGit() {
                this.log('Experience has shown that this kind of thing really needs a working knowledge. So beware');
            }
        };
    }

    prompting() {
        const prompts = [
            {
                when: () => typeof this.message === 'undefined',
                type: 'input',
                name: 'message',
                message: 'Do you want front end code installed?',
                default: 'y'
            },
            {
                when: () =>
                    (typeof this.gatewayMicroserviceName === 'undefined' && this.jhipsterAppConfig.applicationType === 'microservice') ||
                    this.jhipsterAppConfig.applicationType === 'gateway',
                type: 'input',
                name: 'gatewayMicroserviceName',
                message: 'What is the microservice name for the file handling workflow?',
                default: `${this.jhipsterAppConfig.baseName}Main`
            }
        ];

        const done = this.async();
        this.prompt(prompts).then(answers => {
            this.promptAnswers = answers;
            // To access props answers use this.promptAnswers.someOption;
            done();
        });
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
        if (typeof this.message === 'undefined') {
            this.message = this.promptAnswers.message;
        }

        if (typeof this.gatewayMicroserviceName === 'undefined') {
            this.gatewayMicroserviceName = this.promptAnswers.gatewayMicroserviceName;
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
        if (this.jhipsterAppConfig.applicationType === 'gateway') {
            this.log(`Gateway microservice name?=${this.gatewayMicroserviceName}`);
        }
        this.log('------\n');

        this.template = function(source, destination) {
            this.fs.copyTpl(this.templatePath(source), this.destinationPath(destination), this);
        };

        const createdJdl = this.async();
        if (this.jhipsterAppConfig.applicationType === 'microservice') {
            this.template('_fileUploads.jdl', '.jhipster/fileUploads.jdl');
        } else {
            this.template('fileUploads-general.jdl', '.jhipster/fileUploads-general.jdl');
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
        if (this.jhipsterAppConfig.applicationType === 'microservice') {
            this._runMicroserviceScript(skipClient);
        } else {
            this._runGeneralScript(skipClient);
        }
    }

    _runMicroserviceScript(skipClient) {
        const jdlHasRan = this.async();
        const jdlRan = spawn(
            'jhipster',
            [
                'import-jdl',
                '.jhipster/fileUploads.jdl',
                '--fluent-methods=true ',
                `--skip-client=${skipClient} `,
                '--client-root-folder=fileUploads'
            ],
            { stdio: 'inherit', shell: true, windowsVerbatimArguments: true, windowsHide: true }
        );
        jdlRan.on('error', error => {
            // Hopeful that this gives informative error messages
            this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
        });
        jdlRan.stdout.on('data', data => {
            // Hopeful that this gives informative error messages
            this.log(`JDl Stdout : ${data.message} \n`);
        });
        jdlRan.stderr.on('data', data => {
            // Hopeful that this gives informative error messages
            this.log(`JDl Stderr : ${data.message} \n`);
        });
        jdlHasRan();
    }

    _runGeneralScript(skipClient) {
        const jdlHasRan = this.async();
        const jdlRan = spawn(
            'jhipster',
            [
                'import-jdl',
                '.jhipster/fileUploads-general.jdl',
                '--fluent-methods=true ',
                `--skip-client=${skipClient} `,
                '--client-root-folder=fileUploads'
            ],
            { stdio: 'inherit', shell: true, windowsVerbatimArguments: true, windowsHide: true }
        );
        jdlRan.on('error', error => {
            // Hopeful that this gives informative error messages
            this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
        });
        jdlRan.stdout.on('data', data => {
            // Hopeful that this gives informative error messages
            this.log(`JDl Stdout : ${data.message} \n`);
        });
        jdlRan.stderr.on('data', data => {
            // Hopeful that this gives informative error messages
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
