package com.ujjwalkumar.qkart.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.qkart.R;

public class OrderPlacedActivity extends AppCompatActivity {

    private ImageView imageviewback;
    private LinearLayout linearorder;
    private LinearLayout linearcontinue;
    private TextView textviewmyorders;
    private TextView textviewaddress;
    private TextView textvieworderid;

    private Intent in = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderplaced);

        imageviewback = findViewById(R.id.imageviewback);
        linearorder = findViewById(R.id.linearorder);
        linearcontinue = findViewById(R.id.linearcontinue);
        textviewmyorders = findViewById(R.id.textviewmyorders);
        textviewaddress = findViewById(R.id.textviewaddress);
        textvieworderid = findViewById(R.id.textvieworderid);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(30);
        linearorder.setBackground(gd1);

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#FF1744"));
        gd2.setCornerRadius(50);
        linearcontinue.setBackground(gd2);

        textviewaddress.setText(getIntent().getStringExtra("address"));
        textvieworderid.setText(getIntent().getStringExtra("oid"));

        imageviewback.setOnClickListener(_view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), ManageItemsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
            finish();
        });

        linearcontinue.setOnClickListener(_view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
            finish();
        });

        textviewmyorders.setOnClickListener(_view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), OrdersActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
            finish();
        });
    }

}