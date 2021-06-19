package de.hdm.weatherapp.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.repository.CityRepository;

public class FavouritesViewModel extends ViewModel {
    public final LiveData<List<CityEntity>> favourites;

    public FavouritesViewModel(CityRepository repository) {
        favourites = repository.getAllFavourites();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final CityRepository repository;

        public Factory(CityRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @NotNull
        @Override
        public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
            return (T) new FavouritesViewModel(repository);
        }
    }
}