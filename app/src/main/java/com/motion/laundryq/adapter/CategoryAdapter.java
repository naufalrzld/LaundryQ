package com.motion.laundryq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.motion.laundryq.R;
import com.motion.laundryq.model.CategoryModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> categoryList;
    private int[] count;

    public CategoryAdapter(Context context) {
        this.context = context;
        categoryList = new ArrayList<>();
    }

    public void setCategoryList(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
        count = new int[categoryList.size()];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CategoryModel categoryModel = categoryList.get(position);

        count[position] = 0;
        String icon = categoryModel.getIcon();
        String categoryName = categoryModel.getCategoryName();
        int categoryPrice = categoryModel.getCategoryPrice();
        String categoryUnit = categoryModel.getCategoryUnit();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        kursIndonesia.setMaximumFractionDigits(0);

        String categoryPriceUnit = kursIndonesia.format(categoryPrice) + " / " + categoryUnit;

        Glide.with(context).load(icon).into(holder.imgCategory);
        holder.tvCategoryName.setText(categoryName);
        holder.tvCategoryPrice.setText(categoryPriceUnit);
        holder.btnWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lytNumberPicker.setVisibility(View.VISIBLE);
                holder.btnWash.setVisibility(View.GONE);
                count[position]++;
                holder.tvCount.setText(String.valueOf(count[position]));
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[position]++;
                holder.tvCount.setText(String.valueOf(count[position]));
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[position]--;
                holder.tvCount.setText(String.valueOf(count[position]));
                if (count[position] == 0) {
                    holder.btnWash.setVisibility(View.VISIBLE);
                    holder.lytNumberPicker.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_category)
        ImageView imgCategory;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_category_price)
        TextView tvCategoryPrice;
        @BindView(R.id.btn_wash)
        Button btnWash;
        @BindView(R.id.lyt_number_picker)
        LinearLayout lytNumberPicker;
        @BindView(R.id.btn_plus)
        ImageView btnPlus;
        @BindView(R.id.btn_minus)
        ImageView btnMinus;
        @BindView(R.id.tv_count)
        TextView tvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
