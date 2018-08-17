package de.lukaskoerfer.gradle.debugging.model;

/**
 * Describes how the debugging information will be transported.
 * <p>This enumeration does not follow the naming convention to provide a better user experience in Gradle build scripts.</p>
 */
public enum TransportMethod {
    /**
     * Transport via socket (default option).
     */
    dt_socket,
    /**
     * Transport via shared memory.
     * <p><b>Please note:</b> This option is available only on the Microsoft Windows platform.</p>
     */
    dt_shmem
}
