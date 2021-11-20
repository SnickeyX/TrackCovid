package edu.harvard.cs50.trackcovid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CountryActivity extends AppCompatActivity {
    private TextView name;
    private TextView cases_today;
    private TextView deaths_today;
    private TextView total_recovered;
    private TextView total_cases;
    private TextView total_deaths;
    private TextView active;
    private TextView critical;
    private TextView onemillion;
    private String country_name;
    private int cases;
    private int deaths;
    private int recovered;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionview);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        name = findViewById(R.id.country_name);
        cases_today = findViewById(R.id.todays_cases);
        deaths_today = findViewById(R.id.todays_deaths);
        total_cases = findViewById(R.id.total_cases);
        total_deaths = findViewById(R.id.total_deaths);
        total_recovered = findViewById(R.id.total_recovered);
        active = findViewById(R.id.active);
        critical = findViewById(R.id.critical);
        onemillion = findViewById(R.id.one_million);
        country_name = getIntent().getStringExtra("name");
        cases = getIntent().getIntExtra("total_cases",0);
        deaths = getIntent().getIntExtra("total_deaths",0);
        recovered = getIntent().getIntExtra("total_recovered",0);

        load();
    }

    public void load() {
        name.setText(country_name);
        total_cases.setText("Total cases: " + cases);
        total_deaths.setText("Total deaths: " + deaths);
        total_recovered.setText("Total recoveries: " + recovered);
        cases_today.setText("");
        deaths_today.setText("");
        active.setText("");
        critical.setText("");
        onemillion.setText("");
        String url = "https://coronavirus-19-api.herokuapp.com/countries/" + country_name;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try {
                        JSONObject result = response;
                        cases_today.setText("New cases today: " + result.getInt("todayCases"));
                        deaths_today.setText("Deaths today: " + result.getInt("todayDeaths"));
                        active.setText("Active cases: " + result.getInt("active"));
                        critical.setText("Patients in critical condition: " + result.getInt("critical"));
                        onemillion.setText("Number of cases per one million: " + result.getInt("casesPerOneMillion"));
                    } catch (JSONException e) {
                        Log.e("cs50", "Json country_activity error", e);
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Json country_activity_list error", error);
            }
        });
        requestQueue.add(request);
    }
}
