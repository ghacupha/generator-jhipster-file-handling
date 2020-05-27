const chalk = require('chalk');
const semver = require('semver');
const BaseGenerator = require('generator-jhipster/generators/generator-base');
const jhipsterConstants = require('generator-jhipster/generators/generator-constants');
const packagejs = require('../../package.json');

module.exports = class extends BaseGenerator {
    get initializing() {
        return {
            init(args) {
                if (args === 'default') {
                    // do something when argument is 'default'
                    this.createClientCode = 'default message';
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
                createClientCode: 'Do you want to create client code? (y/n)',
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
        this.log(`message=${this.message}`);
        this.log('------\n');

        // utility function to write templates
        this.template = function(source, destination) {
            this.fs.copyTpl(this.templatePath(source), this.destinationPath(destination), this);
        };

        this._install_dependencies();

        // add Java classes
        this.template('src/main/java/package/domain/enumeration/_FileMediumTypes.java', `${javaDir}domain/enumeration/FileMediumTypes.java`);
        this.template('src/main/java/package/domain/enumeration/_FileModelType.java', `${javaDir}domain/enumeration/FileModelType.java`);
        this.template('src/main/java/package/domain/_FileType.java', `${javaDir}domain/FileType.java`);
        this.template('src/main/java/package/domain/_FileUpload.java', `${javaDir}domain/FileUpload.java`);

        // add repository items
        this.template('src/main/java/package/repository/_FileTypeRepository.java', `${javaDir}repository/FileTypeRepository.java`);
        this.template('src/main/java/package/repository/_FileUploadRepository.java', `${javaDir}repository/FileUploadRepository.java`);

        // add services
        this.template('src/main/java/package/service/_FileTypeQueryService.java', `${javaDir}service/FileTypeQueryService.java`);
        this.template('src/main/java/package/service/_FileTypeService.java', `${javaDir}service/FileTypeService.java`);
        this.template('src/main/java/package/service/_FileUploadQueryService.java', `${javaDir}service/FileUploadQueryService.java`);
        this.template('src/main/java/package/service/_FileUploadService.java', `${javaDir}service/FileUploadService.java`);

        // add data transfer objects
        this.template('src/main/java/package/service/dto/_FileUploadDTO.java', `${javaDir}service/dto/FileUploadDTO.java`);
        this.template('src/main/java/package/service/dto/_FileUploadCriteria.java', `${javaDir}service/dto/FileUploadCriteria.java`);
        this.template('src/main/java/package/service/dto/_FileTypeCriteria.java', `${javaDir}service/dto/FileTypeCriteria.java`);

        this.template('src/main/java/package/service/impl/_FileUploadServiceImpl.java', `${javaDir}service/impl/FileUploadServiceImpl.java`);
        this.template('src/main/java/package/service/mapper/_FileUploadMapper.java', `${javaDir}service/mapper/FileUploadMapper.java`);

        // add resource
        this.template('src/main/java/package/web/rest/_FileUploadResource.java', `${javaDir}web/rest/FileUploadResource.java`);
        this.template('src/main/java/package/web/rest/_FileTypeResource.java', `${javaDir}web/rest/FileTypeResource.java`);

        // Add test code
        this.template('src/test/java/package/domain/_FileTypeTest.java', `${javaTestDir}domain/FileTypeTest.java`);
        this.template('src/test/java/package/domain/_FileUploadTest.java', `${javaTestDir}domain/FileUploadTest.java`);
        this.template('src/test/java/package/service/dto/_FileUploadDTOTest.java', `${javaTestDir}service/dto/FileUploadDTOTest.java`);
        this.template('src/test/java/package/service/mapper/_FileUploadMapperTest.java', `${javaTestDir}service/mapper/FileUploadMapperTest.java`);
        this.template('src/test/java/package/web/rest/_FileUploadResourceIT.java', `${javaTestDir}web/rest/FileUploadResourceIT.java`);
        this.template('src/test/java/package/web/rest/_FileTypeResourceIT.java', `${javaTestDir}web/rest/FileTypeResourceIT.java`);

        // Add liquibase resources
        this.template('src/main/resources/config/liquibase/fake-data/_file_type.csv', `${resourceDir}config/liquibase/fake-data/file_type.csv`);
        this.template('src/main/resources/config/liquibase/fake-data/_file_upload.csv', `${resourceDir}config/liquibase/fake-data/file_upload.csv`);
        this.template('src/main/resources/config/liquibase/fake-data/blob/hipster.png', `${resourceDir}config/liquibase/fake-data/blob/hipster.png`);
        this.changelogDate = this.dateFormatForLiquibase();
        this.template(
            'src/main/resources/config/liquibase/changelog/_added_entity_FileUpload.xml',
            `${resourceDir}config/liquibase/changelog/${this.changelogDate}_added_entity_FileUpload.xml`
        );
        this.template(
            'src/main/resources/config/liquibase/changelog/_added_entity_FileType.xml',
            `${resourceDir}config/liquibase/changelog/${this.changelogDate}_added_entity_FileType.xml`
        );
        this.addChangelogToLiquibase(`${this.changelogDate}_added_entity_FileUpload`);
        this.addChangelogToLiquibase(`${this.changelogDate}_added_entity_FileType`);

        if (this.createClientCode === 'y') {
            if (this.clientFramework === 'angularX') {
                this._install_client_code(webappDir);
            }
        }
    }

    _install_client_code(webappDir) {
        this.template('src/main/webapp/scripts/app/fortune/_fortune.controller.js', `${webappDir}app/fortune/fortune.controller.js`);
        this.template('src/main/webapp/scripts/app/fortune/_fortune.html', `${webappDir}app/fortune/fortune.html`);
        this.template('src/main/webapp/scripts/app/fortune/_fortune.js', `${webappDir}app/fortune/fortune.js`);
        this.template('src/main/webapp/scripts/app/fortune/_fortune.service.js', `${webappDir}app/fortune//fortune.service.js`);
        this.addElementToMenu('fortune', 'sunglasses', true, this.clientFramework);
        this.addElementTranslationKey('fortune', 'Fortune', 'en');
        this.addElementTranslationKey('fortune', 'Fortune', 'fr');

        this.template('src/main/webapp/i18n/en/fortune.json', `${webappDir}i18n/en/fortune.json`);
        this.template('src/main/webapp/i18n/fr/fortune.json', `${webappDir}i18n/fr/fortune.json`);
    }

    _install_dependencies() {
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

    /*install() {
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
    }*/

    end() {
        this.log('Done; File handling entities and code installed');
    }
};
