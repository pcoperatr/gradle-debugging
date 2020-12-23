package de.lukaskoerfer.gradle.debugging;

import lombok.Getter;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaForkOptions;

/**
 * Applies debug settings to a target task that implements {@link JavaForkOptions}
 */
public class Debug extends DefaultTask {

    /**
     *
     */
    @Getter
    private final Property<JavaForkOptions> target;

    /**
     *
     */
    @Getter
    private final Property<Integer> port;

    /**
     *
     */
    @Getter
    private final Property<Boolean> server;

    /**
     *
     */
    @Getter
    private final Property<Boolean> suspend;

    /**
     * Creates a new task to debug
     */
    public Debug() {
        target = getProject().getObjects()
            .property(JavaForkOptions.class);
        port = getProject().getObjects()
            .property(Integer.class).convention(5005);
        server = getProject().getObjects()
            .property(Boolean.class).convention(true);
        suspend = getProject().getObjects()
            .property(Boolean.class).convention(true);
        finalizedBy(target);
    }

    @TaskAction
    private void applyDebugConfiguration() {
        JavaForkOptions task = target.get();
        task.debugOptions(options -> {
            options.getEnabled().set(true);
            options.getPort().set(port);
            options.getServer().set(server);
            options.getSuspend().set(suspend);
        });
    }
}
