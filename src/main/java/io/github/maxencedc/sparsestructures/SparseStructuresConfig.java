package io.github.maxencedc.sparsestructures;

import java.util.List;

public class SparseStructuresConfig {
    public double spreadFactor;
    public double spreadFactor() { return this.spreadFactor; }
    public List<CustomSpreadFactors> customSpreadFactors;
    public List<CustomSpreadFactors> customSpreadFactors() { return this.customSpreadFactors; }
    public SparseStructuresConfig(double spreadFactor, List<CustomSpreadFactors> customSpreadFactors)
    {
        this.spreadFactor = spreadFactor;
        this.customSpreadFactors = customSpreadFactors;
    }
}
