-keepclasseswithmembernames class * {
    native <methods>;
}

-optimizationpasses 10
-overloadaggressively
-repackageclasses ''
-allowaccessmodification
-mergeinterfacesaggressively

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}