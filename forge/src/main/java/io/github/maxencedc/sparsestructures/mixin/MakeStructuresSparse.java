package io.github.maxencedc.sparsestructures.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Decoder;
import io.github.maxencedc.sparsestructures.CustomSpreadFactors;
import io.github.maxencedc.sparsestructures.IdBasedSalt;
import io.github.maxencedc.sparsestructures.SparseStructuresCommon;
import io.github.maxencedc.sparsestructures.StructureSetsSet;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RegistryDataLoader.class)
public class MakeStructuresSparse {

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;decode(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"), method = "loadElementFromResource", locals = LocalCapture.CAPTURE_FAILHARD)
    private static <E> void loadElementFromResource(WritableRegistry<E> registry, Decoder<E> codec, RegistryOps<JsonElement> ops, ResourceKey<E> resourceKey, Resource resource, RegistrationInfo registrationInfo, CallbackInfo ci, @Local JsonElement jsonElement) {
        String string = registry.key().identifier().getPath();
        if (!string.equals("worldgen/structure_set")) return;

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject placement = jsonObject.getAsJsonObject("placement");
        if (placement.get("type").getAsString().equals("minecraft:concentric_rings")) return;

        StructureSetsSet.addStructureSet(resourceKey.identifier().toString());

        double factor = SparseStructuresCommon.config.getSpreadFactor(resourceKey, jsonObject);

        if (factor == 0) {
            placement.addProperty("frequency", 0.0);
            return;
        }

        int spacing;
        int separation;

        spacing = (placement.get("spacing") == null) ? 1 : (int)(placement.get("spacing").getAsDouble() * factor);
        separation = (placement.get("separation") == null) ? 1 : (int)(placement.get("separation").getAsDouble() * factor);
        if (separation >= spacing) {
            spacing = Math.max(1, spacing);
            separation = spacing - 1;
        }

        placement.addProperty("spacing", spacing);
        placement.addProperty("separation", separation);

        if (SparseStructuresCommon.config.idBasedSalt()) {
            int salt = IdBasedSalt.getSalt(resourceKey.identifier().toString());
            placement.addProperty("salt", salt);
        }
    }
}