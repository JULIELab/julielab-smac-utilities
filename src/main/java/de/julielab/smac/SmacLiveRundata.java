package de.julielab.smac;

import java.util.*;
import java.util.stream.Collectors;

public class SmacLiveRundata extends ArrayList<SmacLiveRundataEntry> {
    public SmacLiveRundataEntry getEntryWithBestScore() {
        SmacLiveRundataEntry bestEntry = null;
        for (SmacLiveRundataEntry entry : this) {
            if (bestEntry == null || bestEntry.getrQuality() > entry.getrQuality())
                bestEntry = entry;
        }
        return bestEntry;
    }

    public List<SmacLiveRundataEntry> getEntriesWithBestScore(int n) {
        List<SmacLiveRundataEntry> entries = new ArrayList<>(this);
        // We sort reversed because SMAC minimizes the objective, this lower scores are better
        Collections.sort(entries, Comparator.comparingDouble(SmacLiveRundataEntry::getrQuality).reversed());
        return entries.subList(Math.max(entries.size() - n, 0), entries.size());
    }

    /**
     * <p>Returns those configurations which have been evaluated in the maximum of instances and have the highest average quality score.
     * Those were most likely the incumbent as some point in the SMAC configuration phase.</p>
     * <p>The maximum number of instances is estimated by getting the maximum number of runs for a single configuration.</p>
     * @param n The number of configurations to return.
     * @return The best configurations.
     */
    public List<FullyEvaluatedConfiguration> getBestFullyEvaluatedConfigurations(int n) {
        Map<Integer, List<SmacLiveRundataEntry>> pcid2runs = stream().collect(Collectors.groupingBy(e -> e.getRunInfo().getConfiguration().getPcid()));
        OptionalInt maxRunsPerConfigOpt = pcid2runs.keySet().stream().mapToInt(pcid -> pcid2runs.get(pcid).size()).max();
        List<FullyEvaluatedConfiguration> fullyEvaluatedConfigurations = new ArrayList<>();
        if (maxRunsPerConfigOpt.isPresent()) {
            int maxRunsPerConfig = maxRunsPerConfigOpt.getAsInt();
            for (List<SmacLiveRundataEntry> entries : pcid2runs.values()) {
                if (entries.size() == maxRunsPerConfig) {
                    FullyEvaluatedConfiguration evaluatedConfiguration = new FullyEvaluatedConfiguration(entries);
                    evaluatedConfiguration.calculateAvgQuality();
                    fullyEvaluatedConfigurations.add(evaluatedConfiguration);
                }
            }

        }
        // We sort reversed because SMAC minimizes the objective, this lower scores are better
        Collections.sort(fullyEvaluatedConfigurations, Comparator.comparingDouble(FullyEvaluatedConfiguration::getAvgQuality).reversed());
        return fullyEvaluatedConfigurations.subList(Math.max(fullyEvaluatedConfigurations.size() - n, 0), fullyEvaluatedConfigurations.size());
    }
}
