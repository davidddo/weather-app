package de.hdm.weatherapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.entity.CityEntity;

public class SearchAdapter extends PagingDataAdapter<CityEntity, SearchAdapter.CityViewHolder> {

    public SearchAdapter(@NotNull DiffUtil.ItemCallback<CityEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public SearchAdapter.CityViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchAdapter.CityViewHolder holder, int position) {
        CityEntity city = getItem(position);
        if (city != null) {
            holder.bind(city);
            holder.view.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putInt("cityId", city.id);
                Navigation.findNavController(view).navigate(R.id.action_navigation_search_to_navigation_search_result, args);
            });
        }
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public final TextView name;
        public final TextView country;

        public CityViewHolder(View view) {
            super(view);
            this.view = view;

            name = view.findViewById(R.id.name);
            country =  view.findViewById(R.id.country);
        }

        public void bind(CityEntity city) {
            this.name.setText(city.name);
            this.country.setText(city.country);
            //this.country.setText(city.country);
        }
    }
}