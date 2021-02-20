package profiler;

import java.time.Duration;

public class MethodStats {
    private Duration callsDuration;
    private int callsCount;

    public MethodStats() {
        this(Duration.ofMillis(0), 0);
    }

    public MethodStats(Duration callDuration, int callsCount) {
        this.callsDuration = callDuration;
        this.callsCount = callsCount;
    }

    public int getCallsCount() {
        return callsCount;
    }

    public Duration getTotalCallsDuration() {
        return callsDuration;
    }

    public Duration getAverageCallsDuration() {
        return callsDuration.dividedBy(callsCount);
    }

    public void addNewCall(Duration duration) {
        callsDuration = callsDuration.plus(duration);
        callsCount++;
    }

    @Override
    public String toString() {
        return String.format("Calls number : %d\n", getCallsCount()) +
                String.format("Calls duration (total) : %d mills\n", getTotalCallsDuration().toMillis()) +
                String.format("Calls duration (average) : %d mills", getAverageCallsDuration().toMillis());
    }

}