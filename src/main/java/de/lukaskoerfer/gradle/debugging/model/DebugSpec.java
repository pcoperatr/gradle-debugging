package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class DebugSpec {
    
    private static final String TRANSPORT_OPTION = "transport";
    private static final String ADDRESS_OPTION = "address";
    private static final String SERVER_OPTION = "server";
    private static final String SUSPEND_OPTION = "suspend";
    
    private static final String YES = "y";
    private static final String NO = "n";
    
    @Getter @Setter
    private TransportMethod transport = TransportMethod.dt_socket;
    
    @Getter @Setter
    private Object address = 5050;
    
    @Getter @Setter
    private boolean server = false;
    
    @Getter @Setter
    private boolean suspend = true;
    
    @Getter @Setter
    private boolean useXFlags = false;
    
    private Map<String, String> getOptions() {
        Map<String, String> options = new HashMap<>();
        options.put(TRANSPORT_OPTION, transport.name());
        options.put(ADDRESS_OPTION, address.toString());
        options.put(SERVER_OPTION, server ? YES : NO);
        options.put(SUSPEND_OPTION, suspend ? YES : NO);
        return options;
    }
    
    public List<String> getJvmArgs() {
        String options = getOptions().entrySet().stream()
            .map(option -> String.join("=", option.getKey(), option.getValue()))
            .collect(Collectors.joining(","));
        if (useXFlags) {
            return Arrays.asList("-Xdebug", "-Xrunjdwp:" + options);
        } else {
            return Collections.singletonList("-agentlib:jdwp=" + options);
        }
    }
    
}
