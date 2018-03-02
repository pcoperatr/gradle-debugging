package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gradle.api.Named;

import java.util.function.UnaryOperator;

import static de.lukaskoerfer.gradle.debugging.util.StringUtil.capitalize;

@RequiredArgsConstructor
public class DebugConfiguration extends DebugSpec implements Named {
    
    @Getter
    private final String name;
    
    @Getter @Setter
    private UnaryOperator<String> pattern =
        taskName -> getName() + "Debug" + capitalize(taskName);

}
