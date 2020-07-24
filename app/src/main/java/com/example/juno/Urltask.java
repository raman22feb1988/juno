package com.example.juno;

import android.os.AsyncTask;

abstract class Urltask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {

        JSONhelper jsonhelper = new JSONhelper();
        String data = new String();

        switch(params[0]) {
            case "GET": data = jsonhelper.getdatafromurl(params[1]);
                break;
            case "POST": data = jsonhelper.postdatatourl(params[1], params[2]);
                break;
        }

        return data;
    }
}