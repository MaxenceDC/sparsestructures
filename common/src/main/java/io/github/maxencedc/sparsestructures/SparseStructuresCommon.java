package io.github.maxencedc.sparsestructures;

import io.github.maxencedc.sparsestructures.platform.Services;

import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

import com.google.gson.Gson;

import static io.github.maxencedc.sparsestructures.Constants.CONFIG_FILE_PATH;
import static io.github.maxencedc.sparsestructures.Constants.CONFIG_RESOURCE_NAME;

public class SparseStructuresCommon {
    public static SparseStructuresConfig config;

    public static void init() {

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {

            if (!CONFIG_FILE_PATH.toFile().exists()) {
                try (InputStream in = SparseStructuresCommon.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE_NAME)) {
                    if (in == null) throw new IllegalStateException("Failed to load SparseStructure's default config \"" + CONFIG_RESOURCE_NAME +"\"");
                    Files.createDirectories(CONFIG_FILE_PATH);
                    Files.copy(in, CONFIG_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try (final InputStream in = Files.newInputStream(CONFIG_FILE_PATH)) {
                config = new Gson().fromJson(new java.io.InputStreamReader(in), SparseStructuresConfig.class);
            } catch (Exception e) {
                throw new RuntimeException("SparseStructure's config file is malformed! If you don't know what's causing this, delete the config file and restart the game.");
            }
        }
    }
}