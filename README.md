# eo-yaml
<img alt="eo-yaml-logo" src="http://www.amihaiemil.com/images/camila2.png" width="120" height="70"/>

[![Build Status](https://travis-ci.org/decorators-squad/eo-yaml.svg?branch=master)](https://travis-ci.org/decorators-squad/eo-yaml)
[![Coverage Status](https://coveralls.io/repos/github/decorators-squad/eo-yaml/badge.svg?branch=master)](https://coveralls.io/github/decorators-squad/eo-yaml?branch=master)
[![PDD status](http://www.0pdd.com/svg?name=decorators-squad/eo-yaml)](http://www.0pdd.com/p?name=decorators-squad/eo-yaml)

[![DevOps By Rultor.com](http://www.rultor.com/b/decorators-squad/eo-yaml)](http://www.rultor.com/p/decorators-squad/eo-yaml)
[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)
[![OpenJDK Quality Outreach](https://amihaiemil.com/images/openjdk_quality_outreach.svg)](https://wiki.openjdk.java.net/display/quality/Quality+Outreach)

YAML for Java 8 and above. Based on [spec 1.2](http://www.yaml.org/spec/1.2/spec.html).

From the [specification](http://yaml.org/spec/1.2/spec.html): **YAML™** is a human-friendly, cross language, Unicode based data serialization language.

To get the latest release from Maven Central, simply add the following to your ``pom.xml``: 

```xml
<dependency>
    <groupId>com.amihaiemil.web</groupId>
    <artifactId>eo-yaml</artifactId>
    <version>5.1.6</version>
</dependency>
```

or download the <a href="https://oss.sonatype.org/service/local/repositories/releases/content/com/amihaiemil/web/eo-yaml/5.1.6/eo-yaml-5.1.6-jar-with-dependencies.jar">fat</a> jar.

The releases are also available on [Github Packages](https://github.com/decorators-squad/eo-yaml/packages)!

## Usage

The API of this library is clean, intuitive and generally close to the ``javax.json`` API that most developers are used to.
Just start form the ``com.amihaiemil.eoyaml.Yaml`` class, it offers all the builders and readers you may need.

See the [Block Style Yaml](https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML) wiki for a first glance.

## Features detailed (ongoing work!)

Here is what we have so far:

* Building and Reading Block YAML ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML));
* Support for Folded and Literal Block Scalars ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Folded-and-Literal-Block-Scalars));
* Convenience Type-Casting Methods ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Convenience-Type-Casting-Methods));
* Editing of YAML ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Easy-Extension-Via-Interfaces#edit-yaml));
* Support for Comments ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Support-For-Comments));
* Convenient ``YamlPrinter`` ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/YAML-Printer));
* Easy Extension Thanks to Interfaces ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Easy-Extension-Via-Interfaces));
* Building and Reading YAML Streams, integrated with Java 8's Stream API ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/YAML-Streams));
* Java Beans to YAML ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Java-Bean-To-YAML));
* Others:
    - Automatic validation of the input's indentation ([wiki](https://github.com/decorators-squad/eo-yaml/wiki/Validation-of-Indentation)).
    - Clear and detailed Exceptions. For instance, in the case of bad indentation, it will tell you exactly which line is problematic and why.
    - Fully encapsulated. The user works only with a few Java Interfaces.
    - All objects are immutable and thread-safe.
    - It can be used as a **Java Module** (if you're on JDK 9+).
    - It is **lightweight**! It has 0 dependencies.

Also, you can have a look under [src/test/resources](https://github.com/decorators-squad/eo-yaml/tree/master/src/test/resources) to see the kinds of YAML that the library can read and handle so far.

Here is what we're **still missing and working on**:

* Flow and Recursive representation
* Aliases and anchors
* YAML to Java Bean

**Keep in mind** that the library is based on interfaces and OOP best practices, so you can probably extend/decorate the objects in order to create the functionality you need, if it's not yet implemented. 

If you have some time and like the library, please consider contributing. 

## Contribute

Contributors are welcome!

1. Open an issue regarding an improvement you thought of, or a bug you noticed, or ask to be assigned to an existing one.
2. If the issue is confirmed, fork the repository, do the changes on a separate branch and make a Pull Request.
3. After review and acceptance, the PR is merged and closed.

Make sure the maven build

``$ mvn clean install -Pcheckstyle,itcases``

**passes before making a PR**. 
