package de.hdm.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.R;

public class SearchLoadStateAdapter extends LoadStateAdapter<SearchLoadStateAdapter.LoadStateViewHolder> {
    private final View.OnClickListener retryCallback;

    public SearchLoadStateAdapter(View.OnClickListener retryCallback) {
        this.retryCallback = retryCallback;
    }

    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup parent, @NotNull LoadState loadState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_state_item, parent,false);
        return new LoadStateViewHolder(view, retryCallback);
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder holder, @NotNull LoadState loadState) {
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