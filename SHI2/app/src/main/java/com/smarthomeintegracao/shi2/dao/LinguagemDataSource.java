
package com.smarthomeintegracao.shi2.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smarthomeintegracao.shi2.dao.MyObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LinguagemDataSource {

	private static final String CATEGORIA = null;
	private static final String TAG = null;
	private SQLiteDatabase db;
	private DatabaseHelper helper; // resposavel pela manuten����o do codgo
	static private Date data;





	public LinguagemDataSource(Context context) {
		helper = new DatabaseHelper(context);
		db = helper.getDatabase();
		data = new Date();

	}



	public void  addConexao(String port, String ip) {
        Log.i("add_PORTA:", port);
        Log.i("add_IP:", ip);
        Calendar c = Calendar.getInstance();

		//if (verificarConexao(port, ip) == 0) {
			String sql = "insert into conexao (port,ip,date_connect" +
                    ") values" + "('"+port+ "','"+ip+"','"+c.getTime().toString()+"')";
			db.execSQL(sql);
            Log.i("deucerto", "Conexao Adicionada com sucesso"+c.getTime().toString());
//
//		} else {
//
//			Log.i("Deu_errado","##########Os dados ja existem em banco#################################");
//		}
	}

	public int verificarConexao(String port, String ip) {
        Log.i("verif_PORTA:", port);
        Log.i("verif_IP:", ip);

        String sql = "SELECT * FROM  conexao where port='"+port+"'AND ip='"+ip+"'";

        int count;
        count = db.rawQuery(sql, null).getCount();
        Log.i("Number:", ""+count);
        return count;

	}

//	public void deleteFavoritosAll() {
//		Log.i(CATEGORIA, "Deletou registro");
//		// String sql =
//		// "SELECT  titulo FROM tab_todo as a, tab_my_todo as b WHERE a.id=b.id_tab_todo";
//		String sql = "delete from tab_my_todo ";
//		db.execSQL(sql);
//
//		Log.i(CATEGORIA, "Deletou registro");
//	}

    //retorna porta e o ip
	public String getIp() {
        String IP=null;
        
		List<JSONObject> result = new ArrayList<JSONObject>();
		String sql = "SELECT port, ip  FROM conexao order by id DESC limit 1";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
//			JSONObject obj = new JSONObject();
//			try {
//				obj.put("", cursor.getString(0));
//				obj.put("issn",cursor.getString(1));
//				obj.put("nome_titulo", cursor.getString(2));
//				obj.put("estrato", cursor.getString(3));
//				obj.put("nome", cursor.getString(4));
//			} catch (JSONException e) {

            IP="http://"+cursor.getString(1)+":";
            IP=IP+cursor.getString(0);


			//esult.add(obj);
			cursor.moveToNext();
		}
        Log.i("verif_IP_getIp:", IP);
		cursor.close();
		return  IP;

	}









}


