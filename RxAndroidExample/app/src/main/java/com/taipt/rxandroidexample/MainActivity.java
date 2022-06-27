package com.taipt.rxandroidexample;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Faker faker = new Faker(new Locale("vi"));
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<User> observable = getObservableUser();
        Observer<User> observer = getObserverUser();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Observer<User> getObserverUser() {
        return new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.d(TAG, "onNext: " + user.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    private Observable<User> getObservableUser() {
        List<User> userList = getListUsers();

//        return Observable.create(emitter -> {
//            if (userList.isEmpty()) {
//                emitter.onError(new Exception());
//            }
//            for (User user : userList) {
//                if (!emitter.isDisposed()) {
//                    emitter.onNext(user);
//                }
//            }
//            if (!emitter.isDisposed()) {
//                emitter.onComplete();
//            }
//        });
        return Observable.fromArray(getUserArray());
    }

    private User[] getUserArray() {
        User[] userArray = new User[]{new User(1, faker.name().fullName()), new User(1, faker.name().fullName()), new User(1, faker.name().fullName()), new User(1, faker.name().fullName())};
        return userArray;
    }

    private List<User> getListUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, faker.name().fullName()));
        users.add(new User(2, faker.name().fullName()));
        users.add(new User(3, faker.name().fullName()));
        users.add(new User(4, faker.name().fullName()));
        users.add(new User(5, faker.name().fullName()));
        users.add(new User(6, faker.name().fullName()));
        users.add(new User(7, faker.name().fullName()));
        users.add(new User(8, faker.name().fullName()));
        users.add(null);
        return users;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}