package com.motion.laundryq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motion.laundryq.R;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.OrderLaundryModel;
import com.motion.laundryq.utils.CurrencyConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_INTENT_ORDER;

public class MyLaundryAdapter extends RecyclerView.Adapter<MyLaundryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> categories;
    private int status;

    public MyLaundryAdapter(Context context, int status) {
        this.context = context;
        categories = new ArrayList<>();
        this.status = status;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_laundry_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel cm = categories.get(position);

        String icon = cm.getIcon();
        String categoryName = cm.getCategoryName();
        int categoryPrice = cm.getCategoryPrice();
        int quantity = cm.getQuantity();
        int total = categoryPrice * quantity;
        String categoryUnit = cm.getCategoryUnit();
        String quantityString = quantity + " " + categoryUnit;
        int status = cm.getStatus();
        String statusMsg = "-";

        if (status == 0) {
            statusMsg = "Belum dicuci";
        } else if (status == 1) {
            statusMsg = "Dicuci";
        } else if (status == 2) {
            statusMsg = "Selesai";
        }

        Glide.with(context).load(icon).into(holder.imgCategory);
        holder.tvCategoryName.setText(categoryName);
        holder.tvCategoryPrice.setText(CurrencyConverter.toIDR(categoryPrice));
        holder.tvQuantity.setText(quantityString);
        holder.tvTotal.setText(CurrencyConverter.toIDR(total));

        if (this.status == KEY_INTENT_ORDER) {
            holder.lytStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(statusMsg);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_category)
        ImageView imgCategory;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_category_price)
        TextView tvCategoryPrice;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.lyt_status)
        LinearLayout lytStatus;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
