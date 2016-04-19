package com.autodoor.autoscrew.data;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by yangyi_it on 2015-11-26.
 */
//创建数据库、表
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "autodoor.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private static Context _context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
        DatabaseHelper._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("db onCreate:", " ... ");
        CreateSetScrew(db);
        //创建系统设置表
        //"create table SetSys()"

    }

    private void CreateSetScrew(SQLiteDatabase db) {
        //创建锁螺丝点坐标的4个缓存数据
        //cacheNum(哪个缓存点)，number(序号),x,y,z
        String sql = "create table SetScrew(cacheNum int not null ,number int not null,xn int not null, yn int not null," +
                "zn int not null, direction varchar(60) not null );";
        db.execSQL(sql);

        for (int num = 1; num <= 4; num++) {

            int xn = 250 + num - 1;
            int yn = 130 + num - 1;
            int zn = 100 + num - 1;
            String direction = "右";

            for (int i = 1; i <= 100; i++) {

                //插入操作的SQL语句
                String temp = "insert into SetScrew(cacheNum,number,xn,yn,zn,direction)" +
                        " values (" + num + "," + i + "," + xn + "," + yn + "," + zn + ",'" + direction + "');";
                db.execSQL(temp);//执行SQL语句
                xn += 50;
                yn += 30;
                zn += 0;

            }
        }

        //测试旋转。。。
        //插入操作的SQL语句
        /*String temp = "insert into SetScrew(cacheNum,number,xn,yn,zn,direction)" +
                " values (" + 5 + "," + 1 + "," + 0 + "," + 100 + "," + 0 + ",'" + true + "');";
        db.execSQL(temp);//执行SQL语句
        temp = "insert into SetScrew(cacheNum,number,xn,yn,zn,direction)" +
                " values (" + 5 + "," + i + "," + xn + "," + yn + "," + 0 + ",'" + true + "');";
        db.execSQL(temp);//执行SQL语句
        temp = "insert into SetScrew(cacheNum,number,xn,yn,zn,direction)" +
                " values (" + 5 + "," + i + "," + xn + "," + yn + "," + 0 + ",'" + true + "');";
        db.execSQL(temp);//执行SQL语句
        temp = "insert into SetScrew(cacheNum,number,xn,yn,zn,direction)" +
                " values (" + 5 + "," + i + "," + xn + "," + yn + "," + 0 + ",'" + true + "');";
        db.execSQL(temp);//执行SQL语句*/




        Cursor c = db.rawQuery("select * from SetScrew where cacheNum=?", new String[]{"4"});
        if (c.moveToLast()) {
            String xn = c.getString(c.getColumnIndex("xn"));
            Log.d("db xn:", xn);
            String yn = c.getString(c.getColumnIndex("yn"));
            Log.d("db yn:", yn);
            String zn = c.getString(c.getColumnIndex("zn"));
            Log.d("db zn:", zn);
            String direction = c.getString(c.getColumnIndex("direction"));
            Log.d("db direction:", direction);
        }

        //db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}