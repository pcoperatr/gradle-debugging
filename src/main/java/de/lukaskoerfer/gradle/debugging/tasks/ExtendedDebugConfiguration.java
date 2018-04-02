package de.lukaskoerfer.gradle.debugging.tasks;

import de.lukaskoerfer.gradle.debugging.model.DebugSpec;
import groovy.lang.Closure;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Action;
import org.gradle.process.JavaForkOptions;

/**
 * Provides the functionality to specify a debug specification to a task that implements {@link JavaForkOptions}
 */
@RequiredArgsConstructor
public class ExtendedDebugConfiguration {
    
    private final JavaForkOptions task;
    
    /**
     * Adds JVM parameters for debugging according to the specification configured by the given action to the task
     * @param specAction An action with a debug specification as parameter
     */
    public void debug(Action<DebugSpec> specAction) {
        DebugSpec spec = new DebugSpec();
        specAction.execute(spec);
        task.jvmArgs(spec.getJvmArgs());
    }
    
    /**
     * Adds JVM parameters for debugging according to the specification configured by the given closure to the task
     * @param specAction A closure with a debug specification as both delegate and single parameter
     */
    public void debug(Closure specAction) {
        DebugSpec spec = new DebugSpec();
        specAction.setDelegate(spec);
        specAction.call(spec);
        task.jvmArgs(spec.getJvmArgs());
    }
    
}
