package de.lukaskoerfer.gradle.debugging.tasks;

import de.lukaskoerfer.gradle.debugging.model.DebugSpec;
import groovy.lang.Closure;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Action;
import org.gradle.process.JavaForkOptions;

@RequiredArgsConstructor
public class TaskDebugConvention {
    
    private final JavaForkOptions task;
    
    public void debug(Action<DebugSpec> specAction) {
        DebugSpec spec = new DebugSpec();
        specAction.execute(spec);
        task.jvmArgs(spec.getJvmArgs());
    }
    
    public void debug(Closure specAction) {
        DebugSpec spec = new DebugSpec();
        specAction.setDelegate(spec);
        specAction.call(spec);
        task.jvmArgs(spec.getJvmArgs());
    }
    
}
