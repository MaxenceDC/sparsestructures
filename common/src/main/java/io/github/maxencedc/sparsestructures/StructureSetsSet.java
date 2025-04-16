package io.github.maxencedc.sparsestructures;

import java.util.SortedSet;
import java.util.TreeSet;

public class StructureSetsSet {
    public static SortedSet<String> structureSets = new TreeSet<>();

    public static void addStructureSet(String structureSet) {
        structureSets.add(structureSet);
    }
}
