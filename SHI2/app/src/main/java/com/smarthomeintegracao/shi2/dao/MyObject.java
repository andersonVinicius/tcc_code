package com.smarthomeintegracao.shi2.dao;

import android.util.Log;

public class MyObject {
	 
    private static final String TAG = null;
	public String objectName;
 
    // constructor for adding sample data
    public MyObject(String objectName){
         
        this.objectName = objectName;
        Log.e(TAG, "User input Aqui em MyObject :  " + objectName);

    }
 
}