package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.process.JavaDebugOptions;

public class LegacyConvention {

    @Getter
    private final JavaDebugOptions debugOptions;

    public LegacyConvention(Task task) {
        debugOptions = new LegacyDebugOptions(task);
    }

    public void debugOptions(Action<JavaDebugOptions> action) {
        action.execute(debugOptions);
    }

}
