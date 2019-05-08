package de.lukaskoerfer.gradle.debugging.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Describes how to debug JVM processes
 */
public class DebugSpecification {
    
    private static final String TRANSPORT_PARAMETER = "transport";
    private static final String ADDRESS_PARAMETER = "address";
    private static final String SERVER_PARAMETER = "server";
    private static final String SUSPEND_PARAMETER = "suspend";

    private static final String XFLAGS_OPTION = "xflags";
    
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
     * @return Any object, defaults to null
     * -- SETTER --
     * Sets the address to connect for debugging
     * @param address Any object that provides the address string via its <code>toString()</code> method, e.g. an Integer for a simple port
     */
    @Getter @Setter
    private Object address = null;
    
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
    private boolean suspend = false;

    /**
     * -- GETTER --
     * Gets whether the old flags <var>-Xdebug</var> and <var>-Xrunjdwp</var> should be used (before Java 5.0)
     * @return True if the old flags should be used, false otherwise
     * -- SETTER --
     * Sets whether the old flags <var>-Xdebug</var> and <var>-Xrunjdwp</var> should be used (before Java 5.0)
     * @param xflags True if the old flags should be used, false otherwise
     */
    @Getter @Setter
    private boolean xflags = false;

    /**
     *
     * @return
     */
    public boolean isConfigured() {
        return address != null;
    }

    /**
     * Copies all settings from a given specification
     * @param specification Another debug specification
     */
    public void inheritFrom(DebugSpecification specification) {
        transport = specification.transport;
        address = specification.address;
        server = specification.server;
        suspend = specification.suspend;
        xflags = specification.xflags;
    }

    /**
     *
     * @param settings
     */
    public void apply(Map<String, Object> settings) {
        Optional.ofNullable(settings.get(TRANSPORT_PARAMETER)).ifPresent(value -> {
            transport = TransportMethod.valueOf((String) value);
        });
        Optional.ofNullable(settings.get(ADDRESS_PARAMETER)).ifPresent(value -> {
            address = value;
        });
        Optional.ofNullable(settings.get(SERVER_PARAMETER)).ifPresent(value -> {
            server = (boolean) value;
        });
        Optional.ofNullable(settings.get(SUSPEND_PARAMETER)).ifPresent(value -> {
            suspend = (boolean) value;
        });
        Optional.ofNullable(settings.get(XFLAGS_OPTION)).ifPresent(value -> {
            xflags = (boolean) value;
        });
    }

    /**
     * Packs this debug specification into JVM arguments
     * @return A list of JVM argument strings
     */
    public List<String> getJvmArgs() {
        String options = getParameters().entrySet().stream()
            .map(option -> String.join("=", option.getKey(), option.getValue()))
            .collect(Collectors.joining(","));
        if (xflags) {
            return Arrays.asList("-Xdebug", "-Xrunjdwp:" + options);
        } else {
            return Collections.singletonList("-agentlib:jdwp=" + options);
        }
    }
    
    private Map<String, String> getParameters() {
        Map<String, String> options = new LinkedHashMap<>(4);
        options.put(TRANSPORT_PARAMETER, transport.name());
        options.put(ADDRESS_PARAMETER, address.toString());
        options.put(SERVER_PARAMETER, server ? YES : NO);
        options.put(SUSPEND_PARAMETER, suspend ? YES : NO);
        return options;
    }
    
}
