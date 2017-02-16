<img alt="camel" src="http://www.amihaiemil.com/images/camila2.png" width="120" height="70"/>
# camel

[![Build Status](https://travis-ci.org/decorators-squad/camel.svg?branch=master)](https://travis-ci.org/decorators-squad/camel)
[![Coverage Status](https://coveralls.io/repos/github/decorators-squad/camel/badge.svg?branch=master)](https://coveralls.io/github/decorators-squad/camel?branch=master)

YAML for Java. A [user-friendly](http://www.baeldung.com/design-a-user-friendly-java-library) OOP library.

From the [specification](http://yaml.org/spec/1.2/spec.html): **YAML™** (rhymes with “camel”) is a human-friendly, cross language, Unicode based data serialization language.

**First release is comming soon!**

## Usage

The API of this library is clean, intuitive and generally close to the ``javax.json`` API that most developers are used to:

## Building and printing Yaml:

```java
YamlMapping yaml = Yaml.createYamlMappingBuilder()
    .add("architect", "amihaiemil")
    .add("devops",
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

``toString()`` methods are overriden to pretty-print the yaml, so the above ``yaml.toString()`` will print (notice that the elements are ordered, according to the Yaml specification):

```yaml
architect: amihaiemil
developers: 
  - SherifWally
  - amihaiemil
  - salikjan
devops: 
  - 0pdd
  - rultor
```

## Reading:

Reading a Yaml input is very straight-forward, as outlined bellow. There is one **important** aspect, though: the input has to be
a well-indented Yaml (pretty), otherwise you will get an exception when trying to work with the read object!

```java
//createYamlInput is overloaded to accept also String InputStream
YamlMapping yamlMapping = Yaml.createYamlInput(new File("mapping.yml"))
    .readYamlMapping();

YamlSequence yamlSequence = Yaml.createYamlInput(new File("sequence.yml"))
    .readYamlSequence();
```

More examples to follow...

## Contribute

Contributors are [welcomed](http://www.amihaiemil.com/2016/12/30/becoming-a-contributor.html)

1. Open an issue regarding an improvement you thought of, or a bug you noticed, or ask to be assigned to an existing one.
2. If the issue is confirmed, fork the repository, do the changes on a separate branch and make a Pull Request.
3. After review and acceptance, the PR is merged and closed.
4. You are automatically listed as a contributor on the repo and the project's site (to follow)

Make sure the maven build

``$ mvn clean install -Pcheckstyle``

**passes before making a PR**. 
