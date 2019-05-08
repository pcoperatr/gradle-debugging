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
This plugin provides different ways to use extended debug features in a Gradle build:

#### Direct task configuration
First of all, this plugin registers an extension called `debugging` for every task that implements `JavaForkOptions` (e.g. `JavaExec` and `Test` tasks) to directly configure how to debug the task:
The only mandatory configuration is the `address`, which must be different than `null` to enable debugging.
It can be set to an integer, which will be interpreted as a local port. A string can be used to define a host name with port.

    test {
        debugging {
            address = 8000 // or '192.168.0.1:8000'
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
The Gradle debugging plugin provides a global container for debug configurations called `debugging`.
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

If a container element has the name `main`, the prefix for the `Debug` tasks will be `debug`, otherwise the prefix will be `<name>Debug`.
So for a task `test` and the configuration above, the plugin will create the tasks `debugTest`, `localDebugTest` and `remoteDebugTest`.
All generated tasks belong to the task group `Debugging` by default, as a call to `gradle tasks` will reveal.

#### Task rules
Gradle provides a feature called *task rules* that generates tasks on demand, which is supported by this plugin.
To enable task rules for debugging tasks, simply define a project property called `de.lukaskoerfer.gradle.debugging.rules` either in your `gradle.properties` file or via command line.
Calling `gradle tasks` will show that the tasks from the group `Debugging` are replaced by rules based on the pattern `<prefix><DebuggableTask>`.
It is even possible to reference tasks based on task rules in `dependsOn` and `finalizedBy` calls.

#### Map syntax
Instead of using one line for each piece of configuration, the plugin provides a simple map syntax to define a debug configuration in a single line:

    test {
        debugging.apply address: 8000, server: true
    }
    
    debugging {
        main.apply address: 8000
        // ...
    }

## License
The software is licensed under the [MIT license](https://github.com/lukoerfer/gradle-debugging/blob/master/LICENSE).
