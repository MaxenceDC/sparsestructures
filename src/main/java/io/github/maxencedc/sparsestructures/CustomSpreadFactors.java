package io.github.maxencedc.sparsestructures;

public class CustomSpreadFactors {
    public String structure;
    public double factor;

    public CustomSpreadFactors(String structure, double factor) {
        this.structure = structure;
        this.factor = factor;
    }

    public String structure() {
        return this.structure;
    }

    public double factor() {
        return this.factor;
    }
}