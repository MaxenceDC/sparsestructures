package io.github.maxencedc.sparsestructures;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SparseStructuresConfig(double spreadFactor, List<customSpreadFactors> customSpreadFactors) {
    public record customSpreadFactors(String structure, double factor) {}
}
