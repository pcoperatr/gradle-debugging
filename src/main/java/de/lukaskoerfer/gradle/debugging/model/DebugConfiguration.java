package de.lukaskoerfer.gradle.debugging.model;

import de.lukaskoerfer.gradle.debugging.DebuggingPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gradle.api.Named;

import java.util.function.UnaryOperator;

import static org.codehaus.groovy.runtime.StringGroovyMethods.capitalize;

/**
 * Describes a named configuration on how to debug a JVM processes
 */
@RequiredArgsConstructor
public class DebugConfiguration extends DebugSpec implements Named {
    
    /**
     * Gets the name of this configuration
     * @return A string
     */
    @Getter
    private final String name;
    
    public String getPrefix() {
        return name.equals(DebuggingPlugin.DEFAULT_DEBUG_CONFIGURATION) ? "debug" : name + "Debug";
    }

}
