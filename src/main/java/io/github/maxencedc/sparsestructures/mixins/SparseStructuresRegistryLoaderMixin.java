package io.github.maxencedc.sparsestructures.mixins;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import io.github.maxencedc.sparsestructures.CustomSpreadFactors;
import io.github.maxencedc.sparsestructures.SparseStructures;
import net.minecraft.registry.*;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

@Mixin(RegistryLoader.class)
public abstract class SparseStructuresRegistryLoaderMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"), method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", locals = LocalCapture.CAPTURE_FAILHARD)
    private static <E> void load(RegistryOps.RegistryInfoGetter registryInfoGetter, ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryRef, MutableRegistry<E> newRegistry, Decoder<E> decoder, Map<RegistryKey<?>, Exception> exceptions, CallbackInfo ci, String string, ResourceFinder resourceFinder, RegistryOps registryOps, Iterator var9, Map.Entry entry, Identifier identifier, RegistryKey registryKey, Resource resource, Reader reader, JsonElement jsonElement) {
        if (string.equals("worldgen/structure_set") && !jsonElement.getAsJsonObject().getAsJsonObject("placement").get("type").getAsString().equals("minecraft:concentric_rings")) {
            double factor = SparseStructures.config.customSpreadFactors().stream().filter(s -> {
                if (s == null) return false;
                String structure_set = registryKey.getValue().toString();
                return structure_set.equals(s.structure()) || jsonElement.getAsJsonObject().getAsJsonArray("structures").asList().stream().anyMatch(p -> p.getAsJsonObject().get("structure").getAsString().equals(s.structure()));
            }).findFirst().orElse(new CustomSpreadFactors("", SparseStructures.config.spreadFactor())).factor();
            int spacing = (int)(Math.min(jsonElement.getAsJsonObject().getAsJsonObject("placement").get("spacing").getAsDouble() * factor, 4096.0));
            int separation = (int)(Math.min(jsonElement.getAsJsonObject().getAsJsonObject("placement").get("separation").getAsDouble() * factor, 4096.0));
            if (separation >= spacing) {
                if (spacing == 0) spacing = 1;
                separation = spacing - 1;
            }
            jsonElement.getAsJsonObject().getAsJsonObject("placement").addProperty("spacing", spacing);
            jsonElement.getAsJsonObject().getAsJsonObject("placement").addProperty("separation", separation);
        }
    }
}
