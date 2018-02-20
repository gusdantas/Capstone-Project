package com.gustavohidalgo.quaiscalingudum.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.data.TripsContract;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnChooseLineListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hdant on 14/02/2018.
 */

public class SearchLineAdapter extends RecyclerView.Adapter<SearchLineAdapter.LineViewHolder>  {
    private final Context mContext;
    private Cursor mCursor;
    private final OnChooseLineListener mChooseLineListener;

    public SearchLineAdapter(Context context, OnChooseLineListener chooseLineListener){
        this.mContext = context;
        this.mChooseLineListener = chooseLineListener;
    }

    @Override
    public LineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.line_item, parent, false);
        view.setFocusable(true);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LineViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String code = mCursor.getString(mCursor.getColumnIndex(TripsContract.TripsEntry.ROUTE_ID));
        String name = mCursor.getString(mCursor.getColumnIndex(TripsContract.TripsEntry.TRIP_HEADSIGN));
        holder.mLineCodeTV.setText(code);
        holder.mLineNameTV.setText(name);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void linesFilter(String filter) {
//        mLinesFiltered.clear();
//        if(filter.isEmpty()){
//            mLinesFiltered.addAll(mLines);
//        } else {
//            filter = filter.toLowerCase();
//            for(String item : mLines){
//                String[] line = item.split(",");
//                if(line[0].toLowerCase().replaceAll("\"", "").contains(filter)
//                        || line[3].toLowerCase().replaceAll("\"", "").contains(filter)){
//                    mLinesFiltered.add(item);
//                    notifyDataSetChanged();
//                }
//            }
//        }
    }

    public class LineViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        @BindView(R.id.line_code_tv)
        TextView mLineCodeTV;
        @BindView(R.id.line_name_tv)
        TextView mLineNameTV;


        public LineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String[] lineChosen = new String[mCursor.getCount()];
            for(int i = 0; i < 6; i++){
                lineChosen[i] = mCursor.getString(i);
            }
            mChooseLineListener.lineChosen(lineChosen);
        }
    }
}
