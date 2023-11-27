package com.example.mexpense.fragments.main.budget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpense.R;
import com.example.mexpense.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    List<Category> arrayListFiltered;
    List<Category> arrayList;

    Context context;

    public final int TYPE_DATE = 0; // category chính
    public final int TYPE_CONTENT = 1; // các category con

    public BudgetAdapter (ArrayList<Category> arrayList, Context context) {
        this.arrayListFiltered = arrayList;
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {



        if (arrayListFiltered.get(position).getName().equals("")
        )
            return TYPE_DATE;
        else
            return TYPE_CONTENT;
    }

    @NonNull
    @Override
    public RecyclerView . ViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int viewType
    ) {

        if (viewType == TYPE_DATE) {

            View view = LayoutInflater. from (context).inflate(R.layout.item_header_category, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new dateViewHolder (view);

        } else {

            View view1 = LayoutInflater . from (context).inflate(R.layout.item_content_category, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view1.setLayoutParams(lp);
            return new contentViewHolder (view1);
        }

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

//  hiển thị lên view như thế nào
        if (viewHolder instanceof contentViewHolder) {
            // sửa hiển thị cho view category con
            ((contentViewHolder) viewHolder).tvName.setText(arrayListFiltered.get(position).getName());
            Long spend = arrayListFiltered.get(position).getSpend();
            ((contentViewHolder) viewHolder).tvSpend.setText(String.format("%,d",spend));
        }

        if (viewHolder instanceof dateViewHolder) {

            // sửa hiện thị cho category cha
            String typeCate = arrayListFiltered.get(position).getType();
            ((dateViewHolder) viewHolder).type.setText(typeCate);
        }

    }


    public class contentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvSpend;

        public contentViewHolder (@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvSpend = itemView.findViewById(R.id.tv_spend);
        }
    }

    public class dateViewHolder extends RecyclerView.ViewHolder {
        TextView type;

        public dateViewHolder (@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.tv_type);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    arrayListFiltered = arrayList;
                } else {
                    List<Category> filteredList = new ArrayList<>();
                    for (Category row : arrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    arrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayListFiltered;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayListFiltered = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return arrayListFiltered.size();
    }
}
