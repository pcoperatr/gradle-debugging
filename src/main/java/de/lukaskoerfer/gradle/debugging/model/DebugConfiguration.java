package de.lukaskoerfer.gradle.debugging.model;

import de.lukaskoerfer.gradle.debugging.DebuggingPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Named;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.process.JavaDebugOptions;

import java.util.Map;
import java.util.Optional;

/**
 * Describes a named configuration on how to debug a JVM processes
 */
public class DebugConfiguration implements Named {
    
    /**
     * Gets the name of this configuration
     * @return A unique identifier
     */
    @Getter
    private final String name;

    @Getter
    private final Property<Integer> port;

    @Getter
    private final Property<Boolean> server;

    @Getter
    private final Property<Boolean> suspend;

    public DebugConfiguration(String name, Project project) {
        this.name = name;
        port = project.getObjects().property(Integer.class);
        server = project.getObjects().property(Boolean.class);
        suspend = project.getObjects().property(Boolean.class);
    }

    public void at(Map<String, Object> options) {
        Optional.ofNullable(options.get("port"))
            .map(Integer.class::cast)
            .ifPresent(port::set);
        Optional.ofNullable(options.get("server"))
            .map(Boolean.class::cast)
            .ifPresent(server::set);
        Optional.ofNullable(options.get("suspend"))
            .map(Boolean.class::cast)
            .ifPresent(server::set);
    }

    /**
     * Gets the prefix to use for task names that are debugged with this configuration
     * @return A task name prefix
     */
    public String getPrefix() {
        return name.equals(DebuggingPlugin.DEFAULT_DEBUG_CONFIGURATION) ? "debug" : name + "Debug";
    }

}
