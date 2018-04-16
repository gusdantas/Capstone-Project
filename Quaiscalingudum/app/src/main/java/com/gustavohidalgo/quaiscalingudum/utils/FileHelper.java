package com.gustavohidalgo.quaiscalingudum.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by hdant on 14/02/2018.
 */

public class FileHelper {

    public static ArrayList<String> getLines(int resource, Context context) {
        ArrayList<String> lines = new ArrayList<>();

        InputStream inputStream = context.getResources().openRawResource(resource);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }


}
