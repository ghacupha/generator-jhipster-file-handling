# generator-jhipster-file-handling

[![NPM version][npm-image]][npm-url] [![Build Status][github-actions-image]][github-actions-url] [![Dependency Status][daviddm-image]][daviddm-url]

> JHipster module, Installs entities and dependencies to enable reading list data from excel files

# Introduction

This is a [JHipster](https://www.jhipster.tech/) module, that is meant to be used in a JHipster application.

Using this tool you can install into a project asynchronous reads on data that is in an Excel file using the spring batch tool.
The project uses [poiji](https://github.com/ozlerhakan/poiji), which is an [Apache POI](https://poi.apache.org/) abstraction that converts excel rows to a list of java objects.

Here we are converting data listed in rows of an Excel file into a list of objects which then we populate into respective data models.
We assume that the user needs to update some entity of known fields and field types.

So during set up, one has to specify the type of data models, whose data they would read from an Excel file. These are treated as file-type enumerations to distinguish data models
from each other.

During runtime, the user will need to create a file type, upload a template of the file containing appropriate columns and specify the enumeration to be associated with a given file-type.

Having done that, the user uploading the Excel file (xlsx currently supported) will input the file-type id when uploading the Excel file.

From there the file-handling is processed by an asynchronous process so soon after the file is persisted by the corresponding service,
the user will see a response from the API, which does not mean the file is already deserialized, but nevertheless indicates that the file has been uploaded.
The batch process is triggered by the file-notification, and completed asynchronously and only the message token and batch id are indicative of documents processing completion.
For these things to work there are a number of interfaces to work and class implementations to complete.

### Interfaces

This class uses interface to interact with the batch framework and also to configure the Excel file reader that must be configured by the user.
There are also data objects (excel-view-models) which correlate the columns in an Excel file with the fields of a java bean. These further will need mapping interfaces
correlate them with the DTO.

Ultimately there is a batch-service that implements an update in the table of a relevant model when the ItemWriter needs to persist some data list.

# Caution

This tool is still in active development and has not attained MVP. Lots of changes are happening to the API

# Prerequisites

As this is a [JHipster](https://www.jhipster.tech/) module, we expect you have installed or have a working knowledge of JHipster, Spring, Spring batch
and their related tools as well as POI:

- [Installing Yeoman Generator](https://yeoman.io/generators/)
- [Installing JHipster](https://www.jhipster.tech/installation/)
- [POI](https://github.com/apache/poi)
- [poiji](https://github.com/ozlerhakan/poiji)

# Installation

## With NPM

To install this module:

```bash
npm install -g generator-jhipster-file-handling
```

To update this module:

```bash
npm update -g generator-jhipster-file-handling
```

## With Yarn

To install this module:

```bash
yarn global add generator-jhipster-file-handling
```

To update this module:

```bash
yarn global upgrade generator-jhipster-file-handling
```

## Configuration

Once installed, there will be issues with configuring the list-size parameters for the list partition algorithm used
this is because the FileUploadsProperties may need to be added on the EnableConfigurationProperties declaration like so:

            @SpringBootApplication
            @EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class, FileUploadsProperties.class})
            public class MyJhipsterApp {

                // Application startup logic ...
            }

# Usage

Initiate this module by the yeoman command:

    yo jhipster-file-handling

Input the following prompts :

- folder name (also used as prefix) for file-handling entities on the front end
- TRUE/FALSE, whether to prefix the entities with the the previous folder name
- File model types: Input comma separated names of entities, in capital case, with no spaces between them.

Then press return to proceed.

Formerly entered file-model-types become the enumerations for different entity types for data uploaded from the Excel file.

# License

[Apache-2.0](https://github.com/ghacupha/generator-jhipster-file-handling) © [Edwin Njeru](https://github.com/ghacupha)

[npm-image]: https://img.shields.io/npm/v/generator-jhipster-file-handling.svg
[npm-url]: https://npmjs.org/package/generator-jhipster-file-handling
[github-actions-image]: https://github.com/ghacupha/generator-jhipster-file-handling/workflows/Build/badge.svg
[github-actions-url]: https://github.com/ghacupha/generator-jhipster-file-handling/actions
[daviddm-image]: https://david-dm.org/ghacupha/generator-jhipster-file-handling.svg?theme=shields.io
[daviddm-url]: https://david-dm.org/ghacupha/generator-jhipster-file-handling
