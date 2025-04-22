package io.github.maxencedc.sparsestructures.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import io.github.maxencedc.sparsestructures.IdBasedSalt;
import io.github.maxencedc.sparsestructures.SparseStructuresCommon;
import io.github.maxencedc.sparsestructures.StructureSetsSet;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class MakeStructuresSparse {

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"), method = "Lnet/minecraft/resources/RegistryDataLoader;loadRegistryContents(Lnet/minecraft/resources/RegistryOps$RegistryInfoLookup;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/WritableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", locals = LocalCapture.CAPTURE_FAILHARD)
    private static <E> void loadRegistryContents(RegistryOps.RegistryInfoLookup lookup, ResourceManager manager, ResourceKey key, WritableRegistry registry, Decoder decoder, Map exceptions, CallbackInfo ci, String string, FileToIdConverter filetoidconverter, RegistryOps registryops, Iterator var9, Map.Entry entry, ResourceLocation resourcelocation, ResourceKey resourceKey, Resource resource, Reader reader, JsonElement jsonElement) {
        if (!string.equals("worldgen/structure_set")) return;

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject placement = jsonObject.getAsJsonObject("placement");
        if (placement.get("type").getAsString().equals("minecraft:concentric_rings")) return;

        StructureSetsSet.addStructureSet(resourceKey.location().toString());

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
            int salt = IdBasedSalt.getSalt(resourceKey.location().toString());
            placement.addProperty("salt", salt);
        }
    }
}