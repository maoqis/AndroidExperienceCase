package com.maoqis.testcase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxZipErrorFragment extends Fragment {
    private static final String TAG = "RxZipErrorFragment";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_rx_zip_error_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tv_sample_thread_e).setOnClickListener(v -> {

            new Thread("abc"){
                @Override
                public void run() {

                    super.run();
                    int x = 0;
                    System.out.println(1/x);
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