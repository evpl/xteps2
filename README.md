# Xteps2

[![Maven Central](https://img.shields.io/badge/maven--central-1.0-brightgreen?style=flat)](https://central.sonatype.com/namespace/com.plugatar.xteps2)
[![Javadoc](https://img.shields.io/badge/javadoc-1.0-blue?style=flat)](https://javadoc.io/doc/com.plugatar.xteps2)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/evpl/xteps2/tests.yml)

## How to use

Requires Java 8+ version or Kotlin JVM. Just add suitable dependency.

| Module                 | Maven central                                                                                                                                                                  | Javadoc                                                                                                                                     |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| `xteps2`               | [![Maven Central](https://img.shields.io/badge/-maven--central-brightgreen?style=flat-square)](https://central.sonatype.com/artifact/com.plugatar.xteps2/xteps2)               | [![Javadoc](https://img.shields.io/badge/-javadoc-blue?style=flat-square)](https://javadoc.io/doc/com.plugatar.xteps2/xteps2)               |
| `xteps2-allure`        | [![Maven Central](https://img.shields.io/badge/-maven--central-brightgreen?style=flat-square)](https://central.sonatype.com/artifact/com.plugatar.xteps2/xteps2-allure)        | [![Javadoc](https://img.shields.io/badge/-javadoc-blue?style=flat-square)](https://javadoc.io/doc/com.plugatar.xteps2/xteps2-allure)        |
| `xteps2-extentreports` | [![Maven Central](https://img.shields.io/badge/-maven--central-brightgreen?style=flat-square)](https://central.sonatype.com/artifact/com.plugatar.xteps2/xteps2-extentreports) | [![Javadoc](https://img.shields.io/badge/-javadoc-blue?style=flat-square)](https://javadoc.io/doc/com.plugatar.xteps2/xteps2-extentreports) |
| `xteps2-qase`          | [![Maven Central](https://img.shields.io/badge/-maven--central-brightgreen?style=flat-square)](https://central.sonatype.com/artifact/com.plugatar.xteps2/xteps2-qase)          | [![Javadoc](https://img.shields.io/badge/-javadoc-blue?style=flat-square)](https://javadoc.io/doc/com.plugatar.xteps2/xteps2-qase)          |
| `xteps2-reportportal`  | [![Maven Central](https://img.shields.io/badge/-maven--central-brightgreen?style=flat-square)](https://central.sonatype.com/artifact/com.plugatar.xteps2/xteps2-reportportal)  | [![Javadoc](https://img.shields.io/badge/-javadoc-blue?style=flat-square)](https://javadoc.io/doc/com.plugatar.xteps2/xteps2-reportportal)  |
| `xteps2-testit`        | [![Maven Central](https://img.shields.io/badge/-maven--central-brightgreen?style=flat-square)](https://central.sonatype.com/artifact/com.plugatar.xteps2/xteps2-testit)        | [![Javadoc](https://img.shields.io/badge/-javadoc-blue?style=flat-square)](https://javadoc.io/doc/com.plugatar.xteps2/xteps2-testit)        |

Maven:

```xml
<dependency>
  <groupId>com.plugatar.xteps2</groupId>
  <artifactId>{artifact name from table}</artifactId>
  <version>1.0</version>
  <scope>test</scope>
</dependency>
```

Gradle:

```groovy
dependencies {
    testImplementation 'com.plugatar.xteps2:{artifact name from table}:1.0'
}
```

Kotlin DSL:

```groovy
dependencies {
    testImplementation("com.plugatar.xteps2:{artifact name from table}:1.0")
}
```

## Configuration

There are two ways to load parameters. Be aware that higher source override lower one - properties from file can be
overridden by system properties.

| Priority | Source                               |
|----------|--------------------------------------|
| 1        | System properties                    |
| 2        | Properties file (`xteps.properties`) |

### Properties list

| Name                                           | Type    | Required | Default value | Description                                                                                                                                                                                            |
|------------------------------------------------|---------|----------|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| xteps.reporter.enabled                         | Boolean | No       | `true`        | Enable/disable steps logging.                                                                                                                                                                          |
| xteps.listener.autodetection                   | Boolean | No       | `true`        | Enable/disable Service Provider Interface mechanism to detect and instantiate `com.plugatar.xteps2.core.StepListener` implementations. Implementations should have zero-argument public constructor.   |
| xteps.listener.list                            | String  | No       |               | List of `com.plugatar.xteps2.core.StepListener` implementations names in `Class#getTypeName()` format. Names should be separated by `,`. Implementations should have zero-argument public constructor. |
| xteps.exceptionHandler.cleanStackTrace.enabled | Boolean | No       | `true`        | Removes all stack trace lines about Xteps from any exception except `XtepsException`.                                                                                                                  |
| xteps.textFormatter.enabled                    | Boolean | No       | `true`        | Enable/disable text artifacts (name and description) formatting.                                                                                                                                       |
| xteps.textFormatter.replacementPattern         | String  | No       | `{([^}]*)}`   | Replacement pattern for text formatter.                                                                                                                                                                |
| xteps.textFormatter.field.forceAccess.enabled  | Boolean | No       | `true`        | Allow forced retrieval of field values via reflection.                                                                                                                                                 |
| xteps.textFormatter.method.forceAccess.enabled | Boolean | No       | `true`        | Allow forced retrieval of method result values via reflection.                                                                                                                                         |
