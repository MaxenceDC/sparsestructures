package io.github.maxencedc.sparsestructures;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public class SparseStructuresConfig {
    private final double spreadFactor;
    public double spreadFactor() { return this.spreadFactor; }

    private final boolean idBasedSalt;
    public boolean idBasedSalt() { return this.idBasedSalt; }

    private final List<CustomSpreadFactors> customSpreadFactors;
    public List<CustomSpreadFactors> customSpreadFactors() { return this.customSpreadFactors; }

    public SparseStructuresConfig(double spreadFactor, boolean idBasedSalt, List<CustomSpreadFactors> customSpreadFactors)
    {
        this.spreadFactor = spreadFactor;
        this.idBasedSalt = idBasedSalt;
        this.customSpreadFactors = customSpreadFactors;
    }

    public double getSpreadFactor(ResourceKey resourceKey, JsonObject jsonObject) {
        double factor = SparseStructuresCommon.config.spreadFactor();
        for (CustomSpreadFactors s : this.customSpreadFactors) {
            if (s == null) continue;
            String structure_set = resourceKey.identifier().toString();
            String structure = s.structure();
            if (structure_set.equals(structure) || jsonObject.getAsJsonArray("structures").asList().stream().anyMatch(p -> p.getAsJsonObject().get("structure").getAsString().equals(structure))) {
                factor = s.factor();
                break;
            }
        }
        return factor;
    }
}
