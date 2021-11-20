package edu.harvard.cs50.trackcovid;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {

    @Override
    public Filter getFilter() { return new CountryFilter(); }
    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;

        CountryViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.country_row);
            textView = view.findViewById(R.id.country_row_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Country current = (Country) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), CountryActivity.class);
                    intent.putExtra("name", current.getName());
                    intent.putExtra("total_cases", current.getTotal_cases());
                    intent.putExtra("total_recovered", current.getTotal_recovered());
                    intent.putExtra("total_deaths", current.getTotal_deaths());

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private List<Country> country = new ArrayList<>();
    private List<Country> filtered = new ArrayList<>();
    private RequestQueue requestQueue;

    CountryAdapter(Context context){
        requestQueue = Volley.newRequestQueue(context);
        loadCountry();
    }

    public void loadCountry() {
        String url = "https://coronavirus-19-api.herokuapp.com/countries";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray results = response;
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        country.add(new Country(result.getString("country"), result.getInt("cases"), result.getInt("recovered"), result.getInt("deaths")));
                    }
                    notifyDataSetChanged();
                    filtered.addAll(country);
                } catch (JSONException e) {
                    Log.e("cs50", "Json country error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cs50", "Json countrylist error", error);
            }
        });
        requestQueue.add(request);
    }

    private class CountryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Country> filteredcountry = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                results.values = country;
                results.count = country.size();
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();

                for (Country p : country) {
                    if (p.getName().toLowerCase().contains(filterpattern)) {
                        filteredcountry.add(p);
                    }
                }
                results.values = filteredcountry;
                results.count = filteredcountry.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered.clear();
            filtered.addAll((List) results.values);
            notifyDataSetChanged();
        }
    }
        @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_row, parent, false);

        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country current = filtered.get(position);
        String s = current.getName() + "\n" + " cases: " + current.getTotal_cases()  +" | deaths: " + current.getTotal_deaths() + " | recovered: " + current.getTotal_recovered();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new RelativeSizeSpan(1.5f),0,(current.getName().length()), 0);
        holder.textView.setText(spannableString);
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }


}
