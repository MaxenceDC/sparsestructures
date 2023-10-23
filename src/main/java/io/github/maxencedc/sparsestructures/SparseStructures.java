package io.github.maxencedc.sparsestructures;

import net.fabricmc.api.ModInitializer;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SparseStructures implements ModInitializer {
    private static final String CONFIG_RESOURCE_NAME = "default-config.json5";
    private static final String CONFIG_FILENAME = "sparsestructures.json5";
    private static final Path CONFIG_FILE_PATH = Paths.get("config", CONFIG_FILENAME);
    public static SparseStructuresConfig config;

    public void onInitialize() {
        if (!CONFIG_FILE_PATH.toFile().exists()) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
                if (in == null) throw new IllegalStateException("Failed to load SparseStructure's default config \"" + CONFIG_RESOURCE_NAME +"\"");
                java.nio.file.Files.createDirectories(CONFIG_FILE_PATH);
                java.nio.file.Files.copy(in, CONFIG_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try (final InputStream in = java.nio.file.Files.newInputStream(CONFIG_FILE_PATH)) {
            config = new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(in), SparseStructuresConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("SparseStructure's config file is malformed! If you don't know what's causing this, delete the config file and restart the game.");
        }
    }
}
