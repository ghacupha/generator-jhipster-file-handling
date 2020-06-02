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
                this.log('Experience has shown that this kind of thing really needs, good grasp of git. So beware');
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
                when: () => typeof this.gatewayMicroserviceName === 'undefined' && this.jhipsterAppConfig.applicationType === 'gateway',
                type: 'input',
                name: 'gatewayMicroserviceName',
                message: 'This being a gateway, what name is the microservice with the file handling service?',
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
        let gatewayMicroserviceName;
        if (typeof this.message === 'undefined') {
            this.message = this.promptAnswers.message;
        }

        if (typeof gatewayMicroserviceName === 'undefined') {
            gatewayMicroserviceName = this.promptAnswers.gatewayMicroserviceName;
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
            this.log(`Gateway microservice name?=${gatewayMicroserviceName}`);
        }
        this.log('------\n');

        // make skip-server true if gateway application
        this._useJdlExecution(gatewayMicroserviceName);
    }

    _useJdlExecution(gatewayMicroserviceName) {
        const /** @param {Boolean} */ skipServer = this.jhipsterAppConfig.applicationType === 'gateway';
        // make skip-client true if microservice application
        let /** @param {Boolean} */ skipClient;
        if (this.jhipsterAppConfig.applicationType !== 'microservice' && this.message === 'n') {
            skipClient = false;
        } else {
            skipClient = this.jhipsterAppConfig.applicationType === 'microservice';
        }
        let /** @param {String} */ microserviceConfig = '';
        if (this.jhipsterAppConfig.applicationType === 'microservice') {
            const /** @param {String} */ microserviceName = this.getMicroserviceAppName(this.baseName);
            microserviceConfig = `microservice FileType, FileUpload with ${microserviceName}`;
        } else if (this.jhipsterAppConfig.applicationType === 'gateway') {
            microserviceConfig = `microservice FileType, FileUpload with ${gatewayMicroserviceName}`;
        }
        const /** @param {String} */ jdlScript =
                '  entity FileType { \n' +
                '    fileTypeName String required unique, \n' +
                '    fileMediumType FileMediumTypes required, \n' +
                '    description String, \n' +
                '    fileTemplate AnyBlob, \n' +
                '    fileType FileModelType \n' +
                '  } \n' +
                '  entity FileUpload { \n' +
                '    description String required, \n' +
                '    fileName String required unique, \n' +
                '    periodFrom LocalDate, \n' +
                '    periodTo LocalDate, \n' +
                '    fileTypeId Long required, \n' +
                '    dataFile AnyBlob required, \n' +
                '    uploadSuccessful Boolean, \n' +
                '    uploadProcessed Boolean, \n' +
                '    uploadToken String unique \n' +
                '  } \n' +
                '  enum FileMediumTypes { \n' +
                '    EXCEL, \n' +
                '    EXCEL_XLS, \n' +
                '    EXCEL_XLSX, \n' +
                '    EXCEL_XLSB, \n' +
                '    EXCEL_CSV, \n' +
                '    EXCEL_XML, \n' +
                '    PDF, \n' +
                '    POWERPOINT, \n' +
                '    DOC, \n' +
                '    TEXT, \n' +
                '    JSON, \n' +
                '    HTML5 \n' +
                '  } \n' +
                '  enum FileModelType { \n' +
                '    SERVICE_OUTLETS, \n' +
                '    TRANSACTION_ACCOUNTS, \n' +
                '    PREPAYMENT_ENTRIES, \n' +
                '    AMORTIZATION_ENTRIES \n' +
                '} \n' +
                ' \n' +
                'paginate FileType, FileUpload with pagination \n' +
                'service FileType with serviceClass \n' +
                'service FileUpload with serviceImpl \n' +
                'filter FileType, FileUpload \n' +
                'clientRootFolder FileType, FileUpload with fileUploads \n' +
                'dto FileUpload with mapstruct';
        // eslint-disable-next-line no-unused-expressions
        `${microserviceConfig}`;

        // run jdl script
        this._executeJdlScript(jdlScript, skipServer, skipClient);
    }

    _executeJdlScript(jdlScript, skipServer, skipClient) {
        const written = this.async();
        spawn(
            'jhipster',
            [
                'import-jdl',
                '--inline ',
                `${jdlScript}`,
                '--from-cli=false ',
                '--fluent-methods=true ',
                `--skip-server=${skipServer} `,
                // `--skip-db-changelog=${skipServer} `,
                `--skip-client=${skipClient} `,
                '--client-root-folder=fileUploads'
            ],
            { stdio: 'inherit', stdin: 'write', stdout: 'write' }
        )
            .on('error', error => {
                // Hopeful that this gives informative error messages
                this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
            })
            .on('close', code => {
                this.log(`\nJDL generate process exited with code ${code}\n`);
                this.log(
                    // eslint-disable-next-line quotes
                    "I have tried removing backend code from gateways, but it's not working. So if this is a gateway try removing it yourself with 'git stash'. My apologies"
                );
                written();
            });
    }

    // todo review need for running install
    install() {
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
