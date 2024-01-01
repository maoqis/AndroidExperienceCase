[TOC]

# Case for android experience

## 一、功能简介
### git submodule
1. clone 仓库后，需要再更新子仓库
```
git submodule update
```


## 二、 RxZipErrorFragment
rx相关异常出来经验。配置不好，会导致有未捕获的crash。
### 子线程异常FATAL EXCEPTION，app进程发出信号9(有时候没日志)，后续系统杀进程
```
15:12:54.474  E  FATAL EXCEPTION: abc
                 Process: com.maoqis.testcase, PID: 4622
                 java.lang.ArithmeticException: divide by zero
                 	at com.maoqis.testcase.feature.RxZipErrorFragment$1.run(RxZipErrorFragment.java:51)
15:12:54.485  D  checkEventAndDumpForJE: 0
15:12:54.661  I  Sending signal. PID: 4622 SIG: 9
---------------------------- PROCESS ENDED (4622) for package com.maoqis.testcase ----------------------------
15:12:54.684  E  channel '9f4d8fd com.maoqis.testcase/com.maoqis.testcase.MainActivity (server)' ~ Channel is unrecoverably broken and will be disposed!
15:12:54.685  I  Process 4622 exited due to signal (9)
```

### UncaughtExceptionHandler.uncaughtException

系统发出信号9，但没日志。需要自己提前打印日志。这样说闪退不一定需要W级别日志。这个地方都没打印日志。闪退需要发信号告诉系统

```
16:33:26.749  D  run: 这UncaughtExceptionHandler.uncaughtExceptio：系统没日志
16:33:26.907  I  Sending signal. PID: 8909 SIG: 9
```

### rxjava zip，同时有2个异常，虽然没有E级别异常堆栈，但app进程有发出信号9。

Tip： zip中每一项都需要单独处理 异常。

```
15:13:49.105  I  first
15:13:49.106  I  second
15:13:49.106  W  Observable zip: try catch 1
15:13:49.107  W  io.reactivex.rxjava3.exceptions.UndeliverableException: The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with. Further reading: https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling | java.lang.Exception: second exception
15:13:49.108  W  	at io.reactivex.rxjava3.plugins.RxJavaPlugins.onError(RxJavaPlugins.java:367)
15:13:49.108  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate$CreateEmitter.onError(ObservableCreate.java:73)
15:13:49.109  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:43)
15:13:49.109  W  	at io.reactivex.rxjava3.core.Observable.subscribe(Observable.java:13102)
15:13:49.109  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn$SubscribeTask.run(ObservableSubscribeOn.java:96)
15:13:49.109  W  	at io.reactivex.rxjava3.core.Scheduler$DisposeTask.run(Scheduler.java:614)
15:13:49.110  W  	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.run(ScheduledRunnable.java:65)
15:13:49.110  W  	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.call(ScheduledRunnable.java:56)
15:13:49.111  W  	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
15:13:49.111  W  	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:301)
15:13:49.111  W  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
15:13:49.111  W  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
15:13:49.111  W  	at java.lang.Thread.run(Thread.java:764)
15:13:49.112  W  Caused by: java.lang.Exception: second exception
15:13:49.112  W  	at com.maoqis.testcase.feature.RxZipErrorFragment.lambda$null$2(RxZipErrorFragment.java:64)
15:13:49.112  W  	at com.maoqis.testcase.-$$Lambda$RxZipErrorFragment$d0gZw506znokShgrSfoRIiWhEH8.subscribe(Unknown Source:0)
15:13:49.113  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:40)
15:13:49.113  W  	... 10 more
15:13:50.197  I  ProcessProfilingInfo new_methods=0 is saved saved_to_disk=0 resolve_classes_delay=8000

// 这个地方比较奇怪，为什么系统会弹窗？ 没发现E级别日志。弹窗打印的也是一样的日志，UndeliverableException
//此时弹出了异常信息弹窗，内容同上，但是没有 E级别日志。
//点击取消，系统发出信号9. 
---------------------------- PROCESS ENDED (4773) for package com.maoqis.testcase ----------------------------
15:13:57.965  I  Process 4773 exited due to signal (9)
```

### RxJavaPlugins.setErrorHandler 才能捕获库内部异常，subscribe中无法捕获的异常。
```
14:51:22.333  I  first
14:51:22.333  W  Observable zip: try catch 1
14:51:22.334  I  second
14:51:22.336  W  RxJavaPlugins.setErrorHandler callback: 
                 io.reactivex.rxjava3.exceptions.UndeliverableException: The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with. Further reading: https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling | java.lang.Exception: second exception
                 	at io.reactivex.rxjava3.plugins.RxJavaPlugins.onError(RxJavaPlugins.java:367)
                 	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate$CreateEmitter.onError(ObservableCreate.java:73)
                 	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:43)
                 	at io.reactivex.rxjava3.core.Observable.subscribe(Observable.java:13102)
                 	at io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn$SubscribeTask.run(ObservableSubscribeOn.java:96)
                 	at io.reactivex.rxjava3.core.Scheduler$DisposeTask.run(Scheduler.java:614)
                 	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.run(ScheduledRunnable.java:65)
                 	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.call(ScheduledRunnable.java:56)
                 	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
                 	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:301)
                 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
                 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
                 	at java.lang.Thread.run(Thread.java:764)
                 Caused by: java.lang.Exception: second exception
                 	at com.maoqis.testcase.feature.RxZipErrorFragment.lambda$null$22(RxZipErrorFragment.java:161)
                 	at com.maoqis.testcase.-$$Lambda$RxZipErrorFragment$27RZ5dbhKZ2Mx85W5oZRzvVRTCo.subscribe(Unknown Source:0)
                 	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:40)
                 	at io.reactivex.rxjava3.core.Observable.subscribe(Observable.java:13102) 
                 	at io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn$SubscribeTask.run(ObservableSubscribeOn.java:96) 
                 	at io.reactivex.rxjava3.core.Scheduler$DisposeTask.run(Scheduler.java:614) 
                 	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.run(ScheduledRunnable.java:65) 
                 	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.call(ScheduledRunnable.java:56) 
                 	at java.util.concurrent.FutureTask.run(FutureTask.java:266) 
                 	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:301) 
                 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167) 
                 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641) 
                 	at java.lang.Thread.run(Thread.java:764)

```

## 三、启动模式
### 3.1 

## 四、Glide加载9.png
[github Glide9Png](https://github.com/maoqis/Glide9Png)


## 五、模块化，git push maven

### 5.1 发布的子module的gradle文件中引入release-bintray.gradle

```
apply from: "./../release-bintray.gradle"
```

### 5.2 主发布文件
https://github.com/maoqis/Glide9Png/blob/main/release-bintray.gradle

### 5.3 local.properties 中配置秘密相关
```
//配置from local.properties
ext["signing.keyId"] = ''
ext["signing.password"] = ''
ext["signing.secretKeyRingFile"] = ''
ext["ossrhUsername"] = ''
ext["ossrhPassword"] = '' //
```
### 5.4 project gradle.properties 配置版本号、组织名

```
//配置from project gradle
version = VERSION_NAME

//配置from gradle.properties
group = GROUP
```