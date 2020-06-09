/**
 * Copyright 2020 the Edwin Njeru or authors from the JHipster project.
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
const BaseGenerator = require('generator-jhipster/generators/generator-base');
const { spawn } = require('child_process');

// jdl scripts
const FILE_UPLOADS_JDL = 'fileUploads';
const GENERAL_FILE_UPLOADS_JDL = 'fileUploads-general';
const MICROSERVICE_FILE_UPLOADS_JDL = 'fileUploads-microservice';
const GENERAL_CLIENT_ROOT_FOLDER = 'fileUploads';

/**
 * This class contains functions to run jdl scripts on the client code with child process
 *
 * @type {exports}
 */
module.exports = class extends BaseGenerator {
    /**
     * @param {Boolean} skipClient
     * @param {String} applicationType
     * @param {Boolean} addFieldAndClassPrefix
     */
    executeJdlScript(skipClient, applicationType, addFieldAndClassPrefix) {
        if (applicationType === 'microservice' && addFieldAndClassPrefix) {
            this.runMicroserviceScript(skipClient, `${MICROSERVICE_FILE_UPLOADS_JDL}`);
        }

        if (applicationType === 'microservice' && !addFieldAndClassPrefix) {
            this.runMicroserviceScript(skipClient, `${FILE_UPLOADS_JDL}`);
        }

        if (applicationType !== 'microservice') {
            this.runGeneralScript(skipClient, `${GENERAL_FILE_UPLOADS_JDL}`);
        }
    }

    /**
     *
     * @param {Boolean} skipClient
     * @param {String} jdlScriptFile
     */
    runMicroserviceScript(skipClient, jdlScriptFile) {
        const jdlHasRan = this.async();
        const jdlRan = spawn(
            'jhipster',
            ['import-jdl', `.jhipster/${jdlScriptFile}.jdl`, '--fluent-methods=true ', `--skip-client=${skipClient} `],
            { stdio: 'inherit', shell: true, windowsVerbatimArguments: true, windowsHide: true }
        );
        jdlRan.on('error', error => {
            // Hopeful that this gives informative error messages
            this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
        });
        jdlRan.stdout.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stdout : ${data.message} \n`);
        });
        jdlRan.stderr.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stderr : ${data.message} \n`);
        });
        jdlHasRan();
    }

    /**
     * @deprecated todo replace this method with script-name args
     * @param {Boolean} skipClient
     * @param {String} jdlScriptFile
     */
    runGeneralScript(skipClient, jdlScriptFile) {
        const generalClientRootFolder = GENERAL_CLIENT_ROOT_FOLDER;
        const jdlHasRan = this.async();
        const jdlRan = spawn(
            'jhipster',
            [
                'import-jdl',
                `.jhipster/${jdlScriptFile}.jdl`,
                '--fluent-methods=true ',
                `--skip-client=${skipClient} `,
                `--client-root-folder=${generalClientRootFolder}`
            ],
            { stdio: 'inherit', shell: true, windowsVerbatimArguments: true, windowsHide: true }
        );
        jdlRan.on('error', error => {
            // Hopeful that this gives informative error messages
            this.log(`error: ${error.message} \n See stack-trace : \n ${error.stack}`);
        });
        jdlRan.stdout.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stdout : ${data.message} \n`);
        });
        jdlRan.stderr.on('data', data => {
            // TODO This thing causes an error at the end but it helps synch the workflow
            this.log(`JDl Stderr : ${data.message} \n`);
        });
        jdlHasRan();
    }
};
