package com.example.mexpense.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpense.R;
import com.example.mexpense.databinding.ExpenseListItemBinding;
import com.example.mexpense.entity.Expense;
import com.example.mexpense.ultilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.bindData(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private final ExpenseListItemBinding binding;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ExpenseListItemBinding.bind(itemView);
        }

        public void bindData(Expense expense) {
            binding.textCost.setText("$ " + expense.getCost());
            binding.textDate.setText(Utilities.convertDate(expense.getDate(), false));
            binding.textAmount.setText(" x " + expense.getAmount());
            binding.textCategory.setText(expense.getCategory());
            binding.iconCategory.setImageResource(getIcon(expense.getCategory()));
            binding.getRoot().setOnClickListener( v-> listener.onItemClick(expense.getId()));
        }

        private int getIcon(String category) {
            return binding.getRoot()
                    .getResources()
                    .getIdentifier("ic_" + category.toLowerCase(), "drawable", binding.getRoot().getContext().getPackageName());
        }
    }

    public interface ItemListener {
        void onItemClick(int expenseId);
    }

    private List<Expense> expenseList;

    private ItemListener listener;

    public ExpenseAdapter(List<Expense> expenseList, ItemListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }
}
