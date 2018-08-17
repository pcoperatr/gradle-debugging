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
     * -- GETTER --
     * Gets the used debug transport method
     * @return The used transport method
     * -- SETTER --
     * Sets the used debug transport method
     * @param transport A transport method (or string with a fitting name)
     */
    @Getter @Setter
    private TransportMethod transport = TransportMethod.dt_socket;
    
    /**
     * -- GETTER --
     * Gets the address to connect for debugging
     * @return Any object
     * -- SETTER --
     * Sets the address to connect for debugging
     * @param address Any object that provides the address string via its <code>toString()</code> method, e.g. an Integer for a simple port
     */
    @Getter @Setter
    private Object address = 5050;
    
    /**
     * -- GETTER --
     * Gets whether the JVM process should act as server in this debug session
     * @return True if the process should act as server, false otherwise
     * -- SETTER --
     * Sets whether the JVM process should act as server in this debug session
     * @param server True if the process should act as server, false otherwise
     */
    @Getter @Setter
    private boolean server = false;
    
    /**
     * -- GETTER --
     * Gets whether the JVM process should suspend until a debug connection is established
     * @return True if the process should suspend, false otherwise
     * -- SETTER --
     * Sets whether the JVM process should suspend until a debug connection is established
     * @param suspend True if the process should suspend, false otherwise
     */
    @Getter @Setter
    private boolean suspend = true;
    
    /**
     * -- GETTER --
     * Gets whether the old flags <var>-Xdebug</var> and <var>-Xrunjdwp</var> should be used (before Java 5.0)
     * @return True if the old flags should be used, false otherwise
     * -- SETTER --
     * Sets whether the old flags <var>-Xdebug</var> and <var>-Xrunjdwp</var> should be used (before Java 5.0)
     * @param useXFlags True if the old flags should be used, false otherwise
     */
    @Getter @Setter
    private boolean useXFlags = false;
    
    /**
     * Copies all settings from a given specification
     * @param otherSpec Another debug specification
     */
    public void copyFrom(DebugSpec otherSpec) {
        this.transport = otherSpec.transport;
        this.address = otherSpec.address;
        this.server = otherSpec.server;
        this.suspend = otherSpec.suspend;
        this.useXFlags = otherSpec.useXFlags;
    }
    
    /**
     * Packs this debug specification into JVM arguments
     * @return A list of JVM argument strings
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
    
    private Map<String, String> getOptions() {
        Map<String, String> options = new LinkedHashMap<>(4);
        options.put(TRANSPORT_OPTION, transport.name());
        options.put(ADDRESS_OPTION, address.toString());
        options.put(SERVER_OPTION, server ? YES : NO);
        options.put(SUSPEND_OPTION, suspend ? YES : NO);
        return options;
    }
    
}
