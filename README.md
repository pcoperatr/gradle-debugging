# Gradle Debugging Plugin
Gradle plugin for extended debug functionality

## Motivation


## Download
The plugin is available via the [Gradle plugin portal](https://plugins.gradle.org/plugin/de.lukaskoerfer.gradle.debugging). Simply add it to the `plugins` block of your build script:

``` gradle
plugins {
    id 'de.lukaskoerfer.gradle.debugging' version '0.3'
}
```
    
## Usage

Once the plugin is applied, tasks that implement `JavaForkOptions` (e.g. `Test` or `JavaExec`) can be debugged by calling them with the prefix `debug`.
As an example, the following command debugs the task `test` created by the [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html):

```
gradlew debugTest
```

The actual task `debugTest` will be created on the fly using a Gradle feature called [task rules](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:task_rules).
By default, the debug configuration of the [Gradle `debug` flag](https://docs.gradle.org/current/javadoc/org/gradle/process/JavaForkOptions.html#getDebug--) will be used, causing the process to be started in a suspended state, listening on port 5005.
To change this behavior, a `debugging` block in the `build.gradle` file can be used, as shown in the example below:

``` gradle
debugging {
    main {
        server = true
        suspend = false
        port = 5000
    }
}
```

The `main` block refers to the default debug configuration that is created by the plugin.
To manage different debug configurations, additional inner blocks may be created and configured in the same way.


``` gradle
debugging {
    local {
        server = true
        suspend = true
        port = 8008
    }
    // Short notation
    remote.at server: true, suspend: true, port: 8008 
}
```



```
gradlew localDebugTest
```



## License
The software is licensed under the [MIT license](https://github.com/lukoerfer/gradle-debugging/blob/master/LICENSE).
