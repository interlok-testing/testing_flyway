# Flyway Testing

[![license](https://img.shields.io/github/license/interlok-testing/testing_flyway.svg)](https://github.com/interlok-testing/testing_flyway/blob/develop/LICENSE)
[![Actions Status](https://github.com/interlok-testing/testing_flyway/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/interlok-testing/testing_flyway/actions/workflows/gradle-build.yml)

Project tests interlok-flyway features

## What it does

This project is very simple and contains a share flyway connection and one channel with one workflow.

The shared flyway connection just start the flyway migration if needed. Meaning that the first time the adapter will start and connect to the database it will create a table **account** and add an entry.
The next times the adapter will start, flyway will detected that the init script has already been executed and will ignore it.

The workflow has a polling trigger that trigger a database query (*select \* from account*) every 20 seconds, put the response into the message and log the message into the console.

## Getting started

Before starting Interlok you need to create a MariaDB docker container with

* `docker-compose up`

Then start Interlok

* `./gradlew clean build`
* `(cd ./build/distribution && java -jar lib/interlok-boot.jar)`

The config is using a variables.properties to configure the MariaDB properties.

```
mariadbUsername=root
mariadbPassword=AES_GCM:ViuVzQL6VDLAydkGq9eyQxvsS2NS/T1+pa62pKsJ9EIKEvuFjQfK87l55htMsP3yEdcgqA==
mariadbHost=localhost
mariadbPort=3306
mariadbUrl=jdbc:mariadb://${mariadbHost}:${mariadbPort}/accounts
mariadbDriver=org.mariadb.jdbc.Driver
```

## Notes

- Interlok UI is not enabled in this build.
- **build.gradle** need to include dependencies on `org.mariadb.jdbc:mariadb-java-client:3.2.0` as we are using MariaDB but also, since vesion 8+ Flyway, on `org.flywaydb:flyway-mysql:8.5.13` to work with MySQL and MariaDB.
The same would apply to Microsoft SQLServer if this was used.

## Also

The docker-compose file also creat a animer container so you can easily check the content of MariaDB.

Just go to http://localhost:8085/ and enter the MariaDB credenials.
