package de.lukaskoerfer.gradle.debugging.model;

import de.lukaskoerfer.gradle.debugging.DebuggingPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Named;

/**
 * Describes a named configuration on how to debug a JVM processes
 */
@RequiredArgsConstructor
public class DebugConfiguration extends DebugSpecification implements Named {
    
    /**
     * Gets the name of this configuration
     * @return A unique identifier
     */
    @Getter
    private final String name;
    
    /**
     * Gets the prefix to use for task names that are debugged with this configuration
     * @return A task name prefix
     */
    public String getPrefix() {
        return name.equals(DebuggingPlugin.DEFAULT_DEBUG_CONFIGURATION) ? "debug" : name + "Debug";
    }

}
