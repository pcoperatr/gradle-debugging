package de.lukaskoerfer.gradle.debugging;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaDebugOptions;
import org.gradle.process.JavaForkOptions;

/**
 * Applies debug settings to a target task that implements {@link JavaForkOptions}
 */
public abstract class Debug extends DefaultTask {

    @Inject
    public Debug() {
    }

    @Internal
    public abstract Property<JavaForkOptions> getTarget();

    @Internal
    public abstract Property<JavaDebugOptions> getDebugOptions();


    @TaskAction
    protected void applyDebugConfiguration() {
        JavaForkOptions task = getTarget().get();
        JavaDebugOptions from = getDebugOptions().get();
        task.debugOptions(options -> {
            options.getEnabled().set(from.getEnabled());
            options.getPort().set(from.getPort());
            options.getHost().set(from.getHost());
            options.getServer().set(from.getServer());
            options.getSuspend().set(from.getSuspend());
        });
    }
}
