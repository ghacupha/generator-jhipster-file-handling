const path = require('path');
const fse = require('fs-extra');
const assert = require('yeoman-assert');
const helpers = require('yeoman-test');
const expectedFiles = require('./expected-files');

describe('JHipster generator file-handling', () => {
    describe('Test with Maven microservice and no file model types', () => {
        beforeEach(done => {
            helpers
                .run(path.join(__dirname, '../generators/app'))
                .inTmpDir(dir => {
                    fse.copySync(path.join(__dirname, '../test/templates/maven'), dir);
                })
                .withOptions({
                    testmode: true
                })
                .withPrompts({
                    gatewayMicroserviceName: 'TestMain',
                    addFieldAndClassPrefix: true,
                    fileModelTypes: ''
                })
                .on('end', done);
        });

        it('Creates expected default files for file-uploads', () => {
            assert.file(expectedFiles.resources);
            assert.file(expectedFiles.server);
            // assert.file(expectedFiles.liquibase);
        });
    });

    describe('Test with Gradle microservice and no file model types', () => {
        beforeEach(done => {
            helpers
                .run(path.join(__dirname, '../generators/app'))
                .inTmpDir(dir => {
                    fse.copySync(path.join(__dirname, '../test/templates/gradle'), dir);
                })
                .withOptions({
                    testmode: true
                })
                .withPrompts({
                    gatewayMicroserviceName: 'TestMain',
                    addFieldAndClassPrefix: true,
                    fileModelTypes: ''
                })
                .on('end', done);
        });

        it('Creates expected default files for file-uploads', () => {
            // TODO review gradle directory structure
            assert.file(expectedFiles.resources);
            assert.file(expectedFiles.server);
            // assert.file(expectedFiles.liquibase);
        });
    });

    describe('Test with Gradle monolith and no file model types', () => {
        beforeEach(done => {
            helpers
                .run(path.join(__dirname, '../generators/app'))
                .inTmpDir(dir => {
                    fse.copySync(path.join(__dirname, '../test/templates/gradle-react-monolith'), dir);
                })
                .withOptions({
                    testmode: true
                })
                .withPrompts({
                    gatewayMicroserviceName: 'fileUploads',
                    generalClientRootFolder: 'fileUploads',
                    addFieldAndClassPrefix: true,
                    fileModelTypes: 'CONTACTS'
                })
                .on('end', done);
        });

        it('Creates expected default resource files for file-uploads', () => {
            // TODO review gradle directory structure
            assert.file(expectedFiles.resources);
            // assert.file(expectedFiles.liquibase);
        });

        it('Creates expected default server files for file-uploads', () => {
            // TODO review gradle directory structure
            assert.file(expectedFiles.server);
            // assert.file(expectedFiles.liquibase);
        });
    });
});
