# Butterknife
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

# Dagger
-dontwarn com.google.errorprone.annotations.*

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }

# JSoup
-keeppackagenames org.jsoup.nodes

# Retrofit
-dontwarn okio.*
-dontwarn retrofit2.*

# Project
-keepattributes SourceFile,LineNumberTable
-keep class com.deange.githubstatus.model.** { *; }
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
