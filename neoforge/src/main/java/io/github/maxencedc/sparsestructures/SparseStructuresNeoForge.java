package io.github.maxencedc.sparsestructures;


import io.github.maxencedc.sparsestructures.command.DumpStructureSetsCommand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class SparseStructuresNeoForge {

    public SparseStructuresNeoForge() {
        SparseStructuresCommon.init();
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        DumpStructureSetsCommand.register(event.getDispatcher());
    }
}