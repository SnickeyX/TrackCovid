package edu.harvard.cs50.trackcovid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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

public class GlobalActivity extends AppCompatActivity {
    private TextView global_cases_today;
    private TextView global_deaths_today;
    private TextView global_cases;
    private TextView global_deaths;
    private TextView global_recovered;
    private TextView global_active;
    private TextView global_critical;
    private TextView news;
    private TextView who;
    private TextView cdc;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionview);

        news = findViewById(R.id.news);
        news.setMovementMethod(LinkMovementMethod.getInstance());
        who = findViewById(R.id.who);
        who.setMovementMethod(LinkMovementMethod.getInstance());
        cdc = findViewById(R.id.cdc);
        cdc.setMovementMethod(LinkMovementMethod.getInstance());

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        global_cases_today = findViewById(R.id.globalcasestoday);
        global_deaths_today = findViewById(R.id.globaldeathstoday);
        global_cases = findViewById(R.id.globalcases);
        global_deaths = findViewById(R.id.globaldeaths);
        global_recovered = findViewById(R.id.globalrecovered);
        global_active = findViewById(R.id.globalactive);
        global_critical = findViewById(R.id.globalcritical);

        load();
    }

    public void load(){
        global_cases_today.setText("");
        global_deaths_today.setText("");
        global_cases.setText("");
        global_deaths.setText("");
        global_recovered.setText("");
        global_active.setText("");
        global_critical.setText("");
        String url = "https://thevirustracker.com/free-api?global=stats";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++){
                        JSONObject result = results.getJSONObject(i);
                        global_cases_today.setText("New cases today: " + result.getInt("total_new_cases_today"));
                        global_deaths_today.setText("Deaths today: " + result.getInt("total_new_deaths_today"));
                        global_cases.setText("Total cases: " + result.getInt("total_cases"));
                        global_deaths.setText("Total deaths: " + result.getInt("total_deaths"));
                        global_recovered.setText("Total recoveries: " + result.getInt("total_recovered"));
                        global_active.setText("Active cases: " + result.getInt("total_active_cases"));
                        global_critical.setText("Patients in critical condition: " + result.getInt("total_serious_cases"));
                    }
                } catch (JSONException e) {
                    Log.e("cs50", "Json globalcountry error", e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Json global_activity_list error", error);
            }
        });
        requestQueue.add(request);
    }
}
