package com.jxust.englishdict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MyDBHelper dbHelper;
	private Button bt_insert;
	private Button bt_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ����MyDBHelper����ָ���汾Ϊ1,
		// ���ݿ��ļ����Զ������ڳ���������ļ����µ�databaseĿ¼��
		dbHelper = new MyDBHelper(this, "myDict.db3", 1);
		bt_insert = (Button) findViewById(R.id.bt_insert);
		bt_search = (Button) findViewById(R.id.bt_search);

		bt_insert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ȡ�û�����
				String word = ((EditText) findViewById(R.id.et_word)).getText().toString();
				String detail = ((EditText) findViewById(R.id.et_detail)).getText().toString();
				// ��������
				boolean insert = insertData(dbHelper.getWritableDatabase(),
						word, detail);
				// ��ʾ��ʾ��Ϣ
				if (insert)
					Toast.makeText(MainActivity.this, "������ʳɹ�", 8000).show();
			}
		});

		bt_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ȡ�û�����
				String key = ((EditText) findViewById(R.id.et_search))
						.getText().toString();
				// ִ�в�ѯ
				Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
						"select * from dict where word like ? or detail like ?",
								new String[] { "%" + key + "%", "%" + key + "%" });

				// ����Activity���֮�䴫����Ϣ
				// ����һ��Bundle����
				Bundle data = new Bundle();
				data.putSerializable("data", coverCursorToList(cursor));
				Intent intent = new Intent(MainActivity.this,
						ResultActivity.class);
				intent.putExtras(data);
				// ����Activity
				startActivity(intent);
			}
		});
	}

	protected ArrayList<Map<String, String>> coverCursorToList(Cursor cursor) {

		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// ����Cursor����
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("word", cursor.getString(1));
			map.put("detail", cursor.getString(2));
			result.add(map);
		}
		return result;
	}

	protected boolean insertData(SQLiteDatabase db, String word, String detail) {

		if (TextUtils.isEmpty(word) || TextUtils.isEmpty(detail)) {
			Toast.makeText(MainActivity.this, "�������ݲ���Ϊ�գ�", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			db.execSQL("insert into dict values (null, ?, ?)", new String[] { word, detail });
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �Ƴ�����ʱ�ر�MyDBHelper���SQLiteDatabase
		if (dbHelper != null)
			dbHelper.close();
	}

}
