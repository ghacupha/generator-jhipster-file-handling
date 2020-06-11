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
 * @param javaTemplateDir
 * @param javaDir
 * @param javaTemplateTestDir
 * @param javaTestDir
 * @returns {({from: string, to: string})[]}
 */
function getTemplateFiles(javaTemplateDir, javaDir, javaTemplateTestDir, javaTestDir) {
    const files = [
        {
            from: `${javaTemplateDir}/config/_CloudMessagingConfiguration.java`,
            to: `${javaDir}/config/CloudMessagingConfiguration.java`
        },
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
        },
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
            from: `${javaTemplateDir}/internal/messaging/jsonStrings/JsonStringStreamsListener.java`,
            to: `${javaDir}/internal/messaging/jsonStrings/JsonStringStreamsListener.java`
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
        },
        {
            from: `${javaTemplateDir}/internal/model/package-info.java`,
            to: `${javaDir}/internal/model/package-info.java`
        },
        {
            from: `${javaTemplateDir}/internal/report/QueryTools.java`,
            to: `${javaDir}/internal/report/QueryTools.java`
        },
        {
            from: `${javaTemplateDir}/internal/report/Report.java`,
            to: `${javaDir}/internal/report/Report.java`
        },
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
        },
        {
            from: `${javaTemplateDir}/internal/service/BatchService.java`,
            to: `${javaDir}/internal/service/package-info.java`
        },
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
    return files;
}

module.exports = {
    getTemplateFiles
};
