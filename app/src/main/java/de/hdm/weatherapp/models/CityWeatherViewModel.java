package de.hdm.weatherapp.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.repository.CityRepository;

public class CityWeatherViewModel extends ViewModel {
    public final LiveData<CityEntity> city;

    private final CityRepository repository;
    private final int id;

    public CityWeatherViewModel(Context context, int id) {
        this.id = id;
        this.repository = new CityRepository(context);
        this.city = repository.getById(id);
    }

    public void updateBookmark(boolean saved) {
        repository.update(id, saved);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context context;
        private final int id;

        public Factory(Context context, final int id) {
            this.context = context;
            this.id = id;
        }

        @NonNull
        @NotNull
        @Override
        public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
            return (T) new CityWeatherViewModel(context, id);
        }
    }
}
