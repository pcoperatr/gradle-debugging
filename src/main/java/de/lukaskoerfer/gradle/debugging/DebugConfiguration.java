package de.lukaskoerfer.gradle.debugging;

import lombok.Getter;
import org.gradle.api.Named;
import org.gradle.api.Project;

import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.gradle.process.JavaDebugOptions;

/**
 * Describes a configuration on how to debug JVM processes
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class DebugConfiguration implements Named, JavaDebugOptions {

    /**
     * Gets the name of this configuration
     * @return The unique configuration name
     */
    @Getter
    private final String name;

    /**
     * Creates a new debug configuration
     * @param name A unique name
     * @param project The project instance
     */
    @Inject
    public DebugConfiguration(String name, Project project) {
        this.name = name;
        getEnabled().convention(true);
        getPort().convention(5005);
        getHost().convention("localhost");
        getServer().convention(true);
        getSuspend().convention(true);
    }

    /**
     *
     * @param options
     */
    public void at(Map<String, Object> options) {
        Optional.ofNullable(options.get("port"))
            .map(Integer.class::cast)
            .ifPresent(port -> getPort().set(port));
        Optional.ofNullable(options.get("host"))
            .map(String.class::cast)
            .ifPresent(host -> getHost().set(host));
        Optional.ofNullable(options.get("server"))
            .map(Boolean.class::cast)
            .ifPresent(server -> getServer().set(server));
        Optional.ofNullable(options.get("suspend"))
            .map(Boolean.class::cast)
            .ifPresent(suspend -> getSuspend().set(suspend));
    }

    /**
     * Gets the prefix to use for task names that are debugged with this configuration
     * @return A task name prefix
     */
    public String getPrefix() {
        return name.equals(DebuggingPlugin.DEFAULT_DEBUG_CONFIGURATION) ? "debug" : name + "Debug";
    }
}
