# Gradle Scalate plugin

**State**: Experimental

This plugin provides basic support for precompiling [Scalate](http://scalate.fusesource.org/) templates.
It defines a new task `scalatePreCompile` that runs before `compileScala`, which generates Scala files equivalent to the
input Scalate templates using the Scalate precompiler. The output of `scalatePreCompile` is passed to `compileScala` as
additional Scala sources to compile.

*Note:* [Scalate](http://scalate.fusesource.org/) already provides
[official plugins to Precompile Templates](http://scalate.fusesource.org/documentation/user-guide.html#precompiling_templates)
for Maven and SBT. Use of the official plugin in Maven or SBT is strongly recommended. Use this plugin **only when** you
have to precompile your Scalate templates in a Gradle build. This plugin aims to behave similarly to the SBT plugin.

## Compatibility

 * Tested with Gradle 4.0, should work with >=2.0
 * Tested with Scalate 1.8.0 (uses precompiler version 1.8.0.1), should work with other versions as long as the
   API of precompiled templates is compatible.

## Requirements

* The plugin depends on `'scala'` plugin. Include below in your build script.
```gradle
apply plugin: 'scala'
```
* Add necessary dependencies for scala. See [user manual for "gradle scala plugin"](http://gradle.org/docs/current/userguide/scala_plugin.html#N12952).

## Setup

The plugin is currently published in a Bintray repository. To include it in your project:

```
buildscript {
    repositories {
    jcenter()
        maven {
            url "http://dl.bintray.com/abesto/gradle-plugins"
        }
    }

    dependencies {
        classpath "murtools:gradle-scalate-plugin:0.0.1"
    }
}

apply plugin: 'scalate'
```

## Configuration

All the customizable options, along with defaults:

```
scalate {
  templateSrcDir 'src/main/webapp'
  generatedDir   'build/scalate-gen'
  loggingConfig  'src/main/resources/logback.xml'
  packagePrefix  'scalate'
}
