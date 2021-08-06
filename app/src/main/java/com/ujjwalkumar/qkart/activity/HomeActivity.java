package com.ujjwalkumar.qkart.activity;

import android.animation.ObjectAnimator;
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
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ujjwalkumar.qkart.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> cmap = new HashMap<>();
    private HashMap<String, Object> tmp = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmp_shops = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();

    private ImageView imageviewabout;
    private LinearLayout linearinfo;
    private LinearLayout linearloading;
    private ListView listview1;
    private EditText edittext1;
    private ImageView imageviewcart;
    private TextView textviewstatus;
    private LinearLayout linearorders;
    private LinearLayout linearslist;
    private LinearLayout linearhelp;
    private LinearLayout linearaccount;

    private final Intent inh = new Intent();
    private final FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private final DatabaseReference db1 = firebase.getReference("sellers");
    private final ObjectAnimator ani1 = new ObjectAnimator();
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        imageviewabout = findViewById(R.id.imageviewabout);
        linearinfo = findViewById(R.id.linearinfo);
        linearloading = findViewById(R.id.linearloading);
        listview1 = findViewById(R.id.listview1);
        edittext1 = findViewById(R.id.edittext1);
        imageviewcart = findViewById(R.id.imageviewcart);
        textviewstatus = findViewById(R.id.textviewstatus);
        linearorders = findViewById(R.id.linearorders);
        linearslist = findViewById(R.id.linearslist);
        linearhelp = findViewById(R.id.linearhelp);
        linearaccount = findViewById(R.id.linearaccount);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(50);
        linearinfo.setBackground(gd1);

        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        ani1.setTarget(imageviewcart);
        ani1.setPropertyName("translationX");
        ani1.setFloatValues((float) (0), (float) (700));
        ani1.setInterpolator(new BounceInterpolator());
        ani1.setDuration(15000);
        ani1.start();
        loadList();

        imageviewabout.setOnClickListener(view -> {
            inh.setAction(Intent.ACTION_VIEW);
            inh.setClass(getApplicationContext(), AboutActivity.class);
            inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inh);
        });

        listview1.setOnItemClickListener((param1, param2, param3, param4) -> {
            final int position = param3;
            tmp = filtered.get(position);
            inh.setAction(Intent.ACTION_VIEW);
            inh.setClass(getApplicationContext(), ManageItemsActivity.class);
            inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            inh.putExtra("map", new Gson().toJson(tmp));
            startActivity(inh);
        });

        edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence param1, int param2, int param3, int param4) {
                final String _charSeq = param1.toString();
                searchList(_charSeq);
            }

            @Override
            public void beforeTextChanged(CharSequence param1, int param2, int param3, int param4) {

            }

            @Override
            public void afterTextChanged(Editable param1) {

            }
        });

        linearorders.setOnClickListener(view -> {
            inh.setAction(Intent.ACTION_VIEW);
            inh.setClass(getApplicationContext(), OrdersActivity.class);
            inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inh);
        });

        linearslist.setOnClickListener(view -> {
            inh.setAction(Intent.ACTION_VIEW);
            inh.setClass(getApplicationContext(), ShoppingListActivity.class);
            inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inh);
        });

        linearhelp.setOnClickListener(view -> {
            inh.setAction(Intent.ACTION_VIEW);
            inh.setClass(getApplicationContext(), HelpActivity.class);
            inh.putExtra("oid", "");
            inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inh);
        });

        linearaccount.setOnClickListener(view -> {
            inh.setAction(Intent.ACTION_VIEW);
            inh.setClass(getApplicationContext(), AccountActivity.class);
            inh.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(inh);
        });
    }

    private void loadList() {
        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lmp_shops = new ArrayList<>();
                filtered = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        lmp_shops.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < lmp_shops.size(); i++) {
                    double dist = calculateDistance(Double.parseDouble(lmp_shops.get(i).get("lat").toString()), Double.parseDouble(lmp_shops.get(i).get("lng").toString()));
                    if (dist < Double.parseDouble(lmp_shops.get(i).get("range").toString())) {
                        cmap = lmp_shops.get(i);
                        cmap.put("distance", new DecimalFormat("0.00").format(dist));
                        filtered.add(cmap);
                    }
                }
                if (filtered.size() > 0) {
                    ani1.cancel();
                    linearloading.setVisibility(View.GONE);
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("No shop found in your locality.");
                    ani1.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private double calculateDistance(final double latc, final double lngc) {
        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lng);
        double lat2 = Math.toRadians(latc);
        double lon2 = Math.toRadians(lngc);
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + ((Math.cos(lat1 / 2) * Math.cos(lat2 / 2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2)));
        double dist = 12742 * Math.asin(Math.sqrt(a));
        return dist;
    }

    private void searchList(final String str) {
        filtered.clear();
        linearloading.setVisibility(View.VISIBLE);
        textviewstatus.setText("Loading nearby shops ...");
        ani1.start();
        for (int i = 0; i < lmp_shops.size(); i++) {
            if (lmp_shops.get(i).get("name").toString().toLowerCase().contains(str.toLowerCase())) {
                double dist = calculateDistance(Double.parseDouble(lmp_shops.get(i).get("lat").toString()), Double.parseDouble(lmp_shops.get(i).get("lng").toString()));
                if (dist < Double.parseDouble(lmp_shops.get(i).get("range").toString())) {
                    cmap = lmp_shops.get(i);
                    cmap.put("distance", new DecimalFormat("0.00").format(dist));
                    filtered.add(cmap);
                }
            }
        }
        if (filtered.size() > 0) {
            ani1.cancel();
            linearloading.setVisibility(View.GONE);
            listview1.setAdapter(new Listview1Adapter(filtered));
            ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
        } else {
            textviewstatus.setText("No result found.");
            ani1.cancel();
        }
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
                v = inflater.inflate(R.layout.adaptershops, null);
            }

            final LinearLayout linear1 = v.findViewById(R.id.linear1);
            final ImageView imageview = v.findViewById(R.id.imageview);
            final TextView textviewshopname = v.findViewById(R.id.textviewshopname);
            final TextView textviewshopaddress = v.findViewById(R.id.textviewshopaddress);
            final TextView textviewdistance = v.findViewById(R.id.textviewdistance);

            linear1.setElevation(4);
            if(filtered.get(position).containsKey("img")) {
                Glide.with(getApplicationContext())
                        .load(filtered.get(position).get("img"))
                        .placeholder(R.drawable.shopicon)
                        .into(imageview);
            }
            if (filtered.get(position).containsKey("name")) {
                textviewshopname.setText(filtered.get(position).get("name").toString());
            }
            if (filtered.get(position).containsKey("address")) {
                textviewshopaddress.setText(filtered.get(position).get("address").toString());
            }
            if (filtered.get(position).containsKey("distance")) {
                textviewdistance.setText(filtered.get(position).get("distance").toString());
            }

            return v;
        }
    }

}