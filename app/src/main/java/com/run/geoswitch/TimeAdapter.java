package com.run.geoswitch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.Arrays;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder>{
    private Context context,zxc;
    private ArrayList item1,item2,item3;
    SQLiteDatabase db;

    public TimeAdapter(Context context, ArrayList name, ArrayList timea, ArrayList timeb) {
        this.context = context;
        this.item1 = name;
        this.item2 = timea;
        this.item3 = timeb;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.runtesttime,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(String.valueOf(item1.get(position)));
        holder.timea.setText(String.valueOf(item2.get(position)));
        holder.timeb.setText(String.valueOf(item3.get(position)));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            TimeDatabaseHelper dBmain=new TimeDatabaseHelper(context);
            @Override
            public void onClick(View view) {
                db = dBmain.getWritableDatabase();
                String hhh= (String) (holder.name.getText()).toString();
                //Toast.makeText(context, hhh, Toast.LENGTH_SHORT).show();
                Cursor dataxx = db.rawQuery("SELECT * FROM " + TimeDatabaseHelper.TABLE_NAME + " where " + TimeDatabaseHelper.COL2 + " = " + "\"" + hhh + "\" ;", null);
                dataxx.moveToFirst();
                int qwe= Integer.parseInt(dataxx.getString(4));
                //Toast.makeText(context, ""+ Arrays.toString(dataxx.getColumnNames()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, ""+qwe, Toast.LENGTH_SHORT).show();
                zxc = holder.itemView.getContext();
                AlarmManager alarmManager = (AlarmManager) zxc.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(zxc, SilentReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(zxc, qwe, intent, PendingIntent.FLAG_MUTABLE);
                alarmManager.cancel(pendingIntent);
                PendingIntent.getBroadcast(zxc, qwe, intent, PendingIntent.FLAG_MUTABLE).cancel();
                dataxx.close();

                 long delele = db.delete(TimeDatabaseHelper.TABLE_NAME, TimeDatabaseHelper.COL2 + "=" + "\"" + hhh + "\"", null);
                 if (delele != -1){
                     Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                     //item1.remove(position);
                     //notifyDataSetChanged();
                     ((time_home)context).refreshPage();
                 }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, timea, timeb;
        ImageButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.run1name);
            timea = itemView.findViewById(R.id.run1time1);
            timeb = itemView.findViewById(R.id.run1time2);
            delete = itemView.findViewById(R.id.imageButton);
        }
    }
}
