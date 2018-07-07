package com.motion.laundryq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.motion.laundryq.R;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.LaundryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LaundryAdapter extends RecyclerView.Adapter<LaundryAdapter.ViewHolder> {
    private Context context;
    private List<LaundryModel> laundryList;

    public LaundryAdapter(Context context) {
        this.context = context;
        laundryList = new ArrayList<>();
    }

    public void setData(List<LaundryModel> laundryList) {
        this.laundryList = laundryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.laundry_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LaundryModel laundryModel = laundryList.get(position);
        final List<CategoryModel> categoryList = laundryModel.getCategories();

        boolean isActive = laundryModel.isActive();

        if (isActive) {
            String urlPhoto = laundryModel.getUrlPhoto();
            String laundryName = laundryModel.getLaundryName();

            String status;
            boolean isOpen = laundryModel.isOpen();

            if (isOpen) {
                status = "Buka";
                holder.tvLaundryStatus.setBackgroundResource(R.drawable.tv_status_open_template);
                holder.tvLaundryStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                status = "Tutup";
                holder.tvLaundryStatus.setBackgroundResource(R.drawable.tv_status_close_template);
                holder.tvLaundryStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
            }

            Glide.with(context).load(urlPhoto).into(holder.laundryImage);
            holder.laundryName.setText(laundryName);
            holder.tvLaundryStatus.setText(status);

            holder.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (CategoryModel cm : categoryList) {
                        Toast.makeText(context, cm.getCategoryName()
                                + " " + cm.getCategoryUnit() + " "
                                + cm.getCategoryPrice(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return laundryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_item)
        CardView cvItem;
        @BindView(R.id.laundry_image)
        ImageView laundryImage;
        @BindView(R.id.tv_laundry_name)
        TextView laundryName;
        @BindView(R.id.tv_distance)
        TextView laundryDistance;
        /*@BindView(R.id.like_button)
        LikeButton likeButton;*/
        @BindView(R.id.tv_laundry_status)
        TextView tvLaundryStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
