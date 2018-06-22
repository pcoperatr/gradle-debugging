# Gradle Debugging Plugin
Gradle plugin for extended debug functionality



## Motivation
The Gradle API for (remote) debugging Gradle `JavaExec` tasks or tests is very limited. You either need to stick to a specific default debug configuration or apply the JVM arguments on your own (via `jvmArgs` method). This plugin adds the possibility to define debug parameters in the same declarative style as everything else in your build script.

## Download
The plugin is available via the [Gradle plugin portal](https://plugins.gradle.org/plugin/de.lukaskoerfer.gradle.debugging). Simply add it to the `plugins` block of your build script:

    plugins {
        id 'de.lukaskoerfer.gradle.debugging' version '0.1'
    }
    
## Usage
The plugin provides three different ways to use its extended debug features in a Gradle build:

#### Direct configuration
By default, Gradle provides a `debug` property for all task types that implements `JavaForkOptions` (e.g. `JavaExec` and `Test`). This plugin adds a method to these tasks with the same name. It takes a closure to configure how to debug the task:

    test {
        debug {
            server = true
            suspend = true
            address = 8000
        }
    }

#### Debug configuration task

    task debugMyTest(type: Debug) {
        target = test
        server = true
        suspend = true
        address = 8000
    }

#### Global debug configurations

    debugging {
        default {
            
        }
        remote {
            
        }
    }

## License
The software is licensed under the [MIT license](https://github.com/lukoerfer/gradle-debugging/blob/master/LICENSE).
