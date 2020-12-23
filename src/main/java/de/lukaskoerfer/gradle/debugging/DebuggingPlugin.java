package de.lukaskoerfer.gradle.debugging;

import static org.codehaus.groovy.runtime.StringGroovyMethods.capitalize;
import static org.codehaus.groovy.runtime.StringGroovyMethods.uncapitalize;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.process.JavaForkOptions;

/**
 * Provides a {@link Plugin} that provides extended debugging functionality
 */
public class DebuggingPlugin implements Plugin<Project> {

    /**
     * Defines the name of the debug configuration container
     */
    public static final String DEBUG_CONFIGURATION_CONTAINER = "debugging";
    /**
     * Defines the name of the default debug configuration
     */
    public static final String DEFAULT_DEBUG_CONFIGURATION = "main";
    /**
     * Defines the group for tasks created by debug configurations
     */
    private static final String DEBUGGING_TASK_GROUP = "Debug";
    /**
     * Defines the description template for tasks created by the debugging container
     */
    private static final String DEBUGGING_TASK_DESCRIPTION = "Debugs task '%s' using debug configuration '%s'";

    /**
     * Applies the debugging plugin to a project
     * @param project A Gradle project instance
     */
    @Override
    public void apply(Project project) {
        NamedDomainObjectContainer<DebugConfiguration> container =
            project.container(DebugConfiguration.class, name -> new DebugConfiguration(name, project));
        container.all(configuration -> setupDebugConfiguration(project, configuration));
        container.create(DEFAULT_DEBUG_CONFIGURATION);
        project.getExtensions().add(DEBUG_CONFIGURATION_CONTAINER, container);
    }
    
    private void setupDebugConfiguration(Project project, DebugConfiguration configuration) {
        project.getLogger().debug("");
        TaskCollection<Task> debuggableTasks = project.getTasks().matching(JavaForkOptions.class::isInstance);
        String prefix = configuration.getPrefix();
        project.getTasks().addRule("Pattern: " + prefix + "<DebuggableTask>", name -> {
            if (name.startsWith(prefix)) {
                String targetName = uncapitalize(name.substring(prefix.length()));
                Task target = debuggableTasks.findByName(targetName);
                if (target != null) {
                    createDebugTask(project, target, configuration);
                } else {
                    project.getLogger().warn("");
                }
            }
        });
    }
    
    private void createDebugTask(Project project, Task target, DebugConfiguration configuration) {
        project.getLogger().debug("");
        String name = configuration.getPrefix() + capitalize((CharSequence) target.getName());
        Debug task = project.getTasks().create(name, Debug.class);
        task.setGroup(DEBUGGING_TASK_GROUP);
        task.setDescription(String.format(DEBUGGING_TASK_DESCRIPTION, target.getName(), configuration.getName()));
        task.getTarget().set((JavaForkOptions) target);
        task.getPort().set(configuration.getPort());
        task.getServer().set(configuration.getServer());
        task.getSuspend().set(configuration.getSuspend());
    }

    
}
