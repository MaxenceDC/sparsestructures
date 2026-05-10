package io.github.maxencedc.sparsestructures.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Decoder;
import io.github.maxencedc.sparsestructures.IdBasedSalt;
import io.github.maxencedc.sparsestructures.SparseStructuresCommon;
import io.github.maxencedc.sparsestructures.StructureSetsSet;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.resources.RegistryLoadTask$PendingRegistration")
public class MakeStructuresSparseNF {

    @Inject(method = "loadFromResource", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/NeoForgeExtraCodecs;decodeOnly(Lcom/mojang/serialization/Decoder;)Lcom/mojang/serialization/Codec;"))
    private static <T> void loadFromResource(Decoder<T> elementDecoder, RegistryOps<JsonElement> ops, ResourceKey<T> elementKey, Resource thunk, CallbackInfoReturnable<Either<T, Exception>> cir, @Local(name = "json") JsonElement json) {
        String string = elementKey.registryKey().identifier().getPath();
        if (!string.equals("worldgen/structure_set")) return;

        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject placement = jsonObject.getAsJsonObject("placement");
        if (placement.get("type").getAsString().equals("minecraft:concentric_rings")) return;

        StructureSetsSet.addStructureSet(elementKey.identifier().toString());

        double factor = SparseStructuresCommon.config.getSpreadFactor(elementKey, jsonObject);

        if (factor == 0) {
            placement.addProperty("frequency", 0.0);
            return;
        }

        int spacing = (placement.get("spacing") == null) ? 1 : (int)(placement.get("spacing").getAsDouble() * factor);
        int separation = (placement.get("separation") == null) ? 1 : (int)(placement.get("separation").getAsDouble() * factor);
        System.out.println("Spacing : " + spacing + " for " + elementKey.identifier());

        if (separation >= spacing) {
            spacing = Math.max(1, spacing);
            separation = spacing - 1;
        }

        placement.addProperty("spacing", spacing);
        placement.addProperty("separation", separation);

        if (SparseStructuresCommon.config.idBasedSalt()) {
            int salt = IdBasedSalt.getSalt(elementKey.identifier().toString());
            placement.addProperty("salt", salt);
        }
    }
}