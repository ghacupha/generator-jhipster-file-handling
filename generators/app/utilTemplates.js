/**
 * Copyright 2013-2020 the Edwin Njeru and the respective JHipster contributors.
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

/**
 * This method lists the files in the templates and how they are to be copied into the client code
 *
 * @param {Object} gen Generator
 * @returns {({from: string, to: string})[]}
 */
function getTemplateFiles(gen) {
    // Initialize important variables
    const /** {String} */ javaTemplateDir = gen.javaTemplateDir;
    const /** {String} */ javaTemplateResourceDir = gen.javaTemplateResourceDir;
    const /** {String} */ javaDir = gen.javaDir;
    const /** {String} */ javaTemplateTestDir = gen.javaTemplateTestDir;
    const /** {String} */ javaTestDir = gen.javaTestDir;
    const /** {String} */ resourceDir = gen.resourceDir;
    const /** {String} */ resourceTestDir = gen.resourceTestDir;

    /**
     * Configuration file for file-uploads as coded in the package/config folder. These configure spring-boot
     * to read file-uppload properties from the fileUploads.yml files
     *
     * @type {{from: string, to: string}[]}
     */
    const fileUploadConfigFiles = [
        {
            from: `${javaTemplateDir}/config/FileUploadsProperties.java`,
            to: `${javaDir}/config/FileUploadsProperties.java`
        },
        {
            from: `${javaTemplateDir}/config/FileUploadsPropertyFactory.java`,
            to: `${javaDir}/config/FileUploadsPropertyFactory.java`
        }
    ];

    /**
     * Returns an array with the java batch workflow files
     *
     * @type {({from: string, to: string})[]}
     */
    const batchFiles = [
        // Batch files
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
        }
    ];

    /**
     * Returns an array with the java excel files workflow templates
     *
     * @type {({from: string, to: string})[]}
     */
    const excelWorkflowFiles = [
        // Excel workflow files
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
        }
    ];

    /**
     * Returns an array with the fileProcessing workflow template files
     *
     * @type {({from: string, to: string})[]}
     */
    const fileProcessingWorkflowFiles = [
        {
            from: `${javaTemplateDir}/internal/fileProcessing/StringTokenValueSearch.java`,
            to: `${javaDir}/internal/fileProcessing/StringTokenValueSearch.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/TokenValueSearch.java`,
            to: `${javaDir}/internal/fileProcessing/TokenValueSearch.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/BatchSupportedFileUploadProcessor.java`,
            to: `${javaDir}/internal/fileProcessing/BatchSupportedFileUploadProcessor.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/FileUploadProcessor.java`,
            to: `${javaDir}/internal/fileProcessing/FileUploadProcessor.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/FileUploadProcessorChain.java`,
            to: `${javaDir}/internal/fileProcessing/FileUploadProcessorChain.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/FileUploadProcessorContainer.java`,
            to: `${javaDir}/internal/fileProcessing/FileUploadProcessorContainer.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing//package-info.java`,
            to: `${javaDir}/internal/fileProcessing/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/StringTokenValueSearch.java`,
            to: `${javaDir}/internal/fileProcessing/StringTokenValueSearch.java`
        },
        {
            from: `${javaTemplateDir}/internal/fileProcessing/TokenValueSearch.java`,
            to: `${javaDir}/internal/fileProcessing/TokenValueSearch.java`
        }
    ];

    /**
     * Returns an array with the sample data for currency-table entity
     *
     * @type {({from: string, to: string})[]}
     */
    const sampleDataModelFiles = [
        {
            from: `${javaTemplateDir}/internal/model/sampleDataModel/package-info.java`,
            to: `${javaDir}/internal/model/sampleDataModel/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/sampleDataModel/CurrencyTableBatchService.java`,
            to: `${javaDir}/internal/model/sampleDataModel/CurrencyTableBatchService.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/sampleDataModel/CurrencyTableEVM.java`,
            to: `${javaDir}/internal/model/sampleDataModel/CurrencyTableEVM.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/sampleDataModel/CurrencyTableEVMMapping.java`,
            to: `${javaDir}/internal/model/sampleDataModel/CurrencyTableEVMMapping.java`
        },
        {
            from: `${javaTemplateTestDir}/internal/model/sampleDataModel/CurrencyTableEVMMappingTest.java`,
            to: `${javaTestDir}/internal/model/sampleDataModel/CurrencyTableEVMMappingTest.java`
        }
    ];

    /**
     * General purpose model and data representation types
     *
     * @type {{from: string, to: string}[]}
     */
    const modelFiles = [
        {
            from: `${javaTemplateDir}/internal/model/package-info.java`,
            to: `${javaDir}/internal/model/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/FileNotification.java`,
            to: `${javaDir}/internal/model/FileNotification.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/DeleteMessageDTO.java`,
            to: `${javaDir}/internal/model/DeleteMessageDTO.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/Tokenizable.java`,
            to: `${javaDir}/internal/model/Tokenizable.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/TokenizableMessage.java`,
            to: `${javaDir}/internal/model/TokenizableMessage.java`
        }
    ];

    /**
     * Returns an array with the report template files
     *
     * @type {({from: string, to: string})[]}
     */
    const reportFiles = [
        {
            from: `${javaTemplateDir}/internal/report/QueryTools.java`,
            to: `${javaDir}/internal/report/QueryTools.java`
        },
        {
            from: `${javaTemplateDir}/internal/report/Report.java`,
            to: `${javaDir}/internal/report/Report.java`
        }
    ];

    /**
     * Returns an array with the custom internal java resources files
     *
     * @type {({from: string, to: string})[]}
     */
    const internalResourceFiles = [
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
        }
    ];

    /**
     * Returns an array with custom internal java services
     *
     * @type {{from: string, to: string}[]}
     */
    const serviceFiles = [
        {
            from: `${javaTemplateDir}/internal/service/BatchService.java`,
            to: `${javaDir}/internal/service/BatchService.java`
        },
        {
            from: `${javaTemplateDir}/internal/service/FileNotificationHandlingService.java`,
            to: `${javaDir}/internal/service/FileNotificationHandlingService.java`
        },
        {
            from: `${javaTemplateDir}/internal/service/HandlingService.java`,
            to: `${javaDir}/internal/service/HandlingService.java`
        },
        {
            from: `${javaTemplateDir}/internal/service/package-info.java`,
            to: `${javaDir}/internal/service/package-info.java`
        }
    ];

    /**
     * Returns an array with the test files for the entire project
     *
     * @type {({from: string, to: string})[]}
     */
    const testFiles = [
        {
            from: `${javaTemplateTestDir}/config/FileUploadPropertiesIT.java`,
            to: `${javaTestDir}/config/FileUploadPropertiesIT.java`
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
        }
    ];

    /**
     * Returns an array with the resource configuration files
     *
     * @type {({from: string, to: string})[]}
     */
    const testExcelFiles = [
        {
            from: `${resourceTestDir}files/currencies.xlsx`,
            to: `${resourceTestDir}files/currencies.xlsx`
        }
    ];

    /**
     * Return application properties for the entire uploads system configuration
     *
     * @type {*[]}
     */
    const applicationPropertiesFiles = [
        {
            from: `${javaTemplateResourceDir}/config/fileUploads.yml`,
            to: `${resourceDir}config/fileUploads.yml`
        }
    ];

    /**
     * General file templates and configurations
     *
     * @type {({from: string, to: string})[]}
     */
    const files = [
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
        }
    ];

    // return files.concat(configurationFiles).concat(batchFiles);
    return files
        .concat(...batchFiles)
        .concat(...excelWorkflowFiles)
        .concat(...fileProcessingWorkflowFiles)
        .concat(...reportFiles)
        .concat(...internalResourceFiles)
        .concat(...serviceFiles)
        .concat(...testFiles)
        .concat(...testExcelFiles)
        .concat(...sampleDataModelFiles)
        .concat(...applicationPropertiesFiles)
        .concat(...fileUploadConfigFiles)
        .concat(...modelFiles);
}

module.exports = {
    getTemplateFiles
};
