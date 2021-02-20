package profiler;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Profiler {
    private final Map<String, MethodStats> stats;
    private final Deque<Instant> callsInProgress;

    private static Profiler instance;
    private static String profilePackageName = "";
    private static final String PACKAGE_TO_PROFILE = "PACKAGE_TO_PROFILE";

    private Profiler() {
        stats = new HashMap<>();
        callsInProgress = new ArrayDeque<>();
    }

    public static Profiler getProfilerInstance() {
        if (instance == null) {
            instance = new Profiler();
        }
        return instance;
    }

    public static void setProfiledPackageName(String profiledPackage) {
        System.setProperty(PACKAGE_TO_PROFILE, profiledPackage);
        Profiler.profilePackageName = profiledPackage;
    }

    public static String getProfiledPackageName() {
       return profilePackageName;
    }

    public static String getProfiledPackage() {
        return "package " + getProfiledPackageName();
    }

    public void methodBefore(String methodName) {
        callsInProgress.push(Instant.now());
    }

    public void methodAfter(String methodName) {
        Duration callDuration = Duration.between(callsInProgress.pop(), Instant.now());
        stats.putIfAbsent(methodName, new MethodStats());
        stats.get(methodName).addNewCall(callDuration);
    }

    public void printStatistics() {
        System.out.println("Stats :");
        for (Map.Entry<String, MethodStats> entry : stats.entrySet()) {
            System.out.println();
            System.out.println("Method : " + entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}

