package io.github.maxencedc.sparsestructures;

import io.github.maxencedc.sparsestructures.command.DumpStructureSetsCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SparseStructuresForge {

    public SparseStructuresForge() {
        SparseStructuresCommon.init();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        DumpStructureSetsCommand.register(event.getDispatcher());
    }
}