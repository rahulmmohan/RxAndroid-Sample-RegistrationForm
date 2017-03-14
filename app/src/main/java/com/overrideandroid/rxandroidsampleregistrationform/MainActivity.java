package com.overrideandroid.rxandroidsampleregistrationform;

import java.util.regex.Pattern;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


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


        InitialValueObservable<CharSequence> usernameObservable = RxTextView.textChanges(mUsernameEditText);
        InitialValueObservable<CharSequence> emailObservable = RxTextView.textChanges(mEmailEditText);

//       usernameObservable.subscribe(new Consumer<CharSequence>() {
//           @Override
//           public void accept(CharSequence charSequence) throws Exception {
//               Log.d(TAG,charSequence.toString());
//           }
//       });
//        emailObservable.subscribe(new Consumer<CharSequence>() {
//            @Override
//            public void accept(CharSequence charSequence) throws Exception {
//                Log.d(TAG,charSequence.toString());
//            }
//        });


        Observable<Boolean> isUsernameValid = usernameObservable.map(new Function<CharSequence, Boolean>() {

            @Override
            public Boolean apply(CharSequence charSequence) throws Exception {
                return charSequence.length() > 4;
            }
        });
        Observable<Boolean> isEmailValid = emailObservable.map(new Function<CharSequence, Boolean>() {

            @Override
            public Boolean apply(CharSequence charSequence) throws Exception {
                return emailPattern.matcher(charSequence).matches();
            }
        });
//
//
//        isUsernameValid.doOnNext(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean valid) throws Exception {
//                Log.d(TAG, "Username " + (valid ? "Valid" : "Invalid"));
//            }
//        }).map(new Function<Boolean, Integer>() {
//
//            @Override
//            public Integer apply(Boolean valid) throws Exception {
//                return valid?Color.WHITE:Color.RED;
//            }
//        }).subscribe(new Consumer<Integer>() {
//
//            @Override
//            public void accept(Integer color) throws Exception {
//                mUsernameEditText.setTextColor(color);
//            }
//        });
//
//        isEmailValid.doOnNext(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean valid) throws Exception {
//                Log.d(TAG, "Email " + (valid ? "Valid" : "Invalid"));
//            }
//        }).map(new Function<Boolean, Integer>() {
//
//            @Override
//            public Integer apply(Boolean valid) throws Exception {
//                return valid?Color.WHITE:Color.RED;
//            }
//        }).subscribe(new Consumer<Integer>() {
//
//            @Override
//            public void accept(Integer color) throws Exception {
//                mEmailEditText.setTextColor(color);
//            }
//        });
//

        isUsernameValid.distinctUntilChanged().doOnNext(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean valid) throws Exception {
                Log.d(TAG, "Username " + (valid ? "Valid" : "Invalid"));
            }
        }).map(new Function<Boolean, Integer>() {

            @Override
            public Integer apply(Boolean valid) throws Exception {
                return valid?Color.WHITE:Color.RED;
            }
        }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer color) throws Exception {
                mUsernameEditText.setTextColor(color);
            }
        });

        isEmailValid.distinctUntilChanged().doOnNext(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean valid) throws Exception {
                Log.d(TAG, "Email " + (valid ? "Valid" : "Invalid"));
            }
        }).map(new Function<Boolean, Integer>() {

            @Override
            public Integer apply(Boolean valid) throws Exception {
                return valid?Color.WHITE:Color.RED;
            }
        }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer color) throws Exception {
                mEmailEditText.setTextColor(color);
            }
        });

//
//
        Observable<Boolean> registerButtonObservable = Observable.combineLatest(isUsernameValid, isEmailValid, new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                return aBoolean && aBoolean2;
            }
        });

        registerButtonObservable.distinctUntilChanged().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                mRegisterButton.setEnabled(aBoolean);
            }

        });
    }
}
