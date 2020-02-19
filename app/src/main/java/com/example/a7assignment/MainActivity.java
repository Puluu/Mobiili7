package com.example.a7assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public class Kuuntelija extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){

            Toast.makeText(context, "asd", Toast.LENGTH_LONG).show();

            Date currentTime = Calendar.getInstance().getTime();
            MyEntity myEntity = new MyEntity();

            if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                myEntity.teksti = "Näyttö kiinni ";
                myEntity.aika = currentTime.toString();
            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                myEntity.teksti = "Näyttö auki ";
                myEntity.aika = currentTime.toString();
            }
            Intent i = new Intent(MainActivity.this, IntentPalvelu.class);
            i.putExtra("kissa", myEntity);
            context.startService(i);

        }
    }
    Kuuntelija kuuntelija;
    ListView listView;
    Database tietokanta;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> stringLista;
    MyTableDao myTableDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = findViewById(R.id.listView1);

        stringLista = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, stringLista);
        listView.setAdapter(arrayAdapter);

        tietokanta = Room.databaseBuilder(getApplicationContext(), Database.class,Database.NIMI).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        myTableDao = tietokanta.myTableDao();

        kuuntelija = new Kuuntelija();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(kuuntelija, intentFilter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        List<MyEntity> lista = myTableDao.getAllInDescendingOrder();
        for (MyEntity t : lista){
            String s = "";
            s = s + t.teksti + t.aika;
            arrayAdapter.add(s);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(kuuntelija);
    }
}
