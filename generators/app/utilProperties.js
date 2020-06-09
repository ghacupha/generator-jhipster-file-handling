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
