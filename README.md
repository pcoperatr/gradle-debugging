# Gradle Debugging Plugin
Gradle plugin for extended debug functionality

## Motivation
The Gradle API for (remote) debugging Gradle `JavaExec` tasks or tests is very limited.
You either need to stick to a specific debug configuration (via `debug = true`) or apply the JVM arguments on your own (via `jvmArgs`). This plugin adds the possibility to define debug parameters in the same declarative style as everything else in your build script.

## Download
The plugin is available via the [Gradle plugin portal](https://plugins.gradle.org/plugin/de.lukaskoerfer.gradle.debugging). Simply add it to the `plugins` block of your build script:

    plugins {
        id 'de.lukaskoerfer.gradle.debugging' version '0.2'
    }
    
## Usage
The plugin provides three different ways to use its extended debug features in a Gradle build:

#### Direct task configuration
By default, Gradle provides a simple boolean property called `debug` for all task types that implement `JavaForkOptions` (e.g. `JavaExec` and `Test`).
This plugin additionally registers a method with the same name that takes a closure to configure how to debug the task:

    test {
        debug {
            address = 8000
            server = true
            suspend = true
        }
    }

#### Task type `Debug`
Sometimes, it is not necessary or favoured to debug a task in every Gradle build.
As an example, tests could automatically connect to a debugger when executed on a local machine, but run without that debug configuration on a build server:

    task debugTest(type: Debug) {
        target = test
        address = 8000
        server = true
        suspend = true
    }
    
Now, to debug the test execution on your local machine, simply invoke `gradle debugTest`.
The behavior when calling `gradle test` will remain unchanged. This may be important for build servers or other environments, that simply call `gradle build` or `gradle check`.

#### Automatic generation of `Debug` tasks
Gradle prefers a declarative way to write build scripts instead of creating task manually like in the previous example.
The Gradle debugging plugin provides a container for debug configurations.
For each element in the container, a task of the type `Debug` will be created for each task that implements `JavaForkOptions`:

    debugging {
        main {
            address = 8000
            server = true
            suspend = true
        }
        local {
            // ...
        }
        remote {
            // ...
        }
    }

If a container element has the name `main`, the prefix for the `Debug` tasks will be `debug`, otherwise the prefix will be `<name>Debug`, e.g. `localDebug` for the element `local` in the example above.
The created tasks for the task test and the example above will be `debugTest`, `localDebugTest` and `remoteDebugTest`.

## License
The software is licensed under the [MIT license](https://github.com/lukoerfer/gradle-debugging/blob/master/LICENSE).
