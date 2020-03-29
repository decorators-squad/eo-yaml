# eo-yaml
<img alt="eo-yaml-logo" src="http://www.amihaiemil.com/images/camila2.png" width="120" height="70"/>

[![PDD status](http://www.0pdd.com/svg?name=decorators-squad/eo-yaml)](http://www.0pdd.com/p?name=decorators-squad/eo-yaml)
[![Build Status](https://travis-ci.org/decorators-squad/eo-yaml.svg?branch=master)](https://travis-ci.org/decorators-squad/eo-yaml)
[![Coverage Status](https://coveralls.io/repos/github/decorators-squad/eo-yaml/badge.svg?branch=master)](https://coveralls.io/github/decorators-squad/eo-yaml?branch=master)

[![DevOps By Rultor.com](http://www.rultor.com/b/decorators-squad/eo-yaml)](http://www.rultor.com/p/decorators-squad/eo-yaml)
[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)

YAML for Java. A [user-friendly](http://www.baeldung.com/design-a-user-friendly-java-library) OOP library. Based on [spec 1.2](http://www.yaml.org/spec/1.2/spec.html).

From the [specification](http://yaml.org/spec/1.2/spec.html): **YAML™** is a human-friendly, cross language, Unicode based data serialization language.

To get the latest release from Maven Central, simply add the following to your ``pom.xml`` (it's always the latest version): 

```xml
<dependency>
    <groupId>com.amihaiemil.web</groupId>
    <artifactId>eo-yaml</artifactId>
    <version>4.0.1</version>
</dependency>
```

or download the <a href="https://oss.sonatype.org/service/local/repositories/releases/content/com/amihaiemil/web/eo-yaml/4.0.1/eo-yaml-4.0.1-jar-with-dependencies.jar">fat</a> jar.

The releases are also available on [Github Packages](https://github.com/decorators-squad/eo-yaml/packages)!

## Usage

The API of this library is clean, intuitive and generally close to the ``javax.json`` API that most developers are used to.
Just start form the ``com.amihaiemil.eoyaml.Yaml`` class, it offers all the builders and readers you may need.

## Features (ongoing work!)

Since the library is quite young, it doesn't support all the features of YAML yet.

Here is what we have so far:

* Building and Reading Block YAML ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML));
* Building and Reading YAML Streams, integrated with Java 8's Stream API ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/YAML-Streams));
* Java Beans to YAML ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Java-Bean-To-YAML));
* Others:
    - Automatic validation of the input's indentation.
    - Clear and detailed Exceptions. For instance, in the case of bad indentation, it will tell you exactly which line is problematic and why.

Also, you can have a look a look under [src/test/resources](https://github.com/decorators-squad/eo-yaml/tree/master/src/test/resources) to see the kinds of YAML that the library can read and handle so far.

Here is what we're still missing and working on:

* Flow and Recursive representation
* Aliases and anchors

**Keep in mind** that the library is based on interfaces and OOP best practices, so you can probably extend/decorate the objects in order to create the functionality you need, if it's not yet implemented. 

If you have some time and like the library, please consider contributing. 

## Contribute

Contributors are welcome!

1. Open an issue regarding an improvement you thought of, or a bug you noticed, or ask to be assigned to an existing one.
2. If the issue is confirmed, fork the repository, do the changes on a separate branch and make a Pull Request.
3. After review and acceptance, the PR is merged and closed.

Make sure the maven build

``$ mvn clean install -Pcheckstyle``

**passes before making a PR**. 
