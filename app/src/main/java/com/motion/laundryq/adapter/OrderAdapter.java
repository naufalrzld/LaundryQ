package com.motion.laundryq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.motion.laundryq.R;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.OrderLaundryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private static final String SEPARATOR = ", ";
    private Context context;
    private List<OrderLaundryModel> orderList;

    public OrderAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
    }

    public void setOrderList(List<OrderLaundryModel> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderLaundryModel orderLaundryModel = orderList.get(position);
        List<CategoryModel> categories = orderLaundryModel.getCategories();

        String orderID = orderLaundryModel.getOrderID();

        StringBuilder stringBuilder = new StringBuilder();
        for (CategoryModel cm : categories) {
            stringBuilder.append(cm.getCategoryName());
            stringBuilder.append(SEPARATOR);
        }

        String category = stringBuilder.toString();
        category = category.substring(0, category.length() - SEPARATOR.length());

        String datePickup = orderLaundryModel.getDatePickup();
        String dateDelivery = orderLaundryModel.getDateDelivery();

        int status = orderLaundryModel.getStatus();
        String statusMsg = "-";
        if (status == 0) {
            statusMsg = "Menunggu konfirmasi";
        } else if (status == 1) {
            statusMsg = "Dikonfirmasi";
        } else if (status == 2) {
            statusMsg = "Order ditolak";
        } else if (status == 3) {
            statusMsg = "Dicuci";
        } else if (status == 4) {
            statusMsg = "Cuci selesai";
        } else if (status == 5) {
            statusMsg = "Sedang diantar";
        } else if (status == 6) {
            statusMsg = "Sudah diterima";
        }

        holder.tvOrderID.setText(orderID);
        holder.tvCategoryName.setText(category);
        holder.tvDatePickup.setText(datePickup);
        holder.tvDateDelivery.setText(dateDelivery);
        holder.tvStatus.setText(statusMsg);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_item)
        CardView cvItem;
        @BindView(R.id.tv_order_id)
        TextView tvOrderID;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_date_pickup)
        TextView tvDatePickup;
        @BindView(R.id.tv_date_delivery)
        TextView tvDateDelivery;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
