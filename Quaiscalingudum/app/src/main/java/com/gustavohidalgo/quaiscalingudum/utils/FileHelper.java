package com.gustavohidalgo.quaiscalingudum.utils;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by hdant on 14/02/2018.
 */

public final class FileHelper {

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
