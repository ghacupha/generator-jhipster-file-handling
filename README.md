# generator-jhipster-file-handling

[![NPM version][npm-image]][npm-url] [![Build Status][github-actions-image]][github-actions-url] [![Dependency Status][daviddm-image]][daviddm-url]

> JHipster module, Installs entities and dependencies to enable reading from excel file data

# Introduction

This is a [JHipster](https://www.jhipster.tech/) module, that is meant to be used in a JHipster application.

# Caution

This tool is still in active development and has not attained MVP. Lots of changes are happening to the api

# Prerequisites

As this is a [JHipster](https://www.jhipster.tech/) module, we expect you have JHipster and its related tools already installed:

- [Installing JHipster](https://www.jhipster.tech/installation/)

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

Once installed, there will be problems with jpa/entity scanning. This is because the database configuration is usually configured like so :

    @Configuration
    @EnableJpaRepositories("packageName.repository")
    @EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
    @EnableTransactionManagement
    @EnableElasticsearchRepositories("packageName.repository.search")
    public class DatabaseConfiguration {

        private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
    }

But this rules out the currency-table entity which is created as example in the internal package. If you remove the repositories declaration, things will be fine.
This will then look like :

    @Configuration
    @EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
    @EnableTransactionManagement
    public class DatabaseConfiguration {

        private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
    }

# Usage

# License

[Apache-2.0](https://github.com/ghacupha/generator-jhipster-file-handling) © [Edwin Njeru](https://github.com/ghacupha)

[npm-image]: https://img.shields.io/npm/v/generator-jhipster-file-handling.svg
[npm-url]: https://npmjs.org/package/generator-jhipster-file-handling
[github-actions-image]: https://github.com/ghacupha/generator-jhipster-file-handling/workflows/Build/badge.svg
[github-actions-url]: https://github.com/ghacupha/generator-jhipster-file-handling/actions
[daviddm-image]: https://david-dm.org/ghacupha/generator-jhipster-file-handling.svg?theme=shields.io
[daviddm-url]: https://david-dm.org/ghacupha/generator-jhipster-file-handling
