# Sumopod AI SDK - Consumer ProGuard Rules
# These rules are automatically applied to consumers of this library.

# Keep all public API classes
-keep public class com.cikup.sumopod.ai.** { public *; }

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.cikup.sumopod.ai.model.**$$serializer { *; }
-keepclassmembers class com.cikup.sumopod.ai.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.cikup.sumopod.ai.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class com.cikup.sumopod.ai.error.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Coroutines
-dontwarn kotlinx.coroutines.**
