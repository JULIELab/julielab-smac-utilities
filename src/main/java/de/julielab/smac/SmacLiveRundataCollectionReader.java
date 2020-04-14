package de.julielab.smac;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a directory of live-rundata files. This is meant for the case where multiple SMAC sessions have been
 * started with the '--shared-model-mode true' parameter such that all live rundata in the directory
 * belong to the same search space.
 */
public class SmacLiveRundataCollectionReader {
    public SmacLiveRundataCollection read(File directory) throws IOException {
        SmacLiveRundataCollection ret = new SmacLiveRundataCollection();
        SmacLiveRundataReader reader = new SmacLiveRundataReader();
        File[] files = directory.listFiles((dir, name) -> name.contains("live-rundata-") && name.endsWith(".json"));
        for (File file : files) {
            SmacLiveRundata rundata = reader.read(file);
            ret.add(rundata);
        }
        return ret;
    }
}
