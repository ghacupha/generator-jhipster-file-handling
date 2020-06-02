/* eslint-disable no-template-curly-in-string */
const chalk = require('chalk');
const semver = require('semver');
const BaseGenerator = require('generator-jhipster/generators/generator-base');
const { spawn } = require('child_process');
const jhipsterConstants = require('generator-jhipster/generators/generator-constants');
const packagejs = require('../../package.json');

module.exports = class extends BaseGenerator {
    get initializing() {
        return {
            init(args) {
                if (args === 'default') {
                    // do something when argument is 'default'
                    this.createClientCode = 'Do we need to create client code really?';
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
                this.log(`\nWelcome to the ${chalk.bold.yellow('JHipster file-handling')} generator! ${chalk.yellow(`v${packagejs.version}\n`)}`);
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
            checkDbType() {
                const currentDatabaseType = this.jhipsterAppConfig.databaseType;
                if (!currentDatabaseType === 'sql') {
                    this.env.error('\nYour generated project does not use sql database, this generator is designed for sql databases\n');
                }
            }
        };
    }

    prompting() {
        const prompts = [
            {
                when: () => typeof this.createClientCode === 'undefined',
                type: 'input',
                name: 'createClientCode',
                message: 'Do you want to create client code? (y/n)',
                default: 'n'
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
        this.applicationType = this.jhipsterAppConfig.applicationType;
        this.baseName = this.jhipsterAppConfig.baseName;
        this.packageName = this.jhipsterAppConfig.packageName;
        this.packageFolder = this.jhipsterAppConfig.packageFolder;
        this.clientFramework = this.jhipsterAppConfig.clientFramework;
        this.clientPackageManager = this.jhipsterAppConfig.clientPackageManager;
        this.buildTool = this.jhipsterAppConfig.buildTool;

        // Get the application name for use with main class
        this.appName = this.baseName.charAt(0).toUpperCase() + this.baseName.slice(1);

        // use function in generator-base.js from generator-jhipster
        this.angularAppName = this.getAngularAppName();

        // use constants from generator-constants.js
        const javaDir = `${jhipsterConstants.SERVER_MAIN_SRC_DIR + this.packageFolder}/`;
        const javaTestDir = `${jhipsterConstants.SERVER_TEST_SRC_DIR + this.packageFolder}/`;
        const resourceDir = jhipsterConstants.SERVER_MAIN_RES_DIR;
        const webappDir = jhipsterConstants.CLIENT_MAIN_SRC_DIR;
        const webappTestDir = jhipsterConstants.CLIENT_TEST_SRC_DIR;

        // variable from questions
        if (typeof this.createClientCode === 'undefined') {
            this.createClientCode = this.promptAnswers.createClientCode;
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
        this.log(`create client code?=${this.createClientCode}`);
        this.log('------\n');

        // if (!this.applicationType === 'gateway') {
        //     this._installServerCode(javaDir, javaTestDir, resourceDir);
        // }
        //
        // if (this.createClientCode === 'y') {
        //     if (this.clientFramework === 'angularX') {
        //         // only works if it's angular code
        //         this._installClientCode(webappDir, webappTestDir, this.angularAppName, this.clientFramework);
        //     }
        // }

        const /** @param {String}*/ jdlScript =
                'entity FileType {\n' +
                '  fileTypeName String required unique,\n' +
                '  fileMediumType FileMediumTypes required,\n' +
                '  description String,\n' +
                '  fileTemplate AnyBlob,\n' +
                '  fileType FileModelType\n' +
                '}\n' +
                'entity FileUpload {\n' +
                '  description String required,\n' +
                '  fileName String required unique,\n' +
                '  periodFrom LocalDate,\n' +
                '  periodTo LocalDate,\n' +
                '  fileTypeId Long required,\n' +
                '  dataFile AnyBlob required,\n' +
                '  uploadSuccessful Boolean,\n' +
                '  uploadProcessed Boolean,\n' +
                '  uploadToken String unique\n' +
                '}\n' +
                'entity MessageToken {\n' +
                '  description String,\n' +
                '  timeSent Long required,\n' +
                '  tokenValue String required,\n' +
                '  received Boolean,\n' +
                '  actioned Boolean,\n' +
                '  contentFullyEnqueued Boolean\n' +
                '}\n' +
                'enum FileMediumTypes {\n' +
                '  EXCEL,\n' +
                '  EXCEL_XLS,\n' +
                '  EXCEL_XLSX,\n' +
                '  EXCEL_XLSB,\n' +
                '  EXCEL_CSV,\n' +
                '  EXCEL_XML,\n' +
                '  PDF,\n' +
                '  POWERPOINT,\n' +
                '  DOC,\n' +
                '  TEXT,\n' +
                '  JSON,\n' +
                '  HTML5\n' +
                '}\n' +
                'enum FileModelType {\n' +
                '  SERVICE_OUTLETS,\n' +
                '  SBU_LIST,\n' +
                '  CURRENCY_LIST,\n' +
                '  DEPOSIT_LIST,\n' +
                '  BRANCH_LIST,\n' +
                '   FX_RATES,\n' +
                '  SCHEME_LIST,\n' +
                '  SECTORS,\n' +
                '  SUB_SECTORS,\n' +
                '  GENERAL_LEDGERS\n' +
                '}\n' +
                'dto FileUpload, MessageToken with mapstruct\n' +
                'paginate FileType, FileUpload, MessageToken with pagination\n' +
                'service FileUpload, MessageToken with serviceImpl\n' +
                'service FileType with serviceClass\n' +
                'microservice FileType, FileUpload, MessageToken with depositAnalysisMain\n' +
                'filter FileType, FileUpload, MessageToken\n' +
                'clientRootFolder FileType, FileUpload, MessageToken with depositAnalysisMain';

        // todo make microservice name in the script dynamic
        // todo make client root folder dynamic
        // todo make arry of Model types dynamic
        let ls = spawn('jhipster', ['import-jdl', '--inline', '${jdlScript}']);

        ls.stdout.on('data', function(data) {
            console.log('stdout: ' + data);
        });

        ls.stderr.on('data', function(data) {
            console.log('stderr: ' + data);
        });

        ls.on('exit', function(code) {
            console.log('child process exited with code ' + code);
        });
    }

    /**
     * This install back-end java code and liquibase migration configuration
     *
     * @param {String} javaDir path of the project's java directory
     * @param {String} javaTestDir path of the project's java test directory
     * @param {String} resourceDir  path of the main resource directory
     */
    _installServerCode(javaDir, javaTestDir, resourceDir) {
        // // TODO work on backend code
        // // utility function to write templates
        // this.template = function(source, destination) {
        //     this.fs.copyTpl(this.templatePath(source), this.destinationPath(destination), this);
        // };
        //
        // this._installServerDependencies();
        //
        // // add Java source code
        // this.template('src/main/java/package/domain/', `${javaDir}domain/`);
        // this.template('src/main/java/package/repository/', `${javaDir}repository/`);
        // this.template('src/main/java/package/service/', `${javaDir}service/`);
        // this.template('src/main/java/package/web/', `${javaDir}web/`);
        //
        // // Add test code
        // this.template('src/test/java/package/domain/', `${javaTestDir}domain/`);
        // this.template('src/test/java/package/web/', `${javaTestDir}web/`);
        // this.template('src/test/java/package/service/', `${javaTestDir}service/`);
        //
        // // Add liquibase resources
        // this.template('src/main/resources/config/liquibase/fake-data/', `${resourceDir}config/liquibase/fake-data/`);
        // this.changelogDate = this.dateFormatForLiquibase();
        // this.template(
        //     'src/main/resources/config/liquibase/changelog/_added_entity_FileUpload.xml',
        //     `${resourceDir}config/liquibase/changelog/${this.changelogDate}_added_entity_FileUpload.xml`
        // );
        // this.template(
        //     'src/main/resources/config/liquibase/changelog/_added_entity_FileType.xml',
        //     `${resourceDir}config/liquibase/changelog/${this.changelogDate}_added_entity_FileType.xml`
        // );
        // this.addChangelogToLiquibase(`${this.changelogDate}_added_entity_FileUpload`);
        // this.addChangelogToLiquibase(`${this.changelogDate}_added_entity_FileType`);
    }

    /**
     * Runs templates for front-end js code and html templates. Only called if the
     * app is not a microservice
     *
     * @param {String} webappDir
     * @param {String} webappTestDir
     * @param {String} angularAppName
     * @param {String} clientFramework
     */
    _installClientCode(webappDir, webappTestDir, angularAppName, clientFramework) {
        // this.template = function(source, destination) {
        //     this.fs.copyTpl(this.templatePath(source), this.destinationPath(destination), this);
        // };
        // // Install sample code
        // this.template('src/main/webapp/scripts/app/fortune/', `${webappDir}app/fortune/`);
        // // install file-upload code
        // this.template('src/main/webapp/scripts/app/entities/', `${webappDir}app/entities/`);
        // this.template('src/main/webapp/scripts/app/shared/', `${webappDir}app/shared/`);
        // this.template('src/test/javascript/e2e/', `${webappTestDir}e2e/`);
        // this.template('src/test/javascript/spec/', `${webappTestDir}spec/`);
        // this.addElementToMenu('fortune', 'sunglasses', true, clientFramework);
        //
        // // TODO module for file-uploads this.addAngularModule();
        // // TODO Add entities to module
        // this.addEntityToModule('fileUpload', 'FileUpload', 'FileUpload', 'fileUploads', 'file-upload', 'file-upload', '');
        // this.addEntityToModule('fileType', 'FileType', 'FileType', 'fileUploads', 'file-type', 'file-type', '');
        //
        // // TODO Add entities menu
        // this.addEntityToMenu('file-type', 'times', true, clientFramework);
        // this.addEntityToMenu('file-upload', 'times', true, clientFramework);
        //
        // // todo loop the language elements array
        // this.addElementTranslationKey('fortune', 'Fortune', 'en');
        // this.addElementTranslationKey('fortune', 'Fortune', 'fr');
        //
        // // copy all language files
        // this.template('src/main/webapp/i18n/', `${webappDir}i18n/`);
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

    end() {
        this.log('Done; File handling entities and code installed');
    }
};
