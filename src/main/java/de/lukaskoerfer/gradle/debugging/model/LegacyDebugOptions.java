package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import org.gradle.api.Task;
import org.gradle.api.provider.Property;
import org.gradle.process.JavaDebugOptions;
import org.gradle.process.JavaForkOptions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LegacyDebugOptions implements JavaDebugOptions {

    private static final String ADDRESS_PARAMETER = "address";
    private static final String SERVER_PARAMETER = "server";
    private static final String SUSPEND_PARAMETER = "suspend";

    private static final String YES = "y";
    private static final String NO = "n";

    @Getter
    private final Property<Boolean> enabled;

    @Getter
    private final Property<Integer> port;

    @Getter
    private final Property<Boolean> server;

    @Getter
    private final Property<Boolean> suspend;

    public LegacyDebugOptions(Task task) {
        enabled = task.getProject().getObjects().property(Boolean.class);
        port = task.getProject().getObjects().property(Integer.class);
        server = task.getProject().getObjects().property(Boolean.class);
        suspend = task.getProject().getObjects().property(Boolean.class);
        task.getProject().afterEvaluate(project -> {
            apply((JavaForkOptions) task);
        });
    }

    private void apply(JavaForkOptions task) {
        String options = mapOptions().entrySet().stream()
            .map(option -> String.join("=", option.getKey(), option.getValue()))
            .collect(Collectors.joining(","));
        task.jvmArgs("-agentlib:jdwp=" + options);
    }

    private Map<String, String> mapOptions() {
        Map<String, String> options = new LinkedHashMap<>(3);
        options.put(ADDRESS_PARAMETER, port.get().toString());
        options.put(SERVER_PARAMETER, server.get() ? YES : NO);
        options.put(SUSPEND_PARAMETER, suspend.get() ? YES : NO);
        return options;
    }

}
