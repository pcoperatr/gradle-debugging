package de.lukaskoerfer.gradle.debugging;

import de.lukaskoerfer.gradle.debugging.model.DebugConfiguration;
import de.lukaskoerfer.gradle.debugging.tasks.Debug;
import de.lukaskoerfer.gradle.debugging.tasks.ExtendedDebugConfiguration;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.process.JavaForkOptions;

/**
 * A {@link Plugin} that provides extended debugging functionality
 * <br><br>
 * Applying this plugin will do the following:
 * <ul>
 * <li>Register {@link ExtendedDebugConfiguration} methods to add customized debug JVM options on each task that implements {@link JavaForkOptions}</li>
 * <li>Register a container for debugging configurations. Each configuration will create a {@link Debug} task for each task that implements {@link JavaForkOptions}</li>
 * <li>Add a main configuration to the container mentioned above</li>
 * </ul>
 */
public class DebuggingPlugin implements Plugin<Project> {
    
    public static final String DEBUG_TASK_GROUP = "debugging";
    public static final String DEBUG_SPECIFICATION_ID = "debugSpec";
    public static final String DEBUG_CONFIGURATION_CONTAINER = "debugging";
    public static final String DEFAULT_DEBUG_CONFIGURATION = "default";
    
    private Project project;
    
    private TaskCollection<Task> debuggableTasks;
    
    /**
     * Applies the debugging plugin to a project
     * @param project A Gradle project instance
     */
    @Override
    public void apply(Project project) {
        this.project = project;
        debuggableTasks = project.getTasks().matching(JavaForkOptions.class::isInstance);
        // Register debug convention for each debuggable task
        debuggableTasks.all(task ->  task.getConvention().create(DEBUG_SPECIFICATION_ID, ExtendedDebugConfiguration.class, task));
        // Register global debugging configuration container
        project.getExtensions().add(DEBUG_CONFIGURATION_CONTAINER, createDebuggingContainer());
    }
    
    private Object createDebuggingContainer() {
        NamedDomainObjectContainer<DebugConfiguration> debugging =
            project.container(DebugConfiguration.class);
        debugging.all(this::handleDebugConfiguration);
        return debugging;
    }
    
    private void handleDebugConfiguration(DebugConfiguration configuration) {
        if (!project.hasProperty("debugging.rules")) {
            createDebugTasks(configuration);
        } else {
            createDebugRule(configuration);
        }
    }
    
    private void createDebugTasks(DebugConfiguration configuration) {
        debuggableTasks.all(targetTask -> {
            String taskName = configuration.getPrefix() + capitalize(targetTask.getName());
            createDebugTask(taskName, targetTask, configuration);
        });
    }
    
    private void createDebugRule(DebugConfiguration configuration) {
        String prefix = configuration.getPrefix();
        project.getTasks().addRule("Pattern: " + prefix + "<debuggableTask>", taskName -> {
            if (taskName.startsWith(prefix)) {
                String targetTaskName = uncapitalize(taskName.substring(prefix.length()));
                Task targetTask = debuggableTasks.findByName(targetTaskName);
                if (targetTask != null) {
                    createDebugTask(taskName, targetTask, configuration);
                }
            }
        });
    }
    
    private void createDebugTask(String taskName, Task targetTask, DebugConfiguration configuration) {
        Debug task = project.getTasks().create(taskName, Debug.class);
        task.setGroup(capitalize(DEBUG_TASK_GROUP));
        task.setDescription("Debugs the task '" + targetTask.getName() + "' with the '" + configuration.getName() + "' debug configuration");
        task.setDebugSpec(configuration);
        task.setTarget((JavaForkOptions) targetTask);
    }
    
    private static String capitalize(String value) {
        return StringGroovyMethods.capitalize((CharSequence)value);
    }
    
    private static String uncapitalize(String value) {
        return StringGroovyMethods.uncapitalize(value);
    }
    
}
