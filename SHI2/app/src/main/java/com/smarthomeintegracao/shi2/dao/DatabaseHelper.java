
package com.smarthomeintegracao.shi2.dao;


import android.content.Context;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "UPDATE PREFERENCIA";
    private static String DBPATH = "/data/data/com.smarthomeintegracao.shi2/databases/";
    private static String DBNAME = "consumer.sqlite";
    private Context context;
   // private SQLiteDatabase db;

    public DatabaseHelper(Context context) {

        super(context,"consumer.sqlite", null, 1);
        this.context = context;
        DBPATH = "/data/data/" + context.getApplicationContext().getPackageName() +"/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    private boolean checkDataBase(String caminho) {
        SQLiteDatabase db = null;

        try {
            String path = caminho;
            db = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);

            db.close();
        } catch (SQLException e) {

        }
        return db != null;
    }

    private void createDataBase() throws Exception {
        boolean exist = checkDataBase(DBPATH + DBNAME);

        if (!exist) {
            this.getReadableDatabase();
            //context.deleteDatabase("databasescapesfull1.sqlite");
            try {
                SQLiteDatabase db = null;
                copyDatabase();
                String path = DBPATH + "consumer.sqlite";
                if (checkDataBase(path)) {

                    path = DBPATH + "consumer.sqlite";
                    db = SQLiteDatabase.openDatabase(path, null,
                            SQLiteDatabase.OPEN_READWRITE);

                    db.close();

                }
                //db1.close();


            } catch (IOException e) {
                throw new Error("Não foi possivel copiar o arquivo");
            }
        }
    }

    private void copyDatabase() throws IOException {

        String dbPath = DBPATH + DBNAME;
        OutputStream dbStream = new FileOutputStream(dbPath);

        for (int i = 1; i <= 1; i++) {

            InputStream dbInputStream = context.getAssets().open("consumer.sqlite");
            //InputStream dbInputStream = context.getAssets().open("xa" + i);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInputStream.read(buffer)) > 0) {
                dbStream.write(buffer, 0, length);
            }
            dbInputStream.close();
        }
        Log.e(TAG, "@@@ O BAnco foi criado!!!!!! : " + dbPath);

        dbStream.flush();
        dbStream.close();

    }

    public SQLiteDatabase getDatabase() {

        try {
            createDataBase();

            String path = DBPATH + DBNAME;
            Log.e(TAG, "@@@ O BAnco acessado  foi!!!!!! : " + path);
            return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            return this.getWritableDatabase();
        }
    }



}
