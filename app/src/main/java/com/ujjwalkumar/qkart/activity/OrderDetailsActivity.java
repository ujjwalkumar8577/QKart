package com.ujjwalkumar.qkart.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkart.R;
import com.ujjwalkumar.qkart.utility.GoogleMapController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {

    private double lat = 0;
    private double lng = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> nmp = new HashMap<>();
    private HashMap<String, Object> customer = new HashMap<>();
    private HashMap<String, Object> cmp = new HashMap<>();
    private ArrayList<HashMap<String, Object>> item = new ArrayList<>();

    private ImageView imageviewback;
    private TextView textviewname;
    private TextView textviewaddress;
    private ImageView imageviewcall;
    private ImageView imageviewlocate;
    private MapView mapview1;
    private GoogleMapController mapview1_controller;
    private ImageView imageview1;
    private ImageView imageview2;
    private ImageView imageview3;
    private ImageView imageview4;
    private ListView listview1;
    private LinearLayout linearreview;
    private EditText edittext1;
    private TextView textviewadd;
    private TextView textviewamt;

    private Intent in = new Intent();
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private DatabaseReference db3 = firebase.getReference("orders");
    private DatabaseReference db5 = firebase.getReference("reviews");
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetails);

        imageviewback = findViewById(R.id.imageviewback);
        textviewname = findViewById(R.id.textviewname);
        textviewaddress = findViewById(R.id.textviewaddress);
        imageviewcall = findViewById(R.id.imageviewcall);
        imageviewlocate = findViewById(R.id.imageviewlocate);
        imageview1 = findViewById(R.id.imageview1);
        imageview2 = findViewById(R.id.imageview2);
        imageview3 = findViewById(R.id.imageview3);
        imageview4 = findViewById(R.id.imageview4);
        listview1 = findViewById(R.id.listview1);
        linearreview = findViewById(R.id.linearreview);
        edittext1 = findViewById(R.id.edittext1);
        textviewadd = findViewById(R.id.textviewadd);
        textviewamt = findViewById(R.id.textviewamt);
        mapview1 = findViewById(R.id.mapview1);
        mapview1.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1000);
        }

        tmp = new Gson().fromJson(getIntent().getStringExtra("map"), new TypeToken<HashMap<String, Object>>() {}.getType());
        item = new Gson().fromJson(tmp.get("itemmap").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType());
        lat = Double.parseDouble(tmp.get("lat").toString());
        lng = Double.parseDouble(tmp.get("lng").toString());
        textviewname.setText(tmp.get("name").toString());
        textviewaddress.setText(tmp.get("address").toString());
        textviewamt.setText(tmp.get("amt").toString());
        linearreview.setVisibility(View.GONE);

        if ((Integer.parseInt(tmp.get("status").toString()) > 1) || (Integer.parseInt(tmp.get("status").toString()) == 1)) {
            imageview1.setImageResource(R.drawable.status1);
        }
        if ((Integer.parseInt(tmp.get("status").toString()) > 2) || (Integer.parseInt(tmp.get("status").toString()) == 2)) {
            imageview2.setImageResource(R.drawable.status2);
        }
        if ((Integer.parseInt(tmp.get("status").toString()) > 3) || (Integer.parseInt(tmp.get("status").toString()) == 3)) {
            imageview3.setImageResource(R.drawable.status3);
        }
        if ((Integer.parseInt(tmp.get("status").toString()) > 4) || (Integer.parseInt(tmp.get("status").toString()) == 4)) {
            imageview4.setImageResource(R.drawable.status4);
            edittext1.setText(tmp.get("comment").toString());
            if (!tmp.get("comment").toString().equals("")) {
                textviewadd.setText("Edit Review");
            }
            linearreview.setVisibility(View.VISIBLE);
        }
        listview1.setAdapter(new Listview1Adapter(item));
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();

        imageviewback.setOnClickListener(view -> finish());

        imageviewcall.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_CALL);
            in.setData(Uri.parse("tel:".concat(tmp.get("contact").toString())));
            startActivity(in);
        });

        imageviewlocate.setOnClickListener(view -> {
            in.setAction(Intent.ACTION_VIEW);
            in.setData(Uri.parse("google.navigation:q=".concat(tmp.get("lat").toString().concat(",".concat(tmp.get("lng").toString())))));
            startActivity(in);
        });

        mapview1_controller = new GoogleMapController(mapview1, _googleMap -> {
            mapview1_controller.setGoogleMap(_googleMap);
            mapview1_controller.moveCamera(lat, lng);
            mapview1_controller.zoomTo(15);
            mapview1_controller.addMarker("id", lat, lng);
            mapview1_controller.setMarkerIcon("id", R.drawable.ic_location_on_black);
        });

        textviewadd.setOnClickListener(view -> {
            if (!edittext1.getText().toString().equals("")) {
                cal = Calendar.getInstance();
                cmp = new HashMap<>();
                cmp.put("amt", tmp.get("amt").toString());
                cmp.put("comment", edittext1.getText().toString());
                cmp.put("custmap", tmp.get("custmap").toString());
                cmp.put("custuid", tmp.get("custuid").toString());
                cmp.put("itemmap", tmp.get("itemmap").toString());
                cmp.put("oid", tmp.get("oid").toString());
                cmp.put("sellermap", tmp.get("sellermap").toString());
                cmp.put("selleruid", tmp.get("selleruid").toString());
                cmp.put("status", tmp.get("status").toString());
                cmp.put("time", tmp.get("time").toString());
                db3.child(tmp.get("oid").toString()).updateChildren(cmp);
                customer = new Gson().fromJson(tmp.get("custmap").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
                nmp = new HashMap<>();
                nmp.put("sellerid", tmp.get("selleruid").toString());
                nmp.put("oid", tmp.get("oid").toString());
                nmp.put("name", customer.get("name").toString());
                nmp.put("time", String.valueOf(cal.getTimeInMillis()));
                nmp.put("comment", edittext1.getText().toString());
                db5.child(tmp.get("oid").toString()).updateChildren(nmp);
                Toast.makeText(OrderDetailsActivity.this, "Review updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview1.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapview1.onResume();
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
            textviewitemname.setText(item.get(position).get("name").toString());
            textviewitemprice.setText(item.get(position).get("price").toString());
            textviewdetail.setText(item.get(position).get("detail").toString());
            textviewqty.setText(item.get(position).get("qty").toString());

            return v;
        }
    }

}