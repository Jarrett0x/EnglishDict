package com.jxust.englishdict;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ListView lv_show = (ListView) findViewById(R.id.lv_show);
		// ��ȡIntent���������
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// ��Bundle�����ݰ���ȡ������
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = (List<Map<String, String>>) bundle
				.getSerializable("data");

		if(list == null || list.size() == 0){
			TextView tv_title = (TextView) findViewById(R.id.tv_title);
			tv_title.setText("���ʱ��ﻹû�е���Ӵ");
			return;
		}else{
			// ��List��װ��SimpleAdapter
			SimpleAdapter adapter = new SimpleAdapter(ResultActivity.this, list,
					R.layout.adapter_line, new String[] { "word", "detail" },
					new int[] { R.id.line_tv_word, R.id.line_tv_detaile });
			// ���ListView
			lv_show.setAdapter(adapter);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
