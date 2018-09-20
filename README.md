# eo-yaml
<img alt="eo-yaml-logo" src="http://www.amihaiemil.com/images/camila2.png" width="120" height="70"/>

[![PDD status](http://www.0pdd.com/svg?name=decorators-squad/eo-yaml)](http://www.0pdd.com/p?name=decorators-squad/eo-yaml)
[![Build Status](https://travis-ci.org/decorators-squad/eo-yaml.svg?branch=master)](https://travis-ci.org/decorators-squad/eo-yaml)
[![Coverage Status](https://coveralls.io/repos/github/decorators-squad/eo-yaml/badge.svg?branch=master)](https://coveralls.io/github/decorators-squad/eo-yaml?branch=master)

[![DevOps By Rultor.com](http://www.rultor.com/b/decorators-squad/eo-yaml)](http://www.rultor.com/p/decorators-squad/eo-yaml)
[![We recommend IntelliJ IDEA](http://amihaiemil.github.io/images/intellij-idea-recommend.svg)](https://www.jetbrains.com/idea/)

YAML for Java. A [user-friendly](http://www.baeldung.com/design-a-user-friendly-java-library) OOP library. Based on [spec 1.2](http://www.yaml.org/spec/1.2/spec.html).

From the [specification](http://yaml.org/spec/1.2/spec.html): **YAML™** is a human-friendly, cross language, Unicode based data serialization language.

To get the latest release, simply add the following to your ``pom.xml``: 

```
<dependency>
    <groupId>com.amihaiemil.web</groupId>
    <artifactId>eo-yaml</artifactId>
    <version>2.0.1</version>
</dependency>
```

or download the <a href="https://oss.sonatype.org/service/local/repositories/releases/content/com/amihaiemil/web/eo-yaml/2.0.1/eo-yaml-2.0.1-jar-with-dependencies.jar">fat</a> jar.


## Usage

The API of this library is clean, intuitive and generally close to the ``javax.json`` API that most developers are used to:

## Features

Since the library is quite young, it doesn't support all the features of YAML yet. For now, it only supports creating/parsing [Block Style](http://yaml.org/spec/1.2/spec.html#Block) YAML, so use it if you have to read/create configuration files and such stuff. It doesn't yet implement features such as **flow** or **recursive** representation, **aliases & anchors** or document streams (more YAMLs separated by ``---``).

However, keep in mind that the library is based on interfaces and OOP best practices, so you can probably extend/decorate the objects in order to create the functionality you need! 

If you have some time and like the library, please consider contributing. 

## Building and printing Yaml:

```java
YamlMapping yaml = Yaml.createYamlMappingBuilder()
    .add("architect", "amihaiemil")
    .add(
        "devops",
        Yaml.createYamlSequenceBuilder()
            .add("rultor")
            .add("0pdd")
            .build()
    ).add(
        "developers",
        Yaml.createYamlSequenceBuilder()
            .add("amihaiemil")
            .add("salikjan")
            .add("SherifWally")
            .build()
    ).build();
```

``toString()`` methods are overriden to pretty-print the yaml, so the above ``yaml.toString()`` will print:

```yaml
architect: amihaiemil
developers: 
  - amihaiemil
  - salikjan
  - SherifWally
devops: 
  - rultor
  - 0pdd
```

## Reading:

Reading a Yaml input is very straight-forward, as outlined bellow. There is one **important** aspect: the input has to be
a valid (well-indented) Yaml, otherwise you will get an exception, at some point, when trying to work with the read object!

```java
//createYamlInput is overloaded to accept also String InputStream
YamlMapping yamlMapping = Yaml.createYamlInput(new File("mapping.yml"))
    .readYamlMapping();

YamlSequence yamlSequence = Yaml.createYamlInput(new File("sequence.yml"))
    .readYamlSequence();
```

## Parsing a Pojo:

Pojos can be parsed ("dumped") into Yaml as follows (attributes need to have getters and setters): 

```java
Map<String, Integer> grades = new HashMap<>();
grades.put("Math", 9);
grades.put("CS", 10);
YamlMapping studentYaml = new YamlObjectDump(
    new Student ("John", "Doe", 20, grades)
).represent();
```

``studentYaml.toString()`` will print:

```yaml
age: 20
firstName: John
grades: 
  CS: 10
  Math: 9
lastName: Doe
```

You can also parse maps (``Map<Object, Object>``) and collections (``Collection<Object>``) using ``YamlMapDump`` and ``YamlCollectionDump`` respecitvely

## Contribute

Contributors are [welcome](http://www.amihaiemil.com/2016/12/30/becoming-a-contributor.html)

1. Open an issue regarding an improvement you thought of, or a bug you noticed, or ask to be assigned to an existing one.
2. If the issue is confirmed, fork the repository, do the changes on a separate branch and make a Pull Request.
3. After review and acceptance, the PR is merged and closed.
4. You are automatically listed as a contributor on the repo and the project's site (to follow)

Make sure the maven build

``$ mvn clean install -Pcheckstyle``

**passes before making a PR**. 
