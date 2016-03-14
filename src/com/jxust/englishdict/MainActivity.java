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
		// 创建MyDBHelper对象，指定版本为1,
		// 数据库文件会自动保存在程序的数据文件夹下的database目录下
		dbHelper = new MyDBHelper(this, "myDict.db3", 1);
		bt_insert = (Button) findViewById(R.id.bt_insert);
		bt_search = (Button) findViewById(R.id.bt_search);

		bt_insert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户输入
				String word = ((EditText) findViewById(R.id.et_word)).getText().toString();
				String detail = ((EditText) findViewById(R.id.et_detail)).getText().toString();
				// 插入数据
				boolean insert = insertData(dbHelper.getWritableDatabase(),
						word, detail);
				// 显示提示信息
				if (insert)
					Toast.makeText(MainActivity.this, "添加生词成功", 8000).show();
			}
		});

		bt_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户输入
				String key = ((EditText) findViewById(R.id.et_search))
						.getText().toString();
				// 执行查询
				Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
						"select * from dict where word like ? or detail like ?",
								new String[] { "%" + key + "%", "%" + key + "%" });

				// 两个Activity组件之间传递信息
				// 创建一个Bundle对象
				Bundle data = new Bundle();
				data.putSerializable("data", coverCursorToList(cursor));
				Intent intent = new Intent(MainActivity.this,
						ResultActivity.class);
				intent.putExtras(data);
				// 启动Activity
				startActivity(intent);
			}
		});
	}

	protected ArrayList<Map<String, String>> coverCursorToList(Cursor cursor) {

		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// 遍历Cursor集合
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
			Toast.makeText(MainActivity.this, "输入内容不可为空！", Toast.LENGTH_SHORT)
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
		// 推出程序时关闭MyDBHelper里的SQLiteDatabase
		if (dbHelper != null)
			dbHelper.close();
	}

}
