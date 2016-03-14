package com.jxust.englishdict;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	String CREATE_TABLE_SQL = "create table dict(_id integer primary key autoincrement, word, detail)";

	public MyDBHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// ��һ��ʹ�����ݿ�ʱ�������ݱ�
		db.execSQL(CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// ���汾����ʱ���ôη���
		System.out.println("-----onUpdate Called-----" + oldVersion + "---->" + newVersion);
	}

}
