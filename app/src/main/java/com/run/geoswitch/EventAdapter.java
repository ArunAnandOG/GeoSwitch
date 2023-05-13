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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{

    private Context context,zxc;
    private ArrayList item1,item3;
    SQLiteDatabase db;


    public EventAdapter(Context context, ArrayList name, ArrayList date) {
        this.context = context;
        this.item1 = name;
        this.item3 = date;

    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.runtestevent,parent,false);
        return new EventAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {
        holder.name.setText(String.valueOf(item1.get(position)));
        holder.date.setText(String.valueOf(item3.get(position)));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            eventDatabaseHelper dBmain=new eventDatabaseHelper(context);
            @Override
            public void onClick(View view) {
                db = dBmain.getWritableDatabase();
                String hhh= (String) (holder.name.getText()).toString();
                //Toast.makeText(context, hhh, Toast.LENGTH_SHORT).show();
                Cursor dataxx = db.rawQuery("SELECT * FROM " + eventDatabaseHelper.TABLE_NAME + " where " + eventDatabaseHelper.COL2 + " = " + "\"" + hhh + "\" ;", null);
                dataxx.moveToFirst();
                int qwe= Integer.parseInt(dataxx.getString(2));
                zxc = holder.itemView.getContext();
                AlarmManager alarmManager = (AlarmManager) zxc.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(zxc, SilentReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(zxc, qwe, intent, PendingIntent.FLAG_MUTABLE);
                alarmManager.cancel(pendingIntent);
                PendingIntent.getBroadcast(zxc, qwe, intent, PendingIntent.FLAG_MUTABLE).cancel();
                dataxx.close();

                long delele = db.delete(eventDatabaseHelper.TABLE_NAME, eventDatabaseHelper.COL2 + "=" + "\"" + hhh + "\"", null);
                if (delele != -1){
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    ((notification)context).refreshPage();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        ImageButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.eventrunname);
            date = itemView.findViewById(R.id.eventrundate);
            delete = itemView.findViewById(R.id.eventrundelete);
        }
    }
}
