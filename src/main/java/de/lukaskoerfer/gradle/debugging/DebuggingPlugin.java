package de.lukaskoerfer.gradle.debugging;

import de.lukaskoerfer.gradle.debugging.model.DebugConfiguration;
import de.lukaskoerfer.gradle.debugging.tasks.Debug;
import de.lukaskoerfer.gradle.debugging.tasks.TaskDebugConvention;
import de.lukaskoerfer.gradle.debugging.util.StringUtil;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.process.JavaForkOptions;

import static de.lukaskoerfer.gradle.debugging.util.StringUtil.capitalize;

public class DebuggingPlugin implements Plugin<Project> {
    
    public static final String DEBUG_TASK_GROUP = "debugging";
    public static final String DEBUG_TASK_PREFIX = "debug";
    public static final String DEBUG_SPECIFICATION_ID = "debugSpec";
    public static final String DEBUG_CONFIGURATION_CONTAINER = "debugging";
    public static final String MAIN_DEBUG_CONFIGURATION = "main";
    
    @Override
    public void apply(Project project) {
        // Register debug convention for each debuggable task
        project.getTasks().matching(JavaForkOptions.class::isInstance)
            .all(task ->  task.getConvention().create(DEBUG_SPECIFICATION_ID, TaskDebugConvention.class, task));
        // Register global debugging configuration container
        project.getExtensions().add(DEBUG_CONFIGURATION_CONTAINER, createDebuggingContainer(project));
    }
    
    private Object createDebuggingContainer(Project project) {
        NamedDomainObjectContainer<DebugConfiguration> debugging =
            project.container(DebugConfiguration.class);
        // Add main configuration
        debugging.create(MAIN_DEBUG_CONFIGURATION)
            .setPattern(taskName -> DEBUG_TASK_PREFIX + capitalize(taskName));
        // Add handler for all configurations
        debugging.all(configuration -> createDebugTasks(project, configuration));
        return debugging;
    }
    
    private void createDebugTasks(Project project, DebugConfiguration configuration) {
        project.getTasks().matching(JavaForkOptions.class::isInstance).all(targetTask -> {
            String taskName = configuration.getPattern().apply(targetTask.getName());
            Debug task = project.getTasks().create(taskName, Debug.class);
            task.setGroup(StringUtil.capitalize(DEBUG_TASK_GROUP));
            task.setDescription("Debugs the task '" + targetTask.getName() + "' with the '" + configuration.getName() + "' debug configuration");
            task.setDebugSpec(configuration);
            task.setTarget((JavaForkOptions) targetTask);
        });
    }
    
}
