package com.ujjwalkumar.qkart.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkart.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    private double s = 0;
    private HashMap<String, Object> order = new HashMap<>();
    private HashMap<String, Object> sellermap = new HashMap<>();
    private HashMap<String, Object> custmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> cart = new ArrayList<>();

    private LinearLayout lineardetail;
    private ImageView imageviewback;
    private TextView textviewsellername;
    private TextView textviewselleradd;
    private TextView textvieweditaddress;
    private TextView textviewaddress;
    private EditText edittextcomment;
    private TextView textviewshowhide;
    private ImageView imageviewshowhide;
    private ListView listview1;
    private TextView textviewamt;
    private LinearLayout linearplaceorder;

    private Intent in = new Intent();
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private DatabaseReference db3 = firebase.getReference("orders");
    private SharedPreferences sp1;
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        lineardetail = findViewById(R.id.lineardetail);
        imageviewback = findViewById(R.id.imageviewback);
        textviewsellername = findViewById(R.id.textviewsellername);
        textviewselleradd = findViewById(R.id.textviewselleradd);
        textvieweditaddress = findViewById(R.id.textvieweditaddress);
        textviewaddress = findViewById(R.id.textviewaddress);
        edittextcomment = findViewById(R.id.edittextcomment);
        textviewshowhide = findViewById(R.id.textviewshowhide);
        imageviewshowhide = findViewById(R.id.imageviewshowhide);
        listview1 = findViewById(R.id.listview1);
        textviewamt = findViewById(R.id.textviewamt);
        linearplaceorder = findViewById(R.id.linearplaceorder);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        cal = Calendar.getInstance();

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(30);
        linearplaceorder.setBackground(gd1);

        sellermap = new Gson().fromJson(getIntent().getStringExtra("sellermap"), new TypeToken<HashMap<String, Object>>() {}.getType());
        cart = new Gson().fromJson(getIntent().getStringExtra("itemmap"), new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType());

        custmap = new HashMap<>();
        custmap.put("custid", sp1.getString("uid", ""));
        custmap.put("name", sp1.getString("name", ""));
        custmap.put("address", sp1.getString("address", ""));
        custmap.put("lat", sp1.getString("lat", ""));
        custmap.put("lng", sp1.getString("lng", ""));
        custmap.put("contact", sp1.getString("contact", ""));

        order = new HashMap<>();
        order.put("oid", db3.push().getKey());
        order.put("selleruid", sellermap.get("uid").toString());
        order.put("custuid", sp1.getString("uid", ""));
        order.put("amt", getIntent().getStringExtra("amount"));
        order.put("status", "1");
        order.put("time", String.valueOf(cal.getTimeInMillis()));
        order.put("comment", "");
        order.put("custmap", new Gson().toJson(custmap));
        order.put("sellermap", new Gson().toJson(sellermap));
        order.put("itemmap", getIntent().getStringExtra("itemmap"));

        lineardetail.setVisibility(View.GONE);
        textviewshowhide.setText("Show details");
        textviewsellername.setText(sellermap.get("name").toString());
        textviewselleradd.setText(sellermap.get("address").toString());
        textviewaddress.setText(sp1.getString("address", ""));
        textviewamt.setText(getIntent().getStringExtra("amount"));

        listview1.setAdapter(new Listview1Adapter(cart));
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();

        imageviewback.setOnClickListener(view -> finish());

        textvieweditaddress.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), EditDetailsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(in);
        });

        edittextcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence param1, int param2, int param3, int param4) {
                final String charSeq = param1.toString();
                custmap.put("comment", charSeq);
            }

            @Override
            public void beforeTextChanged(CharSequence param1, int param2, int param3, int param4) {

            }

            @Override
            public void afterTextChanged(Editable param1) {

            }
        });

        imageviewshowhide.setOnClickListener(view -> {
            if (s == 0) {
                lineardetail.setVisibility(View.VISIBLE);
                imageviewshowhide.setImageResource(R.drawable.ic_keyboard_arrow_up_black);
                textviewshowhide.setText("Hide details");
                s = 1;
            } else {
                lineardetail.setVisibility(View.GONE);
                imageviewshowhide.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
                textviewshowhide.setText("Show details");
                s = 0;
            }
        });

        linearplaceorder.setOnClickListener(view -> {
            sp1.edit().putString("lastorder", order.get("time").toString()).apply();
            db3.child(order.get("oid").toString()).updateChildren(order);
            Toast.makeText(CartActivity.this, "Order placed successfully.", Toast.LENGTH_SHORT).show();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), OrderPlacedActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            in.putExtra("oid", order.get("oid").toString());
            in.putExtra("address", sp1.getString("address", ""));
            startActivity(in);
            finish();
        });
    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> arr) {
            data = arr;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int index) {
            return data.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = view;
            if (v == null) {
                v = inflater.inflate(R.layout.adapteritems, null);
            }

            final LinearLayout linear2 = v.findViewById(R.id.linear2);
            final TextView textviewdetail = v.findViewById(R.id.textviewdetail);
            final TextView textviewitemname = v.findViewById(R.id.textviewitemname);
            final ImageView imageviewstar = v.findViewById(R.id.imageviewstar);
            final TextView textviewitemprice = v.findViewById(R.id.textviewitemprice);
            final TextView textviewitemmrp = v.findViewById(R.id.textviewitemmrp);
            final ImageView imageviewminus = v.findViewById(R.id.imageviewminus);
            final TextView textviewqty = v.findViewById(R.id.textviewqty);
            final ImageView imageviewplus = v.findViewById(R.id.imageviewplus);

            GradientDrawable gd1 = new GradientDrawable();
            gd1.setColor(Color.parseColor("#FFFFFF"));
            gd1.setCornerRadius(30);
            linear2.setBackground(gd1);

            imageviewplus.setVisibility(View.GONE);
            imageviewminus.setVisibility(View.GONE);
            imageviewstar.setVisibility(View.GONE);
            textviewitemmrp.setVisibility(View.INVISIBLE);
            textviewitemname.setText(cart.get(position).get("name").toString());
            textviewitemprice.setText(cart.get(position).get("price").toString());
            textviewdetail.setText(cart.get(position).get("detail").toString());
            textviewqty.setText(cart.get(position).get("qty").toString());

            return v;
        }
    }

}