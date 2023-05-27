package dev.dubhe.curtain.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanExecution {
    private final Map<Long, List<Executed>> executedMap;

    public PlanExecution() {
        this.executedMap = new HashMap<>();
    }

    public void post(long time, Executed executed) {
        List<Executed> executedList = this.executedMap.getOrDefault(time, new ArrayList<>());
        executedList.add(executed);
        this.executedMap.put(time, executedList);
    }

    public void execute(long time) {
        List<Executed> executedList = this.executedMap.getOrDefault(time, new ArrayList<>());
        this.executedMap.remove(time);
        for (Executed executed : executedList) {
            executed.execute(time);
        }
    }

    @FunctionalInterface
    public interface Executed {
        void execute(long time);
    }
}
