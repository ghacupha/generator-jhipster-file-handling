/**
 * Copyright 2013-2020 the Edwin Njeru, Pascal Grimaud and the respective JHipster contributors.
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

const utilYaml = require('./utilYaml.js');

module.exports = {
    updateAppProperties
};

/**
 * Updates properties in the application-properties
 * @param generator
 * @param resourceDir
 */
function updateAppProperties(generator, resourceDir) {
    // application-dev.yml
    const yamlAppDevProperties = {};
    utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.default.contentType', generator, 'application/json');
    utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.bindings.input.destination', generator, 'topic-jhipster');
    utilYaml.updatePropertyInArray(yamlAppDevProperties, 'spring.cloud.stream.bindings.output.destination', generator, 'topic-jhipster');
    utilYaml.updatePropertyInArray(
        yamlAppDevProperties,
        'spring.cloud.stream.bindings.rabbit.bindings.output.producer.routingKeyExpression',
        generator,
        'headers.title'
    );
    utilYaml.updateYamlProperties(`${resourceDir}config/application-dev.yml`, yamlAppDevProperties, generator);

    // application-prod.yml
    const yamlAppProdProperties = yamlAppDevProperties;
    utilYaml.updatePropertyInArray(
        yamlAppProdProperties,
        'spring.cloud.stream.bindings.rabbit.bindings.output.producer.routingKeyExpression',
        generator,
        'payload.title'
    );
    utilYaml.updateYamlProperties(`${resourceDir}config/application-prod.yml`, yamlAppProdProperties, generator);
}
