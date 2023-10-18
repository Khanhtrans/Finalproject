package com.example.mexpense.fragments.main.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpense.R;
import com.example.mexpense.entity.Category;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Category> arrayList;

    Context context;

    public final int TYPE_DATE = 0;
    public final int TYPE_CONTENT = 1;

    public BudgetAdapter (ArrayList<Category> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {



        if (arrayList.get(position).getName().equals("")
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder instanceof contentViewHolder) {

            ((contentViewHolder) viewHolder).tvName.setText(arrayList.get(position).getName());
            ((contentViewHolder) viewHolder).tvSpend.setText(String.valueOf(arrayList.get(position).getSpend()));
        }

        if (viewHolder instanceof dateViewHolder) {
            String typeCate = arrayList.get(position).getType();
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
    public int getItemCount() {
        return arrayList.size();
    }
}
