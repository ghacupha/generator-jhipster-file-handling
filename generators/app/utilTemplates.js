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
    const /** {String} */ javaDir = gen.javaDir;
    const /** {String} */ javaTemplateTestDir = gen.javaTemplateTestDir;
    const /** {String} */ javaTestDir = gen.javaTestDir;
    const /** {String} */ resourceDir = gen.resourceDir;
    const /** {String} */ resourceTestDir = gen.resourceTestDir;
    const /** {Boolean} */ usingRabbitMq = gen.messageBrokerType === 'RabbitMQ';

    /**
     * Returns an array with the spring-boot configuration files
     *
     * @type {({from: string, to: string})[]}
     */
    const rabbitMQConfigFiles = [
        {
            from: `${javaTemplateDir}/config/_CloudMessagingConfiguration.java`,
            to: `${javaDir}/config/CloudMessagingConfiguration.java`
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
     * Returns an array with the messaging workflow template files
     *
     * @type {({from: string, to: string})[]}
     */
    const messagingWorkflowFiles = [
        {
            from: `${javaTemplateDir}/internal/messaging/DeleteMessageDTO.java`,
            to: `${javaDir}/internal/messaging/DeleteMessageDTO.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/MessageServiceContainer.java`,
            to: `${javaDir}/internal/messaging/MessageServiceContainer.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/StringTokenValueSearch.java`,
            to: `${javaDir}/internal/messaging/StringTokenValueSearch.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/TokenValueSearch.java`,
            to: `${javaDir}/internal/messaging/TokenValueSearch.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotification.java`,
            to: `${javaDir}/internal/messaging/fileNotification/FileNotification.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotificationListener.java`,
            to: `${javaDir}/internal/messaging/fileNotification/FileNotificationListener.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotificationStreams.java`,
            to: `${javaDir}/internal/messaging/fileNotification/FileNotificationStreams.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/FileNotificationStreamsConfig.java`,
            to: `${javaDir}/internal/messaging/fileNotification/FileNotificationStreamsConfig.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/BatchSupportedFileUploadProcessor.java`,
            to: `${javaDir}/internal/messaging/fileNotification/processors/BatchSupportedFileUploadProcessor.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/FileUploadProcessor.java`,
            to: `${javaDir}/internal/messaging/fileNotification/processors/FileUploadProcessor.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/FileUploadProcessorChain.java`,
            to: `${javaDir}/internal/messaging/fileNotification/processors/FileUploadProcessorChain.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/FileUploadProcessorContainer.java`,
            to: `${javaDir}/internal/messaging/fileNotification/processors/FileUploadProcessorContainer.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/StreamSupportedFileUploadProcessor.java`,
            to: `${javaDir}/internal/messaging/fileNotification/processors/StreamSupportedFileUploadProcessor.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/fileNotification/processors/package-info.java`,
            to: `${javaDir}/internal/messaging/fileNotification/processors/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/jsonStrings/CurrencyTableJsonStringStreamsListener.java`,
            to: `${javaDir}/internal/messaging/jsonStrings/CurrencyTableJsonStringStreamsListener.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/jsonStrings/GsonUtils.java`,
            to: `${javaDir}/internal/messaging/jsonStrings/GsonUtils.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/jsonStrings/JsonStringStreams.java`,
            to: `${javaDir}/internal/messaging/jsonStrings/JsonStringStreams.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/jsonStrings/JsonStringStreamsConfig.java`,
            to: `${javaDir}/internal/messaging/jsonStrings/JsonStringStreamsConfig.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/jsonStrings/StringMessageDTO.java`,
            to: `${javaDir}/internal/messaging/jsonStrings/StringMessageDTO.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/MessageService.java`,
            to: `${javaDir}/internal/messaging/platform/MessageService.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/MessageStreams.java`,
            to: `${javaDir}/internal/messaging/platform/MessageStreams.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/MuteListener.java`,
            to: `${javaDir}/internal/messaging/platform/MuteListener.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/ResponsiveListener.java`,
            to: `${javaDir}/internal/messaging/platform/ResponsiveListener.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/StringTokenMessageService.java`,
            to: `${javaDir}/internal/messaging/platform/StringTokenMessageService.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/Tokenizable.java`,
            to: `${javaDir}/internal/messaging/platform/Tokenizable.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/platform/TokenizableMessage.java`,
            to: `${javaDir}/internal/messaging/platform/TokenizableMessage.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/sample/Greetings.java`,
            to: `${javaDir}/internal/messaging/sample/Greetings.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/sample/GreetingsContainer.java`,
            to: `${javaDir}/internal/messaging/sample/GreetingsContainer.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/sample/GreetingsListener.java`,
            to: `${javaDir}/internal/messaging/sample/GreetingsListener.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/sample/GreetingsStreams.java`,
            to: `${javaDir}/internal/messaging/sample/GreetingsStreams.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/sample/GreetingsStreamsConfig.java`,
            to: `${javaDir}/internal/messaging/sample/GreetingsStreamsConfig.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/DeleteMessageDTO.java`,
            to: `${javaDir}/internal/messaging/DeleteMessageDTO.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/MessageServiceContainer.java`,
            to: `${javaDir}/internal/messaging/MessageServiceContainer.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/StringTokenValueSearch.java`,
            to: `${javaDir}/internal/messaging/StringTokenValueSearch.java`
        },
        {
            from: `${javaTemplateDir}/internal/messaging/TokenValueSearch.java`,
            to: `${javaDir}/internal/messaging/TokenValueSearch.java`
        }
    ];

    /**
     * Returns an array with the sample data for currency-table entity
     *
     * @type {({from: string, to: string})[]}
     */
    const sampleDataModelFiles = [
        {
            from: `${javaTemplateDir}/internal/model/package-info.java`,
            to: `${javaDir}/internal/model/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/sampleDataModel/package-info.java`,
            to: `${javaDir}/internal/model/sampleDataModel/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/model/sampleDataModel/CurrencyTableBatchService.java`,
            to: `${javaDir}/internal/model/sampleDataModel/CurrencyTableBatchService.java`
        },
        //     from: `${javaTemplateDir}/internal/model/sampleDataModel/CurrencyTableDTO.java`,
        //     to: `${javaDir}/internal/model/sampleDataModel/CurrencyTableDTO.java`
        // },
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
        }
    ];

    /**
     * Returns an array with the test files for the entire project
     *
     * @type {({from: string, to: string})[]}
     */
    const testFiles = [
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
        },
        {
            from: `${javaTemplateTestDir}/internal/messaging/FileNotificationControllerIT.java`,
            to: `${javaTestDir}/internal/messaging/FileNotificationControllerIT.java`
        },
        {
            from: `${javaTemplateTestDir}/internal/messaging/GreetingsControllerIT.java`,
            to: `${javaTestDir}/internal/messaging/GreetingsControllerIT.java`
        },
        {
            from: `${javaTemplateTestDir}/internal/messaging/GsonUtilsTest.java`,
            to: `${javaTestDir}/internal/messaging/GsonUtilsTest.java`
        },
        {
            from: `${javaTemplateTestDir}/internal/messaging/JsonStringsControllerIT.java`,
            to: `${javaTestDir}/internal/messaging/JsonStringsControllerIT.java`
        },
        {
            from: `${javaTemplateTestDir}/internal/messaging/TokenGeneratorTest.java`,
            to: `${javaTestDir}/internal/messaging/TokenGeneratorTest.java`
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
        // {
        //     from: `${resourceDir}config/liquibase/fake-data/currency_table.csv`,
        //     to: `${resourceDir}config/liquibase/fake-data/currency_table.csv`
        // },
    ];

    /**
     * Return application properties for the entire uploads system configuration
     *
     * @type {*[]}
     */
    const applicationPropertiesFiles = [];

    /**
     * This properties apply when we are using kafka
     *
     * @type {{from: string, to: string}[]}
     * @private
     */
    const _kafkaSpecApplicationProperties = [
        {
            from: `${resourceDir}config/application-uploads.yml`,
            to: `${resourceDir}config/application-uploads.yml`
        }
    ];

    /**
     * These properties apply when we are using RabbitMq
     *
     * @type {{from: string, to: string}[]}
     * @private
     */
    const _rabbitMqSpecApplicationProperties = [
        {
            from: `${resourceDir}config/application-uploads-rabbitMq.yml`,
            to: `${resourceDir}config/application-uploads.yml`
        }
    ];

    // select appropriate application-properties
    applicationPropertiesFiles.concat(...(usingRabbitMq ? _rabbitMqSpecApplicationProperties : _kafkaSpecApplicationProperties));

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
        .concat(...(usingRabbitMq ? rabbitMQConfigFiles : []))
        .concat(...batchFiles)
        .concat(...excelWorkflowFiles)
        .concat(...messagingWorkflowFiles)
        .concat(...reportFiles)
        .concat(...internalResourceFiles)
        .concat(...serviceFiles)
        .concat(...testFiles)
        .concat(...testExcelFiles)
        .concat(...(usingRabbitMq ? _rabbitMqSpecApplicationProperties : _kafkaSpecApplicationProperties))
        .concat(...sampleDataModelFiles);
}

module.exports = {
    getTemplateFiles
};
