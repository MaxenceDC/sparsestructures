package io.github.maxencedc.sparsestructures;

import io.github.maxencedc.sparsestructures.command.DumpStructureSetsCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SparseStructuresFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SparseStructuresCommon.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DumpStructureSetsCommand.register(dispatcher);
        });
    }
}
