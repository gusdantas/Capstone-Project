package com.gustavohidalgo.quaiscalingudum.interfaces;

/**
 * Created by Gustavo on 27/03/2018.
 */

public interface OnRecyclerViewClickListener {
    void onTurnOn(int position);
    void onTurnOff(int position);
    void onDeleteClicked(int position);
}
