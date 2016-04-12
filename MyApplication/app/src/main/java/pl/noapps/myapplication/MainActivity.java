package pl.noapps.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

public class MainActivity extends AppCompatActivity {

    @RxLogObservable
    Observable<String> createObservableMethod(final String name){
        Log.d("createObservable " + name, "create " + Thread.currentThread().getName());
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.d("createObservable " + name, "create call " + Thread.currentThread().getName());
                subscriber.onNext(Thread.currentThread().getName());
                subscriber.onCompleted();
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                Log.d("createObservable " + name, "create doOnSubscribe " + Thread.currentThread().getName());
            }
        });
    }

    @RxLogSubscriber
    class MySubscriber extends Subscriber<String>{

        String loglog;

        public MySubscriber(String loglog) {
            this.loglog = loglog;
        }

        @Override
        public void onCompleted() {
            Log.d("createObservable " + loglog, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.d("createObservable " + loglog, "onError " + e);
        }

        @Override
        public void onNext(String s) {
            Log.d("createObservable " + loglog, "onNext this thread " + Thread.currentThread().getName() + " Observable " + s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> obsN3 = createObservableMethod(">3<");
        obsN3.subscribe(new MySubscriber(">3a<"));
    }
}
