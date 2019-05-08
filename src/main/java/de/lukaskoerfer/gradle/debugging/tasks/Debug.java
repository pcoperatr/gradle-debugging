package de.lukaskoerfer.gradle.debugging.tasks;

import de.lukaskoerfer.gradle.debugging.model.DebugSpecification;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaForkOptions;

import java.io.IOException;

/**
 * Applies debug settings to a target task (that implements {@link JavaForkOptions}) on execution
 * <p>Using a task with this type enables conditional debugging based on task execution.
 * Executing only the target task will not activate debugging.
 * Only calling a {@link Debug} task will add the debug specific JVM arguments to the target task.
 * For each target task, the last associated {@link Debug} task executed will overwrite all others.</p>
 */
public class Debug extends DefaultTask {
    
    /**
     * -- GETTER --
     * Gets the target task to debug
     * @return The current target task, may be null if not set yet
     * -- SETTER --
     * Sets the target task to debug
     * @param target Any task that implements {@link JavaForkOptions}
     */
    @Getter @Setter
    private JavaForkOptions target;
    
    @Delegate
    private final DebugSpecification spec = new DebugSpecification();
    
    /**
     * Creates a new debug task
     */
    public Debug() {
        getProject().afterEvaluate(project -> {
            finalizedBy(target);
        });
    }
    
    @TaskAction
    private void setupDebugForTarget() throws IOException {
        target.jvmArgs(spec.getJvmArgs());
    }

}
