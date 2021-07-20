package com.example.sockettest.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUtils {
    public static void requestPermissions(Activity activity, String... permissions){
        ArrayList<String> checkPermissions=new ArrayList<>();
        for(String s:permissions){
            if(ContextCompat.checkSelfPermission(activity.getApplicationContext(),s)!= PackageManager.PERMISSION_GRANTED) {
                checkPermissions.add(s);
            } }
        if(!checkPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, checkPermissions.toArray(new String[checkPermissions.size()]), 1);
        } }
    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            for (int i = 0; i < permissions.length; i++) {
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity,"Permission: "+permissions[i]+" granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity,"Go to Settings to set the permissions manually",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}