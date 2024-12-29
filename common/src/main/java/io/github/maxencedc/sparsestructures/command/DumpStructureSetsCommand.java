package io.github.maxencedc.sparsestructures.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.maxencedc.sparsestructures.Constants;
import io.github.maxencedc.sparsestructures.StructureSetsSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.minecraft.commands.Commands.literal;


public class DumpStructureSetsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
            literal("dumpstructuresets")
                .requires(cs -> cs.hasPermission(2))
                .executes(context -> {
                    context.getSource().sendSuccess(() -> Component.literal("Dumping structure set..."), false);
                    String fileName = new SimpleDateFormat("'structure_sets_dump_'yy_MM_dd_HH_mm'.txt'").format(new Date());
                    try {
                        dumpStructureSets(fileName);
                        context.getSource().sendSuccess(() -> {
                            boolean isDedicatedServer = context.getSource().getServer().isDedicatedServer();
                            String fileLocation = Paths.get(Constants.MOD_ID, fileName).toString();

                            String message = "Structure sets dumped to: `" + fileLocation + "`";
                            if (isDedicatedServer) {
                                message += "\n(you can find the result in the server's files)";
                            }

                            return Component.literal(message);
                        }, false);
                        return 1;
                    } catch (IOException e) {
                        context.getSource().sendSuccess(() -> Component.literal("Failed to dump structure sets, check logs for error"), false);
                        Constants.LOG.error("Failed to dump structure sets\n", e);
                        return 0;
                    }
                })
        );
    }

    private static void dumpStructureSets(String fileName) throws IOException {
        Path dumpPath = Path.of(Constants.MOD_ID);
        StringBuilder dump = new StringBuilder();
        StructureSetsSet.structureSets.forEach(s -> dump.append("{\n  \"structure\": \"").append(s).append("\",\n  \"factor\": 1//REPLACE WITH YOUR CUSTOM SPREADING FACTOR HERE\n},\n"));
        Files.createDirectories(dumpPath);
        Files.writeString(dumpPath.resolve(fileName), dump.toString());
    }
}
