package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.adapters.SearchAdapter;
import de.hdm.weatherapp.adapters.SearchLoadStateAdapter;
import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.CityDatabase;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.utils.Utils;
import kotlinx.coroutines.CoroutineScope;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private SearchViewModel searchViewModel;
    private SearchAdapter searchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.search_recycler_view);
        searchAdapter = new SearchAdapter(new CityComparator());

        CityDatabase cityDatabase = AppDatabase.instance(requireContext()).getCityDatabase();
        searchViewModel = new ViewModelProvider(this, new SearchViewModel.Factory(cityDatabase.cityDao()))
                .get(SearchViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(searchAdapter.withLoadStateFooter(new SearchLoadStateAdapter(v -> {
            searchAdapter.retry();
        })));

        queryCities("");

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_option_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        queryCities(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        queryCities(query);
        return false;
    }

    private void queryCities(String query) {
        searchViewModel.getPagingSource(query).observe(getViewLifecycleOwner(), pagingData -> {
            searchAdapter.submitData(getViewLifecycleOwner().getLifecycle(), pagingData);
        });
    }

    private static class CityComparator extends DiffUtil.ItemCallback<CityEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull CityEntity oldItem, @NonNull CityEntity newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CityEntity oldItem, @NonNull CityEntity newItem) {
            return oldItem.equals(newItem);
        }
    }

    private static class SearchViewModel extends ViewModel {
        private final CityDao cityDao;

        public SearchViewModel(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        public LiveData<PagingData<CityEntity>> getPagingSource(String query) {
            Pager<Integer, CityEntity> pager = new Pager<>(
                    new PagingConfig(20),
                    () -> cityDao.pagingSource(query));

            CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
            return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), coroutineScope);
        }

        static class Factory implements ViewModelProvider.Factory {
            private final CityDao cityDao;

            public Factory(CityDao cityDao) {
                this.cityDao = cityDao;
            }

            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) new SearchViewModel(cityDao);
            }
        }
    }
}