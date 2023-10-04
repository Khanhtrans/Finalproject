package com.example.mexpense.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpense.R;
import com.example.mexpense.databinding.TripListItemBinding;
import com.example.mexpense.entity.Trip;
import com.example.mexpense.ultilities.Utilities;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public class TripViewHolder extends RecyclerView.ViewHolder {

        private final TripListItemBinding binding;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = TripListItemBinding.bind(itemView);
        }

        public void bindData(Trip trip){
            binding.textStartDate.setText(Utilities.convertDate(trip.getStartDate(), false));
            binding.textEndDate.setText(Utilities.convertDate(trip.getEndDate(), false));
            binding.textTripDestination.setText(trip.getDestination());
            binding.textTripName.setText(trip.getName());
            String s = "$" + trip.getTotal();
            binding.textTotalCost.setText(s);
            binding.getRoot().setOnClickListener( view -> listener.onItemClick(trip.getId()));
        }
    }

    public interface TripItemListener{
        void onItemClick(int tripId);
    }

    private List<Trip> tripList;

    private TripItemListener listener;

    public TripAdapter(List<Trip> tripList, TripItemListener listener){
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.bindData(trip);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
