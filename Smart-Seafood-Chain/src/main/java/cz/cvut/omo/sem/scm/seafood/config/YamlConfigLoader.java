package cz.cvut.omo.sem.scm.seafood.config;

import com.fasterxml.jackson.databind.ObjectMapper; // maps the file data to the Configuration class structure.
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory; // parsing backend that allows Jackson to understand YAML file format.

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class YamlConfigLoader {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static Configuration load(String configFileName) {
        // 1. Check if filename is provided
        if (configFileName == null || configFileName.isBlank()) {
            printHelp();
            return createFallbackConfig();
        }

        // 2. Try to obtain the stream (Logic separated to keep try-catch clean)
        InputStream is = getInputStream(configFileName);

        if (is == null) {
            System.out.printf("Config '%s' not found (checked classpath and disk). Using minimal fallback.%n", configFileName);
            return createFallbackConfig();
        }

        // 3. Process the stream with auto-close (try-with-resources)
        try (is) {
            System.out.println("Reading configuration from: " + configFileName);
            return mapper.readValue(is, Configuration.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse configuration: %s".formatted(configFileName), e);
        }
    }

    /**
     * Helper strategy: try Classpath first, then Disk.
     */
    private static InputStream getInputStream(String fileName) {
        // A. Try Classpath (Resources)
        InputStream stream = YamlConfigLoader.class.getClassLoader().getResourceAsStream(fileName);
        if (stream != null) {
            return stream;
        }

        // B. Try Disk (External file)
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            return null; // Both failed
        }
    }

    private static Configuration createFallbackConfig() {
        Configuration config = new Configuration();
        SimulationConfig sim = new SimulationConfig();
        sim.setMaxTicks(3);
        config.setSimulation(sim);
        return config;
    }

    private static void printHelp() {
        System.out.println("""
                No config file provided – using built-in minimal configuration (3 ticks).
                Available simulation configs in src/main/resources:
                  • scm-test.yaml
                  • nordic-fresh.yaml
                  • baltic-frozen.yaml
                  • prague-sushi-luxury.yaml
                  • russian-sea-dominator.yaml
                """);
    }
}