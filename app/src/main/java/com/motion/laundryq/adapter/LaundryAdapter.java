package com.motion.laundryq.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.motion.laundryq.activity.DetailLaundryActivity;
import com.motion.laundryq.R;
import com.motion.laundryq.model.LaundryModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_MODEL;

public class LaundryAdapter extends RecyclerView.Adapter<LaundryAdapter.ViewHolder> {
    private Context context;
    private List<LaundryModel> laundryList;
    private LatLng currentLocation;

    public LaundryAdapter(Context context) {
        this.context = context;
        laundryList = new ArrayList<>();
    }

    public void setData(List<LaundryModel> laundryList) {
        this.laundryList = laundryList;
        notifyDataSetChanged();
    }

    public void setCurrentLocation(LatLng latLng) {
        this.currentLocation = latLng;
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

        boolean isActive = laundryModel.isActive();

        if (isActive) {
            String urlPhoto = laundryModel.getUrlPhoto();
            String laundryName = laundryModel.getLaundryName();

            double lat = laundryModel.getLocation().getLatitude();
            double lng = laundryModel.getLocation().getLongitude();

            Location startPoint = new Location("Location A");
            Location endPoint = new Location("Location B");

            String distanceString = "-";
            if (currentLocation != null) {
                startPoint.setLatitude(currentLocation.latitude);
                startPoint.setLongitude(currentLocation.longitude);
                endPoint.setLatitude(lat);
                endPoint.setLongitude(lng);

                DecimalFormat df = new DecimalFormat("###.##");
                double distance = startPoint.distanceTo(endPoint);
                double km = distance/1000;

                if (km >= 1) {
                    distanceString = df.format(km) + " KM";
                } else {
                    distanceString = df.format(distance) + " M";
                }
            }

            holder.tvDistance.setText(distanceString);

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
                    Intent intent = new Intent(context, DetailLaundryActivity.class);
                    intent.putExtra(KEY_DATA_INTENT_LAUNDRY_MODEL, laundryModel);
                    context.startActivity(intent);
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
        TextView tvDistance;
        /*@BindView(R.id.like_button)
        LikeButton likeButton;*/
        @BindView(R.id.tv_laundry_status)
        TextView tvLaundryStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + km
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private double meterDistanceBetweenPoints(LatLng startPoint, LatLng endPoint) {
        float pk = (float) (180.f/Math.PI);

        double lat_a = startPoint.latitude;
        double lng_a = startPoint.longitude;

        double lat_b = endPoint.latitude;
        double lng_b = endPoint.longitude;

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }
}
