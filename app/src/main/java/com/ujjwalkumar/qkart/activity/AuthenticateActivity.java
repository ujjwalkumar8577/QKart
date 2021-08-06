package com.ujjwalkumar.qkart.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.ujjwalkumar.qkart.R;

import java.util.HashMap;

public class AuthenticateActivity extends AppCompatActivity {

    private boolean isloginscreen = true;

    private LinearLayout linearlogin;
    private LinearLayout linearsignup;
    private LinearLayout linear14;
    private TextView textviewforgot;
    private LinearLayout linear19;
    private LinearLayout linear20;
    private EditText edittextle;
    private EditText edittextlp;
    private ImageView imageviewlogin;
    private TextView textviewsignup;
    private LinearLayout linear4;
    private LinearLayout linear10;
    private LinearLayout linear11;
    private EditText edittextse;
    private EditText edittextsp;
    private ImageView imageviewsignup;
    private TextView textviewlogin;

    private Intent in = new Intent();
    private ObjectAnimator anix = new ObjectAnimator();
    private ObjectAnimator aniy = new ObjectAnimator();
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> auth_create_user_listener;
    private OnCompleteListener<AuthResult> auth_sign_in_listener;
    private OnCompleteListener<Void> auth_reset_password_listener;
    private SharedPreferences sp1;
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private DatabaseReference db2 = firebase.getReference("consumers");
    private ObjectAnimator ani = new ObjectAnimator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticate);

        linearlogin = findViewById(R.id.linearlogin);
        linearsignup = findViewById(R.id.linearsignup);
        linear14 = findViewById(R.id.linear14);
        textviewforgot = findViewById(R.id.textviewforgot);
        linear19 = findViewById(R.id.linear19);
        linear20 = findViewById(R.id.linear20);
        edittextle = findViewById(R.id.edittextle);
        edittextlp = findViewById(R.id.edittextlp);
        imageviewlogin = findViewById(R.id.imageviewlogin);
        textviewsignup = findViewById(R.id.textviewsignup);
        linear4 = findViewById(R.id.linear4);
        linear10 = findViewById(R.id.linear10);
        linear11 = findViewById(R.id.linear11);
        edittextse = findViewById(R.id.edittextse);
        edittextsp = findViewById(R.id.edittextsp);
        imageviewsignup = findViewById(R.id.imageviewsignup);
        textviewlogin = findViewById(R.id.textviewlogin);

        auth = FirebaseAuth.getInstance();
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
            if (sp1.getString("name", "").equals("")) {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), EditDetailsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            } else {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            }
        }

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(50);
        linear14.setBackground(gd1);
        linear4.setBackground(gd1);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#FFCCBC"));
        gd2.setCornerRadius(80);
        linear19.setBackground(gd2);
        linear20.setBackground(gd2);
        linear10.setBackground(gd2);
        linear11.setBackground(gd2);

        linearlogin.setVisibility(View.VISIBLE);
        linearsignup.setVisibility(View.GONE);

        textviewforgot.setOnClickListener(view -> {
            if (edittextle.getText().toString().equals("")) {
                Toast.makeText(AuthenticateActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
            } else {
                auth.sendPasswordResetEmail(edittextle.getText().toString()).addOnCompleteListener(auth_reset_password_listener);
            }
        });

        imageviewlogin.setOnClickListener(view -> {
            if (!edittextle.getText().toString().equals("")) {
                if (!edittextlp.getText().toString().equals("")) {
                    imageviewlogin.setImageResource(R.drawable.ic_rotate_right_black);
                    ani.setTarget(imageviewlogin);
                    ani.setPropertyName("rotation");
                    ani.setFloatValues(0, 720);
                    ani.setDuration(5000);
                    ani.setInterpolator(new LinearInterpolator());
                    ani.start();
                    sp1.edit().putString("email", edittextle.getText().toString()).apply();
                    sp1.edit().putString("password", edittextlp.getText().toString()).apply();
                    auth.signInWithEmailAndPassword(edittextle.getText().toString(), edittextlp.getText().toString()).addOnCompleteListener(AuthenticateActivity.this, auth_sign_in_listener);
                } else {
                    Toast.makeText(AuthenticateActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AuthenticateActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            }
        });

        textviewsignup.setOnClickListener(view -> {
            isloginscreen = false;
            linearsignup.setVisibility(View.VISIBLE);
            linearlogin.setAlpha(1);
            linearsignup.setAlpha(0);
            anix.setTarget(linearsignup);
            anix.setPropertyName("alpha");
            anix.setFloatValues(0, 1);
            anix.setDuration(500);
            aniy.setTarget(linearlogin);
            aniy.setPropertyName("alpha");
            aniy.setFloatValues(1, 0);
            aniy.setDuration(500);
            anix.start();
            aniy.start();
        });

        imageviewsignup.setOnClickListener(view -> {
            if (!edittextse.getText().toString().equals("")) {
                if (!edittextsp.getText().toString().equals("")) {
                    imageviewsignup.setImageResource(R.drawable.ic_rotate_right_black);
                    ani.setTarget(imageviewsignup);
                    ani.setPropertyName("rotation");
                    ani.setFloatValues(0, 720);
                    ani.setDuration(5000);
                    ani.setInterpolator(new LinearInterpolator());
                    ani.start();
                    sp1.edit().putString("email", edittextse.getText().toString()).apply();
                    sp1.edit().putString("password", edittextsp.getText().toString()).apply();
                    auth.createUserWithEmailAndPassword(edittextse.getText().toString(), edittextsp.getText().toString()).addOnCompleteListener(AuthenticateActivity.this, auth_create_user_listener);
                } else {
                    Toast.makeText(AuthenticateActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AuthenticateActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            }
        });

        textviewlogin.setOnClickListener(view -> {
            isloginscreen = true;
            linearlogin.setVisibility(View.VISIBLE);
            linearsignup.setAlpha(1);
            linearlogin.setAlpha(0);
            anix.setTarget(linearlogin);
            anix.setPropertyName("alpha");
            anix.setFloatValues(0, 1);
            anix.setDuration(500);
            aniy.setTarget(linearsignup);
            aniy.setPropertyName("alpha");
            aniy.setFloatValues(1, 0);
            aniy.setDuration(500);
            anix.start();
            aniy.start();
        });

        anix.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator param1) {

            }

            @Override
            public void onAnimationEnd(Animator param1) {
                if (!isloginscreen) {
                    linearlogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator param1) {

            }

            @Override
            public void onAnimationRepeat(Animator param1) {

            }
        });

        aniy.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator param1) {

            }

            @Override
            public void onAnimationEnd(Animator param1) {
                if (isloginscreen) {
                    linearsignup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator param1) {

            }

            @Override
            public void onAnimationRepeat(Animator param1) {

            }
        });

        auth_create_user_listener = param1 -> {
            final boolean success = param1.isSuccessful();
            final String errorMessage = param1.getException() != null ? param1.getException().getMessage() : "";
            ani.cancel();
            imageviewsignup.setRotation((float) (0));
            imageviewsignup.setImageResource(R.drawable.ic_arrow_forward_black);
            if (success) {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), EditDetailsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
                finish();
            } else {
                Toast.makeText(AuthenticateActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        };

        auth_sign_in_listener = param1 -> {
            final boolean success = param1.isSuccessful();
            final String errorMessage = param1.getException() != null ? param1.getException().getMessage() : "";
            if (success) {
                db2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Toast.makeText(AuthenticateActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else if(!task.getResult().exists()) {
                        Toast.makeText(AuthenticateActivity.this, "This email corresponds to a seller", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                    else {
                        GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        HashMap<String, Object> map = task.getResult().getValue(ind);

                        sp1.edit().putString("uid", map.get("uid").toString()).apply();
                        sp1.edit().putString("email", map.get("email").toString()).apply();
                        sp1.edit().putString("name", map.get("name").toString()).apply();
                        sp1.edit().putString("address", map.get("address").toString()).apply();
                        sp1.edit().putString("lat", map.get("lat").toString()).apply();
                        sp1.edit().putString("lng", map.get("lng").toString()).apply();
                        sp1.edit().putString("contact", map.get("contact").toString()).apply();
                        sp1.edit().putString("img", map.get("img").toString()).apply();
                        in.setAction(Intent.ACTION_VIEW);
                        in.setClass(getApplicationContext(), HomeActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(in);
                        finish();
                    }
                });

            } else {
                Toast.makeText(AuthenticateActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
            ani.cancel();
            imageviewlogin.setRotation(0);
            imageviewlogin.setImageResource(R.drawable.ic_arrow_forward_black);
        };

        auth_reset_password_listener = param1 -> {
            final boolean success = param1.isSuccessful();
            if (success) {
                Toast.makeText(AuthenticateActivity.this, "Password reset mail sent", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

}