package io.github.maxencedc.sparsestructures;

import io.github.maxencedc.sparsestructures.command.DumpStructureSetsCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class SparseStructuresForge {

    public SparseStructuresForge(FMLJavaModLoadingContext context) {
        SparseStructuresCommon.init();
        RegisterCommandsEvent.BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        DumpStructureSetsCommand.register(event.getDispatcher());
    }
}