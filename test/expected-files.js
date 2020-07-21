const constants = require('generator-jhipster/generators/generator-constants');

const SERVER_MAIN_SRC_DIR = constants.SERVER_MAIN_SRC_DIR;
const SERVER_MAIN_RES_DIR = constants.SERVER_MAIN_RES_DIR;
const SERVER_TEST_SRC_DIR = constants.SERVER_TEST_SRC_DIR;

const expectedFiles = {
    server: [
        // Add configuration files to list
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/config/FileUploadsProperties.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/config/FileUploadsPropertyFactory.java`,

        // Add internal server code to the list
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/currencyTable/CurrencyTableBatchConfig.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/currencyTable/CurrencyTableListItemReader.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/currencyTable/package-info.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/BatchConfigs.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/ListPartition.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/package-info.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/batch/PersistenceJobListener.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/excel/deserializer/DefaultExcelFileDeserializer.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/excel/deserializer/DeserializationUtils.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/excel/ExcelDeserializerContainer.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/excel/ExcelFileDeserializer.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/excel/PoijiOptionsConfig.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/BatchSupportedFileUploadProcessor.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/FileUploadProcessor.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/FileUploadProcessorChain.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/FileUploadProcessorContainer.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/package-info.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/StringTokenValueSearch.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/fileProcessing/TokenValueSearch.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/sampleDataModel/CurrencyTableBatchService.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/sampleDataModel/CurrencyTableEVM.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/sampleDataModel/CurrencyTableEVMMapping.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/sampleDataModel/package-info.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/DeleteMessageDTO.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/FileNotification.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/package-info.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/Tokenizable.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/model/TokenizableMessage.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/report/QueryTools.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/report/Report.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/resource/decorator/FileUploadResourceDecorator.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/resource/decorator/IFileUploadResource.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/resource/AppFileUploadResource.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/resource/ReportList.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/service/BatchService.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/service/FileNotificationHandlingService.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/service/HandlingService.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/service/package-info.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/util/TokenGenerator.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/AppConstants.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/Mapping.java`,
        `${SERVER_MAIN_SRC_DIR}io/github/currencies/internal/package-info.java`,

        // Add test files to list
        `${SERVER_TEST_SRC_DIR}io/github/currencies/config/FileUploadPropertiesIT.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/internal/excel/ExcelFileUtilsIT.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/internal/excel/ExcelFileUtilsTest.java`,
        `${SERVER_TEST_SRC_DIR}io/github/currencies/internal/excel/ExcelTestUtil.java`
    ],

    resources: [`${SERVER_MAIN_RES_DIR}config/fileUploads.yml`],

    liquibase: [`${SERVER_MAIN_RES_DIR}config/liquibase/changelog/20200720071941_added_springbatch_schema.xml`]
};

module.exports = expectedFiles;
