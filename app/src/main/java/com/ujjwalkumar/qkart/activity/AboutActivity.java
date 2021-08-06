package com.ujjwalkumar.qkart.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.qkart.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView imageviewback;
    private LinearLayout linear2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        imageviewback = findViewById(R.id.imageviewback);
        linear2 = findViewById(R.id.linear2);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFCCBC"));
        gd1.setCornerRadius(30);
        linear2.setBackground(gd1);

        imageviewback.setOnClickListener(view -> finish());
    }
}