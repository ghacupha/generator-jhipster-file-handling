/* eslint-disable prettier/prettier */
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
        gen.copyTemplate(
            file.from,
            file.to,
            file.type ? file.type : 'template',
            gen,
            file.interpolate ? { interpolate: file.interpolate } : undefined
        );
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
