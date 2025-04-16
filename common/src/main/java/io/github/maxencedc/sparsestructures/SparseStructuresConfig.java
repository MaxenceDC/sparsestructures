package io.github.maxencedc.sparsestructures;

import java.util.List;

public class SparseStructuresConfig {
    private final double spreadFactor;
    public double spreadFactor() { return this.spreadFactor; }

    private final boolean idBasedSalt;
    public boolean idBasedSalt() { return this.idBasedSalt; }

    public List<CustomSpreadFactors> customSpreadFactors;
    public List<CustomSpreadFactors> customSpreadFactors() { return this.customSpreadFactors; }

    public SparseStructuresConfig(double spreadFactor, boolean idBasedSalt, List<CustomSpreadFactors> customSpreadFactors)
    {
        this.spreadFactor = spreadFactor;
        this.idBasedSalt = idBasedSalt;
        this.customSpreadFactors = customSpreadFactors;
    }
}
