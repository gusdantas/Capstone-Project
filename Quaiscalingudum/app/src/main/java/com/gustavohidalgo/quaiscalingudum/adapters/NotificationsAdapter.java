package com.gustavohidalgo.quaiscalingudum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnRecyclerViewClickListener;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by hdant on 14/02/2018.
 */

public class NotificationsAdapter extends
        RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>  {
    private List<BusNotification> mBusNotificationList;
    private OnRecyclerViewClickListener mOnRecyclerViewClickListener;


    public NotificationsAdapter(List<BusNotification> busNotificationList,
                                OnRecyclerViewClickListener clickListener){
        this.mBusNotificationList = busNotificationList;
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
        BusNotification busNotification = mBusNotificationList.get(position);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm\ndd/MM/yyyy");
        holder.mNameSW.setText(busNotification.getName());
        holder.mNameSW.setChecked(intToBoolean(busNotification.getActive()));
        holder.mLineCodeTV.setText(busNotification.getTrip().getRouteId());
        holder.mLineNameTV.setText(busNotification.getTrip().getTripHeadsign());
        holder.mDepartTimeTV.setText(NotificationUtils.getDateTime(busNotification
                .getDepartureDateTime()).toString(fmt));
        holder.mDepartPlaceTV.setText(busNotification.getDepartureStop());
        holder.mArriveTimeTV.setText(NotificationUtils.getDateTime(busNotification
                .getArriveDateTime()).toString(fmt));
        holder.mArrivePlaceTV.setText(busNotification.getArriveStop());

    }

    @Override
    public int getItemCount() {
        return mBusNotificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private WeakReference<OnRecyclerViewClickListener> listenerRef;
        @BindView(R.id.name_switch)
        Switch mNameSW;
        @BindView(R.id.delete_button)
        ImageButton mDeleteButton;
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
        }

        @OnCheckedChanged(R.id.name_switch)
        void changeActive(){
            if (mNameSW.isChecked()) {
                listenerRef.get().onTurnOn(getAdapterPosition());
            } else {
                listenerRef.get().onTurnOff(getAdapterPosition());
            }
        }

        @OnClick(R.id.delete_button)
        void deleteNotification(){
            listenerRef.get().onDeleteClicked(getAdapterPosition());
        }
    }

    private boolean intToBoolean(int integer){
        return integer != 0;
    }
}
