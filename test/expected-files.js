const constants = require('generator-jhipster/generators/generator-constants');

const SERVER_MAIN_SRC_DIR = constants.SERVER_MAIN_SRC_DIR;
const SERVER_MAIN_RES_DIR = constants.SERVER_MAIN_RES_DIR;
const SERVER_TEST_SRC_DIR = constants.SERVER_TEST_SRC_DIR;

const expectedFiles = {
    server: [
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/config/FileUploadsProperties.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/config/FileUploadsPropertyFactory.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/config/FileUploadPropertiesIT.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/internal/excel/ExcelFileUtilsIT.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/internal/excel/ExcelFileUtilsTest.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/internal/excel/ExcelTestUtil.java`
    ],

    resources: [`${SERVER_MAIN_RES_DIR}config/fileUploads.yml`],

    liquibase: [`${SERVER_MAIN_RES_DIR}config/liquibase/changelog/20200720071941_added_springbatch_schema.xml`]
};

module.exports = expectedFiles;
