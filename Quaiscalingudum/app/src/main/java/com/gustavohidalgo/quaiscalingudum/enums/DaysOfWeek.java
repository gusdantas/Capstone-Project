package com.gustavohidalgo.quaiscalingudum.enums;

import com.gustavohidalgo.quaiscalingudum.R;

/**
 * Created by gustavo.hidalgo on 18/02/07.
 */

public enum DaysOfWeek {
    SUNDAY (R.id.sun_cb),
    MONDAY (R.id.mon_cb),
    TUESDAY (R.id.tue_cb),
    WEDNESDAY (R.id.wed_cb),
    THURDAY (R.id.thu_cb),
    FRIDAY (R.id.fri_cb),
    SATURDAY (R.id.sat_cb);

    private final int res;
    DaysOfWeek(int resource) {
        this.res = resource;
    }

    public int getRes() {
        return res;
    }
}
