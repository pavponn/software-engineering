package profiler;

import static profiler.Profiler.getProfilerInstance;


public aspect ProfilerAspect {
    pointcut methodCall(): call(* *.*(..)) &&
            if (thisJoinPointStaticPart.getSignature().getDeclaringType().getPackage().toString()
                .startsWith("package " + System.getProperty("PACKAGE_TO_PROFILE") + ".") ||
                        thisJoinPointStaticPart.getSignature().getDeclaringType().getPackage().toString()
                            .equals(("package " + System.getProperty("PACKAGE_TO_PROFILE")))
             );

    before(): methodCall() {
        getProfilerInstance().methodBefore(String.format("%s.%s",
                thisJoinPointStaticPart.getSignature().getDeclaringTypeName(),
                thisJoinPointStaticPart.getSignature().getName()
        ));
    }

    after(): methodCall() {
        getProfilerInstance().methodAfter(String.format("%s.%s",
                thisJoinPointStaticPart.getSignature().getDeclaringTypeName(),
                thisJoinPointStaticPart.getSignature().getName()
        ));
    }

}