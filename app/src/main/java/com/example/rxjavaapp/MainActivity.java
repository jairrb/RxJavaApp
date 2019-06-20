package com.example.rxjavaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = findViewById(R.id.btnSend);
        startRxJava();
        //btnSend.setOnClickListener(v -> startRxJava());


    }

    private void startRxJava() {
        Observable.range(1, 10)
                .map(integer -> integer * 3)
                .filter(integer -> integer % 2 == 0)
                .skip(10)
                .subscribe(integer -> System.out.println(integer));

        Observable.just(10)
                .subscribe(System.out::println);

        Observable.fromIterable(Arrays.asList("Jair", "Angela", "Emily"))
                .filter(s -> s.contains("Jair"))
                .subscribe(System.out::println);

        Observable<String> stringObservable = Observable.create(emitter -> {
            try {
                emitter.onNext("Jair");
                emitter.onNext("Angela");
                emitter.onError(new Exception("ERROR"));
                emitter.onNext("Emily");
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        stringObservable.subscribe(s -> System.out.println(s)
                , throwable -> System.out.println(throwable.getMessage())
                , () -> System.out.println("Complete"));

        getUserOnServer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> System.out.println(s)
                        , t -> System.out.println(t.getMessage()));

        new Thread(()->getUserOnServer()).start();

    }

    Observable<String> getUserOnServer() {
        return Observable.create(emitter -> {
            emitter.onNext("Jair");
            emitter.onNext("Angela");
            emitter.onNext("Emily");
            emitter.onComplete();
        });
    }
}
