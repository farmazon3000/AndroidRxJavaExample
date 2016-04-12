package pl.noapps.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    final String TAG_generator = "generator";

    void debugThread(String msg){
        if(true){
            Log.d(TAG_generator, msg + " currentThread " + Thread.currentThread().getName());
        }
    }
    @RxLogObservable
    Observable<Integer> generator(int maxNum){
        List<Integer> tmpList = new ArrayList<>();
        for(int i = 1; i < maxNum; ++i){
            tmpList.add(i);
        }
        return Observable
                .from(tmpList)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        debugThread("doOnSubscribe");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noThreads();
    }

    void noThreads(){
        generator(5)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        debugThread("Shifted Up");
                        integer+=10;
                        Log.d(TAG_generator, "Shifted Up " + integer);
                        return integer;
                    }
                })
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        debugThread("Shifted Down");
                        integer-=10;
                        Log.d(TAG_generator, "Shifted Down " + integer);
                        return integer;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        debugThread("onCompleted");
                        Log.d(TAG_generator, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        debugThread("onError");
                        Log.d(TAG_generator, "onError " + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        debugThread("onNext");
                        Log.d(TAG_generator, "Received " + integer);
                    }
                });
    }
}
