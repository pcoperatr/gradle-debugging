package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gradle.api.Named;
import org.gradle.process.JavaForkOptions;

import java.util.function.UnaryOperator;

import static org.codehaus.groovy.runtime.StringGroovyMethods.capitalize;

/**
 * Describes a named configuration on how to debug a JVM processes
 */
@RequiredArgsConstructor
public class DebugConfiguration extends DebugSpec implements Named {
    
    /**
     * Gets the name of this configuration
     */
    @Getter
    private final String name;
    
    /**
     * Gets or sets the debug task name generation pattern for this configuration
     * <br><br>
     * Defaults to the pattern <i>configName</i>Debug<i>taskName</i>
     * -- SETTER --
     * Changing the pattern may lead to confusion because it may be used for task name generation before the change takes effect!
     */
    @Getter @Setter
    private UnaryOperator<String> pattern =
        taskName -> getName() + "Debug" + capitalize((CharSequence) taskName);

}
