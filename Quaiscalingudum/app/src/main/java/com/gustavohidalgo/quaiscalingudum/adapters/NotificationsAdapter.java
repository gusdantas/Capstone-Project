package com.gustavohidalgo.quaiscalingudum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnRecyclerViewClickListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by hdant on 14/02/2018.
 */

public class NotificationsAdapter extends
        RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>  {
    private List<Notification> mNotificationList;
    private OnRecyclerViewClickListener mOnRecyclerViewClickListener;


    public NotificationsAdapter(List<Notification> notificationList, OnRecyclerViewClickListener clickListener){
        this.mNotificationList = notificationList;
        this.mOnRecyclerViewClickListener = clickListener;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view, mOnRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = mNotificationList.get(position);
        holder.mNameSW.setText(notification.getName());
        holder.mNameSW.setChecked(intToBoolean(notification.getActive()));
        holder.mLineCodeTV.setText(notification.getTrip().getRouteId());
        holder.mLineNameTV.setText(notification.getTrip().getTripHeadsign());
        holder.mDepartTimeTV.setText("");
        holder.mDepartPlaceTV.setText("");
        holder.mArriveTimeTV.setText(NotificationUtils.getDateTime(notification).toString());
        holder.mArrivePlaceTV.setText(notification.getStopTime().getStopId());

    }


    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private WeakReference<OnRecyclerViewClickListener> listenerRef;
        @BindView(R.id.name_switch)
        Switch mNameSW;
        @BindView(R.id.line_code_tv)
        TextView mLineCodeTV;
        @BindView(R.id.line_name_tv)
        TextView mLineNameTV;
        @BindView(R.id.depart_time_tv)
        TextView mDepartTimeTV;
        @BindView(R.id.depart_place_tv)
        TextView mDepartPlaceTV;
        @BindView(R.id.arrive_time_tv)
        TextView mArriveTimeTV;
        @BindView(R.id.arrive_place_tv)
        TextView mArrivePlaceTV;


        public NotificationViewHolder(View itemView, OnRecyclerViewClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            listenerRef = new WeakReference<>(clickListener);
            //itemView.setOnClickListener(this);
        }

        @OnCheckedChanged(R.id.name_switch)
        void changeActive(){
            if (mNameSW.isChecked()) {
                listenerRef.get().onTurnOn(getAdapterPosition());
            } else {
                listenerRef.get().onTurnOff(getAdapterPosition());
            }
        }
    }

    private boolean intToBoolean(int integer){
        return integer != 0;
    }
}
