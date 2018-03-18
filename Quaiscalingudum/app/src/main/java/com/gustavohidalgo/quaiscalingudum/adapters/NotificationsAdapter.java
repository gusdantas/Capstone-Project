package com.gustavohidalgo.quaiscalingudum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnTripSelectListener;
import com.gustavohidalgo.quaiscalingudum.models.Trip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.*;

/**
 * Created by hdant on 14/02/2018.
 */

public class NotificationsAdapter extends
        RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>  {
    private ArrayList<String> mLinesFiltered, mLines;
    //private static String[] mLineChosen;
    private static Trip sTripSelected;

    private static OnTripSelectListener mChooseLineListener;

    public NotificationsAdapter(){

    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.line_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        String[] mLineChosen = mLinesFiltered.get(position).split(",");
        sTripSelected = new Trip(
                mLineChosen[TRIPS_ROUTE_ID],
                mLineChosen[TRIPS_SERVICE_ID],
                mLineChosen[TRIPS_TRIP_ID],
                mLineChosen[TRIPS_TRIP_HEADSIGN],
                mLineChosen[TRIPS_TRIP_DIRECTION_ID],
                mLineChosen[TRIPS_TRIP_SHAPE_ID]
        );
//        holder.mLineCodeTV.setText(mLineChosen[0]);
//        holder.mLineNameTV.setText(mLineChosen[3]);
        holder.mLineCodeTV.setText(sTripSelected.getRouteId());
        holder.mLineNameTV.setText(sTripSelected.getTripHeadsign());
    }

    @Override
    public int getItemCount() {
        if (mLinesFiltered != null){
            return mLinesFiltered.size();
        }
        return 0;
    }

    public void setLines(ArrayList<String> lines){
        this.mLines = new ArrayList<>();
        this.mLinesFiltered = new ArrayList<>();
        this.mLines.addAll(lines);
        this.mLinesFiltered.addAll(lines);
        notifyDataSetChanged();
    }

    public void setChooseLineListener(OnTripSelectListener chooseLineListener){
        mChooseLineListener = chooseLineListener;
    }

    public void linesFilter(String filter) {
        mLinesFiltered.clear();
        if(filter.isEmpty()){
            mLinesFiltered.addAll(mLines);
        } else {
            filter = filter.toLowerCase();
            for(String item : mLines){
                String[] line = item.split(",");
                if(line[0].toLowerCase().replaceAll("\"", "").contains(filter)
                        || line[3].toLowerCase().replaceAll("\"", "").contains(filter)){
                    mLinesFiltered.add(item);
                    notifyDataSetChanged();
                }
            }
        }
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        @BindView(R.id.line_code_tv)
        TextView mLineCodeTV;
        @BindView(R.id.line_name_tv)
        TextView mLineNameTV;


        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChooseLineListener.tripSelected(sTripSelected);
        }
    }
}
