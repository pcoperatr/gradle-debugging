package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Describes how to debug JVM processes
 */
public class DebugSpec {
    
    private static final String TRANSPORT_OPTION = "transport";
    private static final String ADDRESS_OPTION = "address";
    private static final String SERVER_OPTION = "server";
    private static final String SUSPEND_OPTION = "suspend";
    
    private static final String YES = "y";
    private static final String NO = "n";
    
    /**
     * Gets or sets the used debug transport method
     */
    @Getter @Setter
    private TransportMethod transport = TransportMethod.dt_socket;
    
    /**
     * Gets or sets the address to connect for debugging
     * <br><br>
     * You may use any object that provides the address string via its toString method, e.g. an Integer for a simple port
     */
    @Getter @Setter
    private Object address = 5050;
    
    /**
     * Gets or sets whether the JVM process should act as server in this debug session
     */
    @Getter @Setter
    private boolean server = false;
    
    /**
     * Gets or sets whether the JVM process should suspend until a debug connection is established
     */
    @Getter @Setter
    private boolean suspend = true;
    
    /**
     * Gets or sets whether the old flags -Xdebug and -Xrunjdwp should be used (Java < 5.0)
     */
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
    
    /**
     * Packs this debug specifications into JVM arguments
     * @return A list of JVM arguments
     */
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
