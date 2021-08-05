package com.ujjwalkumar.qkart.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;

public class ManageItemsActivity extends AppCompatActivity {

    private String selleruid = "";
    private double s = 0;
    private double amt = 0;
    private HashMap<String, Object> tmp = new HashMap<>();
    private HashMap<String, Object> seller = new HashMap<>();
    private HashMap<String, Object> itm = new HashMap<>();
    private HashMap<String, Object> tmp1 = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmpitems = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> cart = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> slist = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> lmpitems1 = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> filtered1 = new ArrayList<>();

    private ImageView imageviewback;
    private ListView listviewitems;
    private TextView textviewshopname;
    private TextView textviewaddress;
    private TextView textviewcontact;
    private LinearLayout linearreview;
    private ImageView imageviewtoggle;
    private TextView textviewnoreview;
    private TextView textviewstatus;
    private ListView listviewreviews;
    private TextView textviewamt;
    private LinearLayout linearproceed;

    private Intent in = new Intent();
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private DatabaseReference db4 = firebase.getReference("items");
    private DatabaseReference db5 = firebase.getReference("reviews");
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageitems);

        imageviewback = findViewById(R.id.imageviewback);
        textviewstatus = findViewById(R.id.textviewstatus);
        listviewitems = findViewById(R.id.listviewitems);
        textviewshopname = findViewById(R.id.textviewshopname);
        textviewaddress = findViewById(R.id.textviewaddress);
        textviewcontact = findViewById(R.id.textviewcontact);
        linearreview = findViewById(R.id.linearreview);
        imageviewtoggle = findViewById(R.id.imageviewtoggle);
        textviewnoreview = findViewById(R.id.textviewnoreview);
        listviewreviews = findViewById(R.id.listviewreviews);
        textviewamt = findViewById(R.id.textviewamt);
        linearproceed = findViewById(R.id.linearproceed);

        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FF1744"));
        gd1.setCornerRadius(30);

        linearproceed.setBackground(gd1);
        if (!sp1.getString("slist", "").equals("")) {
            slist = new Gson().fromJson(sp1.getString("slist", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType());
        }
        linearreview.setVisibility(View.GONE);
        imageviewtoggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
        s = 0;
        seller = new Gson().fromJson(getIntent().getStringExtra("map"), new TypeToken<HashMap<String, Object>>() {}.getType());
        amt = 0;
        selleruid = seller.get("uid").toString();
        textviewshopname.setText(seller.get("name").toString());
        textviewaddress.setText(seller.get("address").toString());
        textviewcontact.setText(seller.get("contact").toString());

        listviewitems.setAdapter(new ListviewitemsAdapter(filtered));
        loadList();

        listviewreviews.setAdapter(new ListviewreviewsAdapter(filtered1));
        loadReviews();

        imageviewback.setOnClickListener(view -> finish());

        imageviewtoggle.setOnClickListener(view -> {
            if (s == 0) {
                linearreview.setVisibility(View.VISIBLE);
                imageviewtoggle.setImageResource(R.drawable.ic_keyboard_arrow_up_black);
                s = 1;
            } else {
                linearreview.setVisibility(View.GONE);
                imageviewtoggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
                s = 0;
            }
        });

        linearproceed.setOnClickListener(view -> {
            if (amt > 0) {
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(getApplicationContext(), CartActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                in.putExtra("itemmap", new Gson().toJson(cart));
                in.putExtra("sellermap", getIntent().getStringExtra("map"));
                in.putExtra("amount", String.valueOf(amt));
                startActivity(in);
            } else {
                Toast.makeText(ManageItemsActivity.this, "No item added in cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadList() {
        db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lmpitems = new ArrayList<>();
                filtered = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        lmpitems.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < lmpitems.size(); i++) {
                    if (lmpitems.get(i).get("sellerid").toString().equals(selleruid) && lmpitems.get(i).get("status").toString().equals("1")) {
                        tmp = lmpitems.get(i);
                        tmp.put("qty", "0");
                        filtered.add(tmp);
                    }
                }
                if (filtered.size() > 0) {
                    textviewstatus.setVisibility(View.GONE);
                    ((BaseAdapter) listviewitems.getAdapter()).notifyDataSetChanged();
                } else {
                    textviewstatus.setText("This seller has not added any items yet.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAmount(final double amt) {
        textviewamt.setText(String.valueOf(amt));
    }

    private boolean checkRecommendation(final String name, final String detail) {
        for (int i = 0; i < slist.size(); i++) {
            if (name.toLowerCase().contains(slist.get(i).get("name").toString().toLowerCase()) || detail.toLowerCase().contains(slist.get(i).get("name").toString().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void loadReviews() {
        db5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lmpitems1 = new ArrayList<>();
                filtered1 = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        HashMap<String, Object> map = data.getValue(ind);
                        lmpitems1.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < lmpitems1.size(); i++) {
                    if (lmpitems1.get(i).get("sellerid").toString().equals(selleruid)) {
                        tmp1 = lmpitems1.get(i);
                        filtered1.add(tmp1);
                    }
                }
                if (filtered1.size() > 0) {
                    ((BaseAdapter) listviewitems.getAdapter()).notifyDataSetChanged();
                    textviewnoreview.setText(String.valueOf((long) (filtered1.size())).concat(" reviews"));
                } else {
                    textviewnoreview.setText("No reviews yet");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class ListviewitemsAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> data;

        public ListviewitemsAdapter(ArrayList<HashMap<String, Object>> arr) {
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
            final LinearLayout linearop1 = v.findViewById(R.id.linearop1);
            final ImageView imageviewminus = v.findViewById(R.id.imageviewminus);
            final TextView textviewqty = v.findViewById(R.id.textviewqty);
            final ImageView imageviewplus = v.findViewById(R.id.imageviewplus);

            GradientDrawable gd2 = new GradientDrawable();
            gd2.setColor(Color.parseColor("#FFFFFF"));
            gd2.setCornerRadius(30);
            linear2.setBackground(gd2);

            GradientDrawable gd3 = new GradientDrawable();
            gd3.setStroke(4, Color.parseColor("#FF1744"));
            gd3.setCornerRadius(10);
            linearop1.setBackground(gd3);

            imageviewstar.setVisibility(View.GONE);
            textviewitemname.setText(filtered.get(position).get("name").toString());
            textviewitemprice.setText(filtered.get(position).get("price").toString());
            textviewdetail.setText(filtered.get(position).get("detail").toString());
            if (!filtered.get(position).get("price").toString().equals(filtered.get(position).get("mrp").toString())) {
                textviewitemmrp.setText(filtered.get(position).get("mrp").toString());
                textviewitemmrp.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textviewitemmrp.setVisibility(View.INVISIBLE);
            }
            boolean isRecommended = checkRecommendation(filtered.get(position).get("name").toString(), filtered.get(position).get("detail").toString());
            if (isRecommended) {
                imageviewstar.setVisibility(View.VISIBLE);
            }

            imageviewplus.setOnClickListener(view12 -> {
                itm = filtered.get(position);
                textviewqty.setText(String.valueOf((long) (Double.parseDouble(textviewqty.getText().toString()) + 1)));
                itm.put("qty", textviewqty.getText().toString());
                amt = amt + Double.parseDouble(filtered.get(position).get("price").toString());
                setAmount(amt);

                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).get("id").toString().equals(filtered.get(position).get("id").toString())) {
                        cart.remove(i);
                        break;
                    }
                }
                cart.add(itm);
            });

            imageviewminus.setOnClickListener(view1 -> {
                if (Double.parseDouble(textviewqty.getText().toString()) > 0) {
                    itm = filtered.get(position);
                    textviewqty.setText(String.valueOf((long) (Double.parseDouble(textviewqty.getText().toString()) - 1)));
                    itm.put("qty", textviewqty.getText().toString());
                    amt = amt - Double.parseDouble(filtered.get(position).get("price").toString());
                    setAmount(amt);

                    for (int i = 0; i < cart.size(); i++) {
                        if (cart.get(i).get("id").toString().equals(filtered.get(position).get("id").toString())) {
                            cart.remove(i);
                            break;
                        }
                    }
                    if (Double.parseDouble(textviewqty.getText().toString()) > 0) {
                        cart.add(itm);
                    }
                }
            });

            return v;
        }
    }

    public class ListviewreviewsAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> data;

        public ListviewreviewsAdapter(ArrayList<HashMap<String, Object>> arr) {
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
                v = inflater.inflate(R.layout.adapterreviews, null);
            }

            final TextView textviewname = v.findViewById(R.id.textviewname);
            final TextView textviewreview = v.findViewById(R.id.textviewreview);

            textviewname.setText(filtered1.get(position).get("name").toString());
            textviewreview.setText(filtered1.get(position).get("comment").toString());

            return v;
        }
    }

}