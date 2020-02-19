package com.example.a7assignment;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.room.Room;

public class IntentPalvelu extends IntentService {

    MyTableDao myTableDao;

    public IntentPalvelu(){
        super("palvelunNimi");
    }
    public IntentPalvelu(String name){
        super(name);
    }
    @Override
    protected void onHandleIntent (@Nullable Intent intent){
        Database tietokanta = Room.databaseBuilder(getApplicationContext(), Database.class,Database.NIMI).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        this.myTableDao = tietokanta.myTableDao();

        MyEntity myEntity = (MyEntity) intent.getSerializableExtra("kissa");
        myTableDao.InsertMyEntity(myEntity);



    }
}
