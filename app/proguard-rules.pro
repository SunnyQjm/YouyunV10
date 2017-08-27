# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}

-keep class com.sunny.youyun.model.** {*;}
-keep class com.sunny.youyun.wifidirect.model.** {*;}
#-keep class com.stu.tool.Module.Internet.Module.** {*;}
# Model
-keep class com.sunny.youyun.activity.file_manager.model.** {*;}
# Receiver
-keep class com.sunny.youyun.wifidirect.receiver.** {*;}

-keep class IndexRouter


# 1V4
# Keep the support library
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
# Keep the design library
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
# Keep the support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# event
-keepattributes *Annotation*
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepclassmembers class ** {@org.greenrobot.eventbus.Subscribe <methods>;}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


# ButterKnife
-keep public class * implements butterknife.Unbinder {
    public <init>(**, android.view.View);
}
-keep class butterknife.*
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn javax.annotation.**

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

# okhttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**


# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# activityrouter
-keep class com.github.mzule.activityrouter.router.** { *; }

# litepal
-keep class org.litepal.** {
    *;
}
-keep class * extends org.litepal.crud.DataSupport {
    *;
}

# 极光推送
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }