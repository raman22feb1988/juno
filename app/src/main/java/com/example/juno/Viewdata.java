package com.example.juno;

public class Viewdata extends Urltask {
    public AsyncResponse delegate = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String dataModels) {
        super.onPostExecute(dataModels);

        delegate.processFinish(dataModels, id);
    }
}