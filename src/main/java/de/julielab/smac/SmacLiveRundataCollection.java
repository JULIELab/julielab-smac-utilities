package de.julielab.smac;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a collection of live-rundata files. This is meant for the case where multiple SMAC sessions have been
 * started with the '--shared-model-mode true' parameter such that all live rundata files belong to the same search space.
 */
public class SmacLiveRundataCollection extends ArrayList<SmacLiveRundata> {
    /**
     * <p>Returns those configurations which have been evaluated in the maximum of instances and have the highest average quality score.
     * Those were most likely the incumbent as some point in the SMAC configuration phase.</p>
     * <p>The maximum number of instances is estimated by getting the maximum number of runs for a single configuration.</p>
     * @param n The number of configurations to return.
     * @return The best configurations.
     */
    public List<FullyEvaluatedConfiguration> getBestFullyEvaluatedConfigurations(int n) {
        List<FullyEvaluatedConfiguration> overallBestConfigurations = stream()
                .map(data -> data.getBestFullyEvaluatedConfigurations(n))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingDouble(FullyEvaluatedConfiguration::getAvgQuality).reversed())
                .collect(Collectors.toList());
        return overallBestConfigurations.subList(Math.max(overallBestConfigurations.size() - n, 0), overallBestConfigurations.size());
    }
}
