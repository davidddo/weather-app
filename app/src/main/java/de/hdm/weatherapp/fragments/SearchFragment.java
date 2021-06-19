package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import androidx.paging.PagingLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;

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

        AppDatabase database = AppDatabase.getInstance(requireContext());
        searchViewModel = new ViewModelProvider(this, new SearchViewModel.Factory(database.cityDao()))
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
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchView.setOnQueryTextListener(this);
        searchView.setSuggestionsAdapter(null);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
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

    private static class SearchAdapter extends PagingDataAdapter<CityEntity, SearchAdapter.CityViewHolder> {
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
            private final View view;

            private final TextView name;
            private final TextView country;

            public CityViewHolder(View view) {
                super(view);
                this.view = view;

                name = view.findViewById(R.id.name);
                country = view.findViewById(R.id.country);
            }

            public void bind(CityEntity city) {
                this.name.setText(city.name);
                this.country.setText(city.country);
            }
        }
    }

    private static class SearchLoadStateAdapter extends LoadStateAdapter<SearchLoadStateAdapter.LoadStateViewHolder> {
        private final View.OnClickListener retryCallback;

        public SearchLoadStateAdapter(View.OnClickListener retryCallback) {
            this.retryCallback = retryCallback;
        }

        @NotNull
        @Override
        public SearchLoadStateAdapter.LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup parent, @NotNull LoadState loadState) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_state_item, parent,false);
            return new SearchLoadStateAdapter.LoadStateViewHolder(view, retryCallback);
        }

        @Override
        public void onBindViewHolder(@NotNull SearchLoadStateAdapter.LoadStateViewHolder holder, @NotNull LoadState loadState) {
            holder.bind(loadState);
        }

        public static class LoadStateViewHolder extends RecyclerView.ViewHolder {
            private final ProgressBar progressBar;
            private final TextView errorView;
            private final Button retryButton;

            LoadStateViewHolder(@NonNull View view, @NonNull View.OnClickListener retryCallback) {
                super(view);

                progressBar = view.findViewById(R.id.progress_bar);
                errorView = view.findViewById(R.id.error_view);
                retryButton = view.findViewById(R.id.retry_button);

                retryButton.setOnClickListener(retryCallback);
            }

            public void bind(LoadState loadState) {
                if (loadState instanceof LoadState.Error) {
                    LoadState.Error loadStateError = (LoadState.Error) loadState;
                    errorView.setText(loadStateError.getError().getLocalizedMessage());
                }

                progressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
                retryButton.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
                errorView.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
            }
        }
    }

    private static class SearchViewModel extends ViewModel {
        private final CityDao cityDao;

        public SearchViewModel(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        public LiveData<PagingData<CityEntity>> getPagingSource(String query) {

            Pager<Integer, CityEntity> pager = new Pager<>(
                    new PagingConfig(20, 2, false, 20, 24),
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