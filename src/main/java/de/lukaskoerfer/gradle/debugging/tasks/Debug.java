package de.lukaskoerfer.gradle.debugging.tasks;

import de.lukaskoerfer.gradle.debugging.model.DebugSpec;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaForkOptions;

import java.io.IOException;

import static de.lukaskoerfer.gradle.debugging.DebuggingPlugin.*;

public class Debug extends DefaultTask {
    
    @Getter @Setter
    private DebugSpec debugSpec = getConvention()
        .create(DEBUG_SPECIFICATION_ID, DebugSpec.class);
    
    @Getter @Setter
    private JavaForkOptions target;
    
    public Debug() {
        getProject().afterEvaluate(project -> {
            finalizedBy(target);
        });
    }
    
    @TaskAction
    private void run() throws IOException {
        target.jvmArgs(debugSpec.getJvmArgs());
    }

}
