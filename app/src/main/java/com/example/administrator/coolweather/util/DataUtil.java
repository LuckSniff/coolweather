package com.example.administrator.coolweather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/11/18.
 */

public class DataUtil extends SQLiteOpenHelper {

    public static final String CREATE_PROVINCE="create table Province(\n" +
            "\tprovinceId int not null,\n" +
            "\tprovinceName char(10) not null,\n" +
            "\tprimary key(provinceId)\n" +
            ");";

    public static final String CREATE_CITY="create table City(\n" +
            "\tcityId int not null,\n" +
            "\tcityName char(10) not null,\n" +
            "\tprovinceId int not null,\n" +
            "\tforeign key (provinceId) references Province(provinceId),\n" +
            "\tprimary key(cityId)\n" +
            ");";

    public static final String CREATE_COUNTY="create table County(\n" +
            "\tcountyId int not null,\n" +
            "\tcountyName char(10) not null,\n" +
            "\tweatherId varchar(30) not null,\n" +
            "\tcityId int not null,\n" +
            "\tforeign key (cityId) references City(cityId),\n" +
            "\tprimary key(countyId)\n" +
            ");";

    public DataUtil(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
