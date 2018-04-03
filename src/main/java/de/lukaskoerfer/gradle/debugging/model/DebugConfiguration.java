package de.lukaskoerfer.gradle.debugging.model;

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
    
    /**
     * -- GETTER --
     * Gets the debug task name generation pattern for this configuration
     * @return An operator that takes a task name string and returns a Debug task name string
     * -- SETTER --
     * Sets the debug task name generation pattern for this configuration
     * <br><br>
     * <b>Warning:</b> Changing the pattern may lead to confusion because the old pattern may have been used for task name generation before the change took effect!
     * @param pattern An operator that takes a task name string and returns a Debug task name string
     */
    @Getter @Setter
    private UnaryOperator<String> pattern =
        taskName -> getName() + "Debug" + capitalize((CharSequence) taskName);

}
