/* eslint-disable prettier/prettier */
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
 * This methods enable the module to work on multiple files which are supplied simultaneously as an array
 * of objects with specification of string from and to variables.
 *
 * @type {{unCapitalizeFLetter: (function(String): string), capitalizeFLetter: (function(String): string), copyFiles: copyFiles}}
 */
module.exports = {
    copyFiles,
    capitalizeFLetter,
    unCapitalizeFLetter
};

/**
 *
 * @param {Object} gen generator
 * @param {Array} files
 */
function copyFiles(gen, files) {
    files.forEach(file => {
        try {
            gen.copyTemplate(
                file.from,
                file.to,
                file.type ? file.type : 'template',
                gen,
                file.interpolate ? { interpolate: file.interpolate } : undefined
            );
        } catch (e) {
            throw new Error(`Exception encountered : ${e.message}`);
        }
    });
}

/**
 * @param {String} string the string to capitalze
 * @return {String} string with camel-case
 * @private
 */
function capitalizeFLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

/**
 * @param {String} string the string to capitalze
 * @return {String} string with camel-case
 * @private
 */
function unCapitalizeFLetter(string) {
    return string.charAt(0).toLowerCase() + string.slice(1);
}
