package com.example.pump;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView mDate, mCurrency,mValue;
    String coin = "BTC";
    String value = "USDT";
    private RadioGroup rdoGroup;
    private RadioButton radioButton;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDate=findViewById(R.id.mDate);
        mCurrency=findViewById(R.id.mCurrency);
        mValue=findViewById(R.id.mValue);
        rdoGroup = findViewById(R.id.rdoGroup);
        btnRefresh = findViewById(R.id.btnRefresh);

        rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=findViewById(checkedId);
                switch(radioButton.getId()){
                    case R.id.rBTC:
                        value="BTC";
                        update();
                        break;
                    case R.id.rUSDT:
                        value="USDT";
                        update();
                        break;
                    case R.id.rETH:
                        value="ETH";
                        update();
                        break;





                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recherche,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Write the binance abbreviation of the currency");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()

        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                coin=query;

                update();

                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                if(getCurrentFocus() !=null){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    public void update(){
        String url="https://api.binance.com/api/v3/ticker/price?symbol="+coin+value;
        mCurrency.setText(coin+" to "+value);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double valueC = response.getDouble("price");
                    String value=String.valueOf(valueC);
                    Log.d("Tag","resultat = "+value);
                    mValue.setText(value);







                    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    mDate.setText(currentDateTimeString);

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCurrency.setText("This conversion does not exist");
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}