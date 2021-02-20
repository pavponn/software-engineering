import profiler.Profiler;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        try {
            Profiler.setProfiledPackageName(args[0]);
            Method entryPoint = getMethodOfClass(args[1], args[2]);
            entryPoint.invoke(null, (Object) (null));
            Profiler.getProfilerInstance().printStatistics();
        } catch (Exception e) {
            System.err.println("Invalid usage, first argument is package to be profiled, second is class with class that has main entry point");
            System.err.println(e.getMessage());
        }
    }

    private static Method getMethodOfClass(String className, String method) throws NoSuchMethodException, ClassNotFoundException {
        Class<?> cls = Class.forName(className);
        return cls.getMethod(method, String[].class);
    }
}