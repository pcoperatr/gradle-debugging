package de.lukaskoerfer.gradle.debugging.tasks;

import de.lukaskoerfer.gradle.debugging.model.DebugSpec;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaForkOptions;

import java.io.IOException;

import static de.lukaskoerfer.gradle.debugging.DebuggingPlugin.*;

/**
 * Applies debug settings to a target task (that implements {@link JavaForkOptions}) on execution
 * <br><br>
 * Using a task with this type enables conditional debugging based on task execution.
 * Executing only the target task will not activate debugging.
 * Only calling a {@link Debug} task will add the debug specific JVM arguments to the target task.
 * For each target task, the last associated {@link Debug} task executed will overwrite all others.
 */
public class Debug extends DefaultTask {
    
    /**
     * Gets or sets the debug specification that describes how the target task should be debugged
     * <br><br>
     * The properties of this debug specification are directly available to the task.
     */
    @Getter @Setter
    private DebugSpec debugSpec = getConvention()
        .create(DEBUG_SPECIFICATION_ID, DebugSpec.class);
    
    /**
     * Gets or sets the target task to debug
     */
    @Getter @Setter
    private JavaForkOptions target;
    
    /**
     * Creates a new debug task
     */
    public Debug() {
        getProject().afterEvaluate(project -> finalizedBy(target));
    }
    
    @TaskAction
    private void run() throws IOException {
        target.jvmArgs(debugSpec.getJvmArgs());
    }

}
