

# Case for android experience

## 系统杀进程 in RxZipErrorFragment

### 子线程异常FATAL EXCEPTION，app进程发出信号9(有时候没日志)，后续系统杀进程
```
2023-11-16 10:55:50.363 23207-23239 AndroidRuntime          com.maoqis.testcase                  E  FATAL EXCEPTION: abc
Process: com.maoqis.testcase, PID: 23207
java.lang.ArithmeticException: divide by zero
at com.maoqis.testcase.RxZipErrorFragment$1.run(RxZipErrorFragment.java:49)
2023-11-16 10:55:50.372 23207-23239 OOMEventManagerFK       com.maoqis.testcase                  D  checkEventAndDumpForJE: 0
2023-11-16 10:55:53.153 23207-23230 maoqis.testcas          com.maoqis.testcase                  I  ProcessProfilingInfo new_methods=0 is saved saved_to_disk=0 resolve_classes_delay=8000
2023-11-16 10:55:53.217 23207-23239 Process                 com.maoqis.testcase                  I  Sending signal. PID: 23207 SIG: 9
```


### rxjava zip，同时有2个异常，虽然没有E级别异常堆栈，但app进程有发出信号9。

`
2023-11-16 11:03:24.949 23506-23545 System.out              com.maoqis.testcase                  I  first
2023-11-16 11:03:24.950 23506-23546 System.out              com.maoqis.testcase                  I  second
2023-11-16 11:03:24.951 23506-23545 RxZipErrorFragment      com.maoqis.testcase                  W  Observable zip: try catch 1
2023-11-16 11:03:24.951 23506-23546 System.err              com.maoqis.testcase                  W  io.reactivex.rxjava3.exceptions.UndeliverableException: The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with. Further reading: https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling | java.lang.Exception: second exception
2023-11-16 11:03:24.952 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.plugins.RxJavaPlugins.onError(RxJavaPlugins.java:367)
2023-11-16 11:03:24.952 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate$CreateEmitter.onError(ObservableCreate.java:73)
2023-11-16 11:03:24.952 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:43)
2023-11-16 11:03:24.952 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.core.Observable.subscribe(Observable.java:13102)
2023-11-16 11:03:24.952 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn$SubscribeTask.run(ObservableSubscribeOn.java:96)
2023-11-16 11:03:24.953 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.core.Scheduler$DisposeTask.run(Scheduler.java:614)
2023-11-16 11:03:24.953 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.run(ScheduledRunnable.java:65)
2023-11-16 11:03:24.953 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.internal.schedulers.ScheduledRunnable.call(ScheduledRunnable.java:56)
2023-11-16 11:03:24.953 23506-23546 System.err              com.maoqis.testcase                  W  	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
2023-11-16 11:03:24.953 23506-23546 System.err              com.maoqis.testcase                  W  	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:301)
2023-11-16 11:03:24.954 23506-23546 System.err              com.maoqis.testcase                  W  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
2023-11-16 11:03:24.954 23506-23546 System.err              com.maoqis.testcase                  W  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
2023-11-16 11:03:24.954 23506-23546 System.err              com.maoqis.testcase                  W  	at java.lang.Thread.run(Thread.java:764)
2023-11-16 11:03:24.955 23506-23546 System.err              com.maoqis.testcase                  W  Caused by: java.lang.Exception: second exception
2023-11-16 11:03:24.955 23506-23546 System.err              com.maoqis.testcase                  W  	at com.maoqis.testcase.RxZipErrorFragment.lambda$null$2(RxZipErrorFragment.java:62)
2023-11-16 11:03:24.955 23506-23546 System.err              com.maoqis.testcase                  W  	at com.maoqis.testcase.-$$Lambda$RxZipErrorFragment$d0gZw506znokShgrSfoRIiWhEH8.subscribe(Unknown Source:0)
2023-11-16 11:03:24.955 23506-23546 System.err              com.maoqis.testcase                  W  	at io.reactivex.rxjava3.internal.operators.observable.ObservableCreate.subscribeActual(ObservableCreate.java:40)
2023-11-16 11:03:24.955 23506-23546 System.err              com.maoqis.testcase                  W  	... 10 more
2023-11-16 11:03:25.191 23506-23546 Process                 com.maoqis.testcase                  I  Sending signal. PID: 23506 SIG: 9
---------------------------- PROCESS ENDED (23506) for package com.maoqis.testcase ----------------------------
2023-11-16 11:03:25.233  1100-1100  Zygote                  pid-1100                             I  Process 23506 exited due to signal (9)
`