package de.lukaskoerfer.gradle.debugging.model;

/**
 * Describes how the debugging information will be transported
 */
public enum TransportMethod {
    /**
     * Transport via socket (default option)
     */
    dt_socket,
    /**
     * Transport via shared memory
     * <br><br>
     * This option is available only on the Microsoft Windows platform.
     */
    dt_shmem
}
