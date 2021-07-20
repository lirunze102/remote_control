package com.example.sockettest.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class TestAsyncTask extends AsyncTask<Void,Integer,Void> {
    private Context context;
    private ProgressDialog progressDialog;
    private String text;
    public TestAsyncTask(Context context,String text) {
        this.context = context;
        this.text=text;
    }
    private void iniProgressDialog(int max) {
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("文件下载中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("");
        progressDialog.setMax(max);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected void onPreExecute() {// in UI thread
// before doInBackground
        super.onPreExecute();
        iniProgressDialog(text.length());
    }
    @Override
    protected void onPostExecute(Void aVoid) {// in UI thread
// after doInBackground
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context,"下载完成!",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
// in UI thread, triggered by publishProgress
        super.onProgressUpdate(values);
        progressDialog.setMessage(text.substring(0,values[0]));
        progressDialog.setProgress(values[0]);
    }
    @Override
    protected Void doInBackground(Void... aVoid) {
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(i+1);// will run onProgressUpdate()
        }
        return null; //will run onPostExcute()
    }
}