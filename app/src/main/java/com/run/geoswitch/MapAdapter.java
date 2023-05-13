package com.run.geoswitch;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyViewHolder>{
    private Context context;
    private ArrayList item1,item4;
    SQLiteDatabase db;
    Dialog dialog;
    int radiusXX = 100;

    public MapAdapter(Context context, ArrayList name, ArrayList radius) {
        this.context = context;
        this.item1 = name;
        this.item4 = radius;
    }

    @NonNull
    @Override
    public MapAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.runtestmap,parent,false);
        return new MapAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MapAdapter.MyViewHolder holder, int position) {
        holder.name.setText(String.valueOf(item1.get(position)));
        holder.radius.setText(String.valueOf(item4.get(position)));
        MapDatabaseHelper dBmain=new MapDatabaseHelper(context);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dBmain.getWritableDatabase();
                String hhh= (String) (holder.name.getText()).toString();
                Toast.makeText(context, hhh, Toast.LENGTH_SHORT).show();
                long delele = db.delete(MapDatabaseHelper.TABLE_NAME, MapDatabaseHelper.COL2 + "=" + "\"" + hhh + "\"", null);
                if (delele != -1){
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    ((location_home)context).refreshPage();
                }
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dBmain.getWritableDatabase();
                String hhh= (String) (holder.name.getText()).toString();
                ((location_home)context).editRadius(hhh);
            }
        });
    }


    @Override
    public int getItemCount() {
        return item1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,radius;
        ImageButton delete,edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.run1name);
            radius = itemView.findViewById(R.id.textView2map);
            delete = itemView.findViewById(R.id.imageButton);
            edit = itemView.findViewById(R.id.imageButton2);
        }
    }
}
