package com.ujjwalkumar.qkart.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkart.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrdersActivity extends AppCompatActivity {

    private String UID = "";
    private double status = 0;
    private double lat = 0;
    private double lng = 0;
    private double sum = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> cmap = new HashMap<>();
    private HashMap<String, Object> fmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmp_orders = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();

    private ImageView imageviewback;
    private TextView textviewstatus;
    private ListView listview1;
    private TextView textviewamt;

    private Intent in = new Intent();
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private DatabaseReference db3 = firebase.getReference("orders");
    private SharedPreferences sp1;
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        imageviewback = findViewById(R.id.imageviewback);
        textviewstatus = findViewById(R.id.textviewstatus);
        listview1 = findViewById(R.id.listview1);
        textviewamt = findViewById(R.id.textviewamt);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);
        UID = sp1.getString("uid", "");
        lat = Double.parseDouble(sp1.getString("lat", ""));
        lng = Double.parseDouble(sp1.getString("lng", ""));
        loadList();

        imageviewback.setOnClickListener(view -> finish());

        listview1.setOnItemClickListener((param1, param2, param3, param4) -> {
            final int position = param3;
            tmp = filtered.get(position);
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), OrderDetailsActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            in.putExtra("map", new Gson().toJson(tmp));
            startActivity(in);
        });
    }

    private void loadList() {
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filtered = new ArrayList<>();
                lmp_orders = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        lmp_orders.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sum = 0;
                for (int i = 0; i < lmp_orders.size(); i++) {
                    if (lmp_orders.get(i).get("custuid").toString().equals(UID)) {
                        cmap = new Gson().fromJson(lmp_orders.get(i).get("sellermap").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
                        fmap = lmp_orders.get(i);
                        fmap.put("name", cmap.get("name").toString());
                        fmap.put("address", cmap.get("address").toString());
                        fmap.put("lat", cmap.get("lat").toString());
                        fmap.put("lng", cmap.get("lng").toString());
                        fmap.put("contact", cmap.get("contact").toString());
                        filtered.add(fmap);
                        sum = sum + Double.parseDouble(lmp_orders.get(i).get("amt").toString());
                    }
                }
                if (filtered.size() > 0) {
                    textviewstatus.setVisibility(View.GONE);
                    textviewamt.setText(String.valueOf(sum));
                    listview1.setAdapter(new Listview1Adapter(filtered));
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("No orders found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
                v = inflater.inflate(R.layout.adapterorders, null);
            }

            final TextView textviewname = v.findViewById(R.id.textviewname);
            final TextView textviewamt = v.findViewById(R.id.textviewamt);
            final TextView textviewtime = v.findViewById(R.id.textviewtime);
            final ImageView imageview1 = v.findViewById(R.id.imageview1);
            final ImageView imageview2 = v.findViewById(R.id.imageview2);
            final ImageView imageview3 = v.findViewById(R.id.imageview3);
            final ImageView imageview4 = v.findViewById(R.id.imageview4);
            final TextView textviewhelp = v.findViewById(R.id.textviewhelp);
            final TextView textvieworderid = v.findViewById(R.id.textvieworderid);

            cal.setTimeInMillis((long) (Double.parseDouble(filtered.get(position).get("time").toString())));
            textviewname.setText(filtered.get(position).get("name").toString());
            textviewamt.setText(filtered.get(position).get("amt").toString());
            textviewtime.setText(new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss").format(cal.getTime()));
            textvieworderid.setText(filtered.get(position).get("oid").toString());
            status = Double.parseDouble(filtered.get(position).get("status").toString());

            if (status == 1) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status0);
                imageview3.setImageResource(R.drawable.status0);
                imageview4.setImageResource(R.drawable.status0);
            }
            if (status == 2) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status2);
                imageview3.setImageResource(R.drawable.status0);
                imageview4.setImageResource(R.drawable.status0);
            }
            if (status == 3) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status2);
                imageview3.setImageResource(R.drawable.status3);
                imageview4.setImageResource(R.drawable.status0);
            }
            if (status == 4) {
                imageview1.setImageResource(R.drawable.status1);
                imageview2.setImageResource(R.drawable.status2);
                imageview3.setImageResource(R.drawable.status3);
                imageview4.setImageResource(R.drawable.status4);
            }
            textviewhelp.setOnClickListener(view1 -> {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), HelpActivity.class);
                in.putExtra("oid", filtered.get(position).get("oid").toString());
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
            });

            return v;
        }
    }

}