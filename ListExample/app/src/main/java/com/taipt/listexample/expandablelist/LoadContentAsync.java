package com.taipt.listexample.expandablelist;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoadContentAsync extends AsyncTask<Void, Void, MainContentModel> {

    private ILoadContentCallback loadContentCallback;
    private Context context;

    public LoadContentAsync(Context context, ILoadContentCallback callback) {
        this.context = context;
        this.loadContentCallback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected MainContentModel doInBackground(Void... voids) {

        Gson gson = new Gson();
        MainContentModel mainContentModel = null;

        try {
            InputStream is = context.getAssets().open("phone.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            synchronized (this) {
                mainContentModel = gson.fromJson(reader, MainContentModel.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mainContentModel;
    }

    @Override
    protected void onPostExecute(MainContentModel mainContentModel) {
        if (loadContentCallback != null) {
            loadContentCallback.onLoadContentSuccess(mainContentModel);
        }
        super.onPostExecute(mainContentModel);
    }

}
