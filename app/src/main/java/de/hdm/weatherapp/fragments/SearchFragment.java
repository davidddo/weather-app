package de.hdm.weatherapp.fragments;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.loader.AssetsProvider;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.cities.City;

public class SearchFragment extends Fragment {

    public SearchFragment() { }

    private EditText searchEditText;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("-SA-","Search created");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText searchEditText = view.findViewById(R.id.search_editText);

        RecyclerView recyclerView = view.findViewById(R.id.searchRecyclerView);

        ArrayList<City> filteredCities = new ArrayList<>();

        MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(filteredCities);


        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                String searchString = searchEditText.getText().toString();

                Log.e("-SA-", searchString);

                Type listType = new TypeToken<ArrayList<City>>(){}.getType();
                ArrayList<City> cities = new Gson().fromJson(loadJSONFromAsset(), listType);

                filteredCities.clear();

                filteredCities.addAll(cities.stream().filter(city -> city.name.contains("Stuttgart")).collect(Collectors.toList()));

                Log.e("-SA-", filteredCities.toString());


                adapter.notifyDataSetChanged();



            }

        });

        return view;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("cities.list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

        private final List<City> mValues;

        public MyItemRecyclerViewAdapter(List<City> mValues) {
            this.mValues = mValues;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.cityData = mValues.get(position);


            holder.name.setText(mValues.get(position).name);
            holder.country.setText(mValues.get(position).country);

        }

        @Override
        public int getItemCount() {

            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView name;
            public final TextView country;

            public City cityData;

            public View mView;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;

                name = (TextView) itemView.findViewById(R.id.name);
                country = (TextView) itemView.findViewById(R.id.country);

                name.setOnClickListener(this);
                country.setOnClickListener(this);


                mView.setOnClickListener(this);


            }

            @Override
            public void onClick(View v) {



            }


            public void removeAt(int position) {

            }
        }
    }

}