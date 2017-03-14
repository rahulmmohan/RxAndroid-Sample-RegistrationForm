package com.overrideandroid.rxandroidsampleregistrationform;

import java.util.regex.Pattern;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity {

    String TAG = getClass().getName();
    private EditText mUsernameEditText;
    private EditText mEmailEditText;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameEditText = (EditText)findViewById(R.id.user_name);
        mEmailEditText = (EditText)findViewById(R.id.email);
        mRegisterButton = (Button)findViewById(R.id.register);

        final Pattern emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


        Observable<OnTextChangeEvent> usernameObservable = WidgetObservable.text(mUsernameEditText);
        Observable<OnTextChangeEvent> emailObservable = WidgetObservable.text(mEmailEditText);

//        usernameObservable .subscribe(new Action1<OnTextChangeEvent>() {
//            @Override
//            public void call(OnTextChangeEvent onTextChangeEvent) {
//                    Log.d(TAG,onTextChangeEvent.text().toString());
//            }
//        });

//        usernameObservable.filter(new Func1<OnTextChangeEvent, Boolean>() {
//            @Override
//            public Boolean call(OnTextChangeEvent onTextChangeEvent) {
//                return onTextChangeEvent.text().length()>4;
//            }
//        }).subscribe(new Action1<OnTextChangeEvent>() {
//            @Override
//            public void call(OnTextChangeEvent onTextChangeEvent) {
//                    Log.d(TAG,onTextChangeEvent.text().toString());
//            }
//        });


        Observable<Boolean> isUsernameValid = usernameObservable.map(new Func1<OnTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(OnTextChangeEvent onTextChangeEvent) {
                return onTextChangeEvent.text().length() > 4;
            }
        });
        Observable<Boolean> isEmailValid = emailObservable.map(new Func1<OnTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(OnTextChangeEvent onTextChangeEvent) {
                return emailPattern.matcher(onTextChangeEvent.text()).matches();
            }
        });


//        isUsernameValid.doOnNext(new Action1<Boolean>() {
//            @Override
//            public void call(Boolean valid) {
//                Log.d(TAG, "Username " + (valid ? "Valid" : "Invalid"));
//            }
//        }).map(new Func1<Boolean, Integer>() {
//            @Override
//            public Integer call(Boolean valid) {
//                return valid?Color.WHITE:Color.RED;
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer color) {
//                mUsernameEditText.setTextColor(color);
//            }
//        });
//
//        isEmailValid.doOnNext(new Action1<Boolean>() {
//            @Override
//            public void call(Boolean valid) {
//                Log.d(TAG, "Email " + (valid ? "Valid" : "Invalid"));
//            }
//        }).map(new Func1<Boolean, Integer>() {
//            @Override
//            public Integer call(Boolean valid) {
//                return valid?Color.WHITE:Color.RED;
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer color) {
//                mEmailEditText.setTextColor(color);
//            }
//        });

        isUsernameValid.distinctUntilChanged().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean valid) {
                Log.d(TAG, "Username " + (valid ? "Valid" : "Invalid"));
            }
        }).map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean valid) {
                return valid?Color.WHITE:Color.RED;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer color) {
                mUsernameEditText.setTextColor(color);
            }
        });

        isEmailValid.distinctUntilChanged().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean valid) {
                Log.d(TAG, "Email " + (valid ? "Valid" : "Invalid"));
            }
        }).map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean valid) {
                return valid?Color.WHITE:Color.RED;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer color) {
                mEmailEditText.setTextColor(color);
            }
        });


        Observable<Boolean> registerButtonObservable = Observable.combineLatest(isUsernameValid, isEmailValid, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean && aBoolean2;
            }
        });

        registerButtonObservable.distinctUntilChanged().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                    mRegisterButton.setEnabled(aBoolean);
            }
        });
    }
}
