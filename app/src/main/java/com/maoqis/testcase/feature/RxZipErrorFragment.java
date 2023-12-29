package com.maoqis.testcase.feature;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.maoqis.testcase.R;
import com.github.maoqis.android.base.component.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxZipErrorFragment extends BaseFragment {
    private static final String TAG = "RxZipErrorFragment";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getRLayout() {
        return R.layout.activity_rx_zip_error_test;
    }


    @Override
    protected void onInitView(View view) {
        view.findViewById(R.id.tv_on_error_again).setOnClickListener(v -> {
            Observable.just(1).subscribeOn(Schedulers.io()).map(t -> {
                throw new Exception("test, map throw a Exception");
            }).onErrorReturn(throwable -> {
                Log.d(TAG, "onErrorReturn,  throw a Exception again");
                throw  new Exception("onErrorReturn,  throw a Exception again");
            }).subscribe(t -> {
                Log.d(TAG, "subscribe: t=" + t);
            }, e -> {
                Log.w(TAG, "subscribe", e);
            });
        });

        view.findViewById(R.id.tv_sample_thread_e).setOnClickListener(v -> {

            new Thread("abc") {
                @Override
                public void run() {

                    super.run();
                    int x = 0;
                    System.out.println(1 / x);
                }
            }.start();
        });

        view.findViewById(R.id.tv_thread_uncaught).setOnClickListener(v -> {

            new Thread("tv_thread_uncaught") {
                @Override
                public void run() {

                    super.run();
                    Log.d(TAG, "run: 这信号9：系统没日志");
                    UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();
                    uncaughtExceptionHandler.uncaughtException(this, new Exception("tv_thread_uncaught"));
                }
            }.start();
        });

        view.findViewById(R.id.tv_observable).setOnClickListener(v -> {
            Observable first = Observable.create(e -> {
                System.out.println("first");
                throw new Exception("first exception");
            }).subscribeOn(Schedulers.io());

            Observable second = Observable.create(e -> {
                System.out.println("second");
                throw new Exception("second exception");
            }).subscribeOn(Schedulers.io());


            List<Observable<?>> observableList = new ArrayList<>();
            observableList.add(first);
            observableList.add(second);

            Observable.zip(observableList, objects -> "result")
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        System.out.println("result");
                    }, e -> {
                        Log.w(TAG, "Observable zip: try catch 1");
                    });
        });

        view.findViewById(R.id.tv_observable_try_catch).setOnClickListener(v -> {
            Disposable[] disposables = new Disposable[1];
            Observable first = Observable.create(e -> {
                System.out.println("first");
                throw new Exception("first exception");
            }).subscribeOn(Schedulers.io()).onErrorReturnItem(-1);
            ;

            Observable second = Observable.create(e -> {
                System.out.println("second");
                if (disposables[0].isDisposed()) {
                    Log.w(TAG, "onViewCreated: disposables[0].isDisposed()");
                }
                throw new Exception("second exception");
            }).subscribeOn(Schedulers.io()).onErrorReturnItem(-1);


            List<Observable<?>> observableList = new ArrayList<>();
            observableList.add(first);
            observableList.add(second);

            disposables[0] = Observable.zip(observableList, objects -> "result")
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        System.out.println("result");
                    }, e -> {
                        Log.w(TAG, "Observable zip: try catch 1");
                    });
        });

        view.findViewById(R.id.tv_observable_one_exception).setOnClickListener(v -> {
            Observable first = Observable.create(e -> {
                System.out.println("first");
                throw new Exception("first exception");
            }).subscribeOn(Schedulers.io());

            Observable second = Observable.create(e -> {
                System.out.println("second");
//                throw new Exception("second exception");
            }).subscribeOn(Schedulers.io());


            List<Observable<?>> observableList = new ArrayList<>();
            observableList.add(first);
            observableList.add(second);

            Observable.zip(observableList, objects -> "result")
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        System.out.println("result");
                    }, e -> {
                        Log.w(TAG, "Observable zip: try catch 1");
                    });
        });

        view.findViewById(R.id.tv_observable_error_handler).setOnClickListener(v -> {
            RxJavaPlugins.setErrorHandler(e -> {
                Log.w(TAG, "RxJavaPlugins.setErrorHandler callback: ", e);
            });
            Observable first = Observable.create(e -> {
                System.out.println("first");
                throw new Exception("first exception");
            }).subscribeOn(Schedulers.io());

            Observable second = Observable.create(e -> {
                System.out.println("second");
                throw new Exception("second exception");
            }).subscribeOn(Schedulers.io());


            List<Observable<?>> observableList = new ArrayList<>();
            observableList.add(first);
            observableList.add(second);

            Observable.zip(observableList, objects -> "result")
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        System.out.println("result");
                    }, e -> {
                        Log.w(TAG, "Observable zip: try catch 1");
                    });
        });


        view.findViewById(R.id.tv_flow).setOnClickListener(v -> {
            Flowable first = Flowable.create(s -> {
                        System.out.println("first");
                        throw new Exception("first exception");
                    }, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io());

            Flowable second = Flowable.create(s -> {
                        System.out.println("second");
                        throw new Exception("second exception");
                    }, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io());


            List<Flowable<?>> observableList = new ArrayList<>();
            observableList.add(first);
            observableList.add(second);

            Flowable.zip(observableList, objects -> "result")
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        System.out.println("result");
                    }, e -> {
                        Log.w(TAG, "Flow able zip: try catch ");
                    });

        });

        view.findViewById(R.id.tv_flow_one_exception).setOnClickListener(v -> {//11.0.2还会出现
            Flowable first = Flowable.create(s -> {
                        System.out.println("first");
                        throw new Exception("first exception");
                    }, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io());

            Flowable second = Flowable.create(s -> {
                        System.out.println("second");
//                throw new Exception("second exception");
                    }, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io());


            List<Flowable<?>> observableList = new ArrayList<>();
            observableList.add(first);
            observableList.add(second);

            Flowable.zip(observableList, objects -> "result")
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        System.out.println("result");
                    }, e -> {
                        Log.w(TAG, "Flow able zip: try catch ");
                    });

        });
    }
}