package de.julielab.smac;

import java.util.ArrayList;
import java.util.List;

public class FullyEvaluatedConfiguration extends ArrayList<SmacLiveRundataEntry> {
    private int pcid;
    private double avgQuality = Double.MAX_VALUE;

    public FullyEvaluatedConfiguration(List<SmacLiveRundataEntry> entries) {
        super(entries);
        if (!isEmpty()) {
            pcid = get(0).getRunInfo().getConfiguration().getPcid();
        }
    }

    public SmacParameterConfiguration getConfiguration() {
        if (!isEmpty())
            return get(0).getRunInfo().getConfiguration();
        return null;
    }

    public double getAvgQuality() {
        if (avgQuality == Double.MAX_VALUE)
            throw new IllegalStateException("The average quality has not yet been calculated. Call 'calculateAvgQuality' first.");
        return avgQuality;
    }

    public void calculateAvgQuality() {
        stream().mapToDouble(SmacLiveRundataEntry::getrQuality).average().ifPresent(avg -> avgQuality = avg);
    }

    public int getPcid() {
        return pcid;
    }

    public void setPcid(int pcid) {
        this.pcid = pcid;
    }
}
