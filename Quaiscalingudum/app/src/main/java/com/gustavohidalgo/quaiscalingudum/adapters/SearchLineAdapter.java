package com.gustavohidalgo.quaiscalingudum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnChooseLineListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hdant on 14/02/2018.
 */

public class SearchLineAdapter extends RecyclerView.Adapter<SearchLineAdapter.LineViewHolder>  {
    private ArrayList<String> mLines;
    private static String[] mLineChosen;

    private static OnChooseLineListener mChooseLineListener;

    public SearchLineAdapter(){

    }

    @Override
    public LineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.line_item, parent, false);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LineViewHolder holder, int position) {
        mLineChosen = mLines.get(position).split(",");
        holder.mLineCodeTV.setText(mLineChosen[0]);
        holder.mLineNameTV.setText(mLineChosen[3]);
    }

    @Override
    public int getItemCount() {
        if (mLines != null){
            return mLines.size();
        }
        return 0;
    }

    public void setLines(ArrayList<String> lines){
        this.mLines = lines;
        notifyDataSetChanged();
    }

    public void setChooseLineListener(OnChooseLineListener chooseLineListener){
        mChooseLineListener = chooseLineListener;
    }

    public static class LineViewHolder extends RecyclerView.ViewHolder
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
            mChooseLineListener.lineChosen(mLineChosen);
        }
    }
}
