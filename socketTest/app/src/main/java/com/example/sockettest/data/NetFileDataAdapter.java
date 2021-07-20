package com.example.sockettest.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sockettest.R;

import java.util.ArrayList;

public class NetFileDataAdapter extends ArrayAdapter<NetFileData> {
    private Context context;
    private ArrayList<NetFileData> list;
    public NetFileDataAdapter(@NonNull Context context,  ArrayList<NetFileData> list) {
        super(context, android.R.layout.simple_list_item_1,list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v;
        if(convertView==null){
            v= LayoutInflater.from(context).inflate(R.layout.row_view,null,false);
        }else{
            v=convertView;
        }
        TextView name=v.findViewById(R.id.row_name);
        TextView time=v.findViewById(R.id.row_time);
        TextView size=v.findViewById(R.id.row_size);
        final NetFileData data=list.get(position);
        name.setText(data.getFileName());
        time.setText(data.getFileModifiedDate());
        size.setText(data.getFileSizeStr());
        return v;
    }
}
