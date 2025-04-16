package io.github.maxencedc.sparsestructures.mixin;

import net.minecraft.server.commands.LocateCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LocateCommand.class)
public class FixLocateDistance {

    /**
     * @author Maxence
     * @reason Fix the distance calculation (MC-177381)
     */
    @Overwrite
    private static float dist(int pos1x, int pos1z, int pos2x, int pos2z) {
        double d = pos2x - pos1x;
        double e = pos2z - pos1z;
        return (float) Math.hypot(d, e);
    }
}
