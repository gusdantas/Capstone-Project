package com.gustavohidalgo.quaiscalingudum.services;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Gustavo on 01/04/2018.
 */

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
