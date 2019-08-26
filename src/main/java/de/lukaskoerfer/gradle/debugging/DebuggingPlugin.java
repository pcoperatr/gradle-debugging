package de.lukaskoerfer.gradle.debugging;

import de.lukaskoerfer.gradle.debugging.model.DebugConfiguration;
import de.lukaskoerfer.gradle.debugging.tasks.Debug;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.process.JavaForkOptions;

/**
 * A {@link Plugin} that provides extended debugging functionality.
 * <p>
 * Applying this plugin will do the following:
 * <ul>
 * <li>Register extensions to tasks that implement {@link JavaForkOptions} add customized debug JVM options on each task that implements {@link JavaForkOptions}</li>
 * <li>Register a container for debugging configurations. Each configuration will create a {@link Debug} task for each task that implements {@link JavaForkOptions}</li>
 * <li>Add a main configuration to the container mentioned above</li>
 * </ul>
 */
public class DebuggingPlugin implements Plugin<Project> {

    /**
     *
     */
    public static final String DEBUG_EXTENSION = "debugging";
    /**
     *
     */
    public static final String DEBUG_CONFIGURATION_CONTAINER = "debugging";
    /**
     *
     */
    public static final String DEFAULT_DEBUG_CONFIGURATION = "main";
    /**
     * Defines the property string used as a flag whether to use task rules for debugging tasks
     */
    public static final String DEBUGGING_RULES_OPTION = "de.lukaskoerfer.gradle.debugging.rules";

    /**
     * Defines the group for tasks created by the debugging container
     */
    private static final String DEBUGGING_TASK_GROUP = "Debugging";
    /**
     * Defines the description template for tasks created by the debugging container
     */
    private static final String DEBUGGING_TASK_DESCRIPTION = "Debugs task '%s' in configuration '%s'";

    /**
     * Applies the debugging plugin to a project
     * @param project A Gradle project instance
     */
    @Override
    public void apply(Project project) {
        // Register task type Debug
        project.getExtensions().getExtraProperties().set(Debug.class.getSimpleName(), Debug.class);

        // Register the debug extension to debuggable tasks
        // TODO: Check for version < 5.6
        TaskCollection<Task> debuggableTasks = project.getTasks().matching(JavaForkOptions.class::isInstance);
        debuggableTasks.all(this::registerLegacyConvention);

        // Setup declarative debugging container
        NamedDomainObjectContainer<DebugConfiguration> container =
            project.container(DebugConfiguration.class, name -> new DebugConfiguration(name, project));
        container.all(configuration -> setupDebugConfiguration(project, configuration));
        project.getExtensions().add(DEBUG_CONFIGURATION_CONTAINER, container);
    }
    
    private void registerLegacyConvention(Task task) {
        // TODO
    }
    
    private void setupDebugConfiguration(Project project, DebugConfiguration configuration) {
        if (project.hasProperty(DEBUGGING_RULES_OPTION)) {
            createDebugRule(project, configuration);
        } else {
            createDebugTasks(project, configuration);
        }
    }
    
    private void createDebugTasks(Project project, DebugConfiguration configuration) {
        TaskCollection<Task> debuggableTasks = project.getTasks().matching(JavaForkOptions.class::isInstance);
        debuggableTasks.all(target -> {
            createDebugTask(project, target, configuration);
        });
    }
    
    private void createDebugRule(Project project, DebugConfiguration configuration) {
        TaskCollection<Task> debuggableTasks = project.getTasks().matching(JavaForkOptions.class::isInstance);
        String prefix = configuration.getPrefix();
        project.getTasks().addRule("Pattern: " + prefix + "<DebuggableTask>", name -> {
            if (name.startsWith(prefix)) {
                String targetName = uncapitalize(name.substring(prefix.length()));
                Task target = debuggableTasks.findByName(targetName);
                if (target != null) {
                    createDebugTask(project, target, configuration);
                }
            }
        });
    }
    
    private void createDebugTask(Project project, Task target, DebugConfiguration configuration) {
        String name = configuration.getPrefix() + capitalize(target.getName());
        Debug task = project.getTasks().create(name, Debug.class);
        task.setGroup(DEBUGGING_TASK_GROUP);
        task.setDescription(String.format(DEBUGGING_TASK_DESCRIPTION, target.getName(), configuration.getName()));
        task.getTarget().set((JavaForkOptions) target);
        task.getPort().convention(configuration.getPort());
        task.getServer().convention(configuration.getServer());
        task.getSuspend().convention(configuration.getSuspend());
    }

    private static String capitalize(String value) {
        return StringGroovyMethods.capitalize((CharSequence)value);
    }

    private static String uncapitalize(String value) {
        return StringGroovyMethods.uncapitalize(value);
    }
    
}
