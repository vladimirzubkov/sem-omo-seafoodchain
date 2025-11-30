package cz.cvut.omo.sem.scm.seafood.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;

public class YamlConfigLoader {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    /**
     * Loads config from resources.
     * If file missing or null → returns built-in minimal config (3 ticks).
     */
    public static Configuration load(String configFileName) {
        if (configFileName == null || configFileName.isBlank()) {
            System.out.println("""
                    No config file provided – using built-in minimal configuration (3 ticks).
                    
                    Available simulation configs in src/main/resources:
                      • scm-test.yaml            → 5 ticks - 5 hours
                      • nordic-fresh.yaml        → Norwegian premium fresh chain, 1 week
                      • baltic-frozen.yaml       → Baltic high-volume frozen, 2 weeks watch
                      • prague-sushi-luxury.yaml → Prague luxury sushi restaurant, 3 days cycle
                      • russian-sea-dominator.yaml → Russian sea monopoly, 30 days watch
                    
                    Run with config:
                      java -jar target/smart-seafood-chain.jar <config-name>.yaml
                    """);
            return createFallbackConfig();
        }

        try (InputStream is = YamlConfigLoader.class.getClassLoader()
                .getResourceAsStream(configFileName)) {

            if (is == null) {
                System.out.printf("""
                    Config file '%s' not found – using built-in minimal configuration (3 ticks).
                    
                    Available configs: tiny.yaml, quick-test.yaml, nordic-fresh.yaml,
                    baltic-frozen.yaml, prague-sushi-luxury.yaml, russian-sea-dominator.yaml
                    
                    Example: java -jar target/seafood.jar russian-sea-dominator.yaml
                    %n""", configFileName);
                return createFallbackConfig();
            }

            return mapper.readValue(is, Configuration.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration: %s".formatted(configFileName), e);
        }
    }

    // Fallback config when nothing is provided
    private static Configuration createFallbackConfig() {
        Configuration config = new Configuration();
        SimulationConfig sim = new SimulationConfig();
        sim.setMaxTicks(3);
        config.setSimulation(sim);
        return config;
    }
}