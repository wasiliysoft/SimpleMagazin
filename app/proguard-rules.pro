
# Save annotation SerializedName
# https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# GSON uses type tokens to serialize and deserialize generic types.
# # https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md
-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Kotlin suspend functions and generic signatures
# # https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md
-keepattributes Signature
-keep class kotlin.coroutines.Continuation

-assumenosideeffects
class android.util.Log {
    #public static *** e(...);
    public static *** d(...);
    #public static *** i(...);
    #public static *** v(...);
}
-dontobfuscate