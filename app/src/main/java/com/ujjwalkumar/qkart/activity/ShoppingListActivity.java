package com.ujjwalkumar.qkart.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujjwalkumar.qkart.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingListActivity extends AppCompatActivity {

    private HashMap<String, Object> mp = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmp = new ArrayList<>();

    private ImageView imageviewback;
    private LinearLayout linear1;
    private ListView listview1;
    private EditText edittext;
    private ImageView imageviewadd;

    private Intent in = new Intent();
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist);

        imageviewback = findViewById(R.id.imageviewback);
        linear1 = findViewById(R.id.linear1);
        listview1 = findViewById(R.id.listview1);
        edittext = findViewById(R.id.edittext);
        imageviewadd = findViewById(R.id.imageviewadd);
        sp1 = getSharedPreferences("info", Activity.MODE_PRIVATE);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(Color.parseColor("#FFFFFF"));
        gd1.setCornerRadius(30);
        linear1.setBackground(gd1);

        if (!sp1.getString("slist", "").equals("")) {
            lmp = new Gson().fromJson(sp1.getString("slist", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType());
        }
        refreshList();

        imageviewback.setOnClickListener(view -> finish());

        imageviewadd.setOnClickListener(view -> {
            if (!edittext.getText().toString().equals("")) {
                mp = new HashMap<>();
                mp.put("name", edittext.getText().toString());
                lmp.add(mp);
                refreshList();
                edittext.setText("");
                sp1.edit().putString("slist", new Gson().toJson(lmp)).apply();
            } else {
                Toast.makeText(ShoppingListActivity.this, "Enter item name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshList() {
        if (lmp.size() > 0) {
            listview1.setAdapter(new Listview1Adapter(lmp));
            ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
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
                v = inflater.inflate(R.layout.adaptershoppinglist, null);
            }

            final LinearLayout linear1 = v.findViewById(R.id.linear1);
            final TextView textviewname = v.findViewById(R.id.textviewname);
            final ImageView imageviewdelete = v.findViewById(R.id.imageviewdelete);

            GradientDrawable gd2 = new GradientDrawable();
            gd2.setColor(Color.parseColor("#FFCCBC"));
            gd2.setCornerRadius(30);
            linear1.setBackground(gd2);

            if (lmp.get(position).containsKey("name")) {
                textviewname.setText(lmp.get(position).get("name").toString());
            }

            imageviewdelete.setOnClickListener(view1 -> {
                lmp.remove(position);
                sp1.edit().putString("slist", new Gson().toJson(lmp)).apply();
                refreshList();
            });

            return v;
        }
    }

}