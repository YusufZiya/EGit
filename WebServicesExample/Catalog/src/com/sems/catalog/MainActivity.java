package com.sems.catalog;

import java.util.ArrayList;
import java.util.List;

import com.sems.catalog.entity.Flower;
import com.sems.catalog.xmlparser.FlowerXMLParser;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView tvOutput;
	ProgressBar pb;
	Button btnBasla;
	List<MyTask> tasks;
	List<Flower> flowerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tasks = new ArrayList<MyTask>();

		tvOutput = (TextView) findViewById(R.id.tvOutput);
		tvOutput.setMovementMethod(new ScrollingMovementMethod());

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		btnBasla = (Button) findViewById(R.id.btnStart);
		btnBasla.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isOnline()) {

					getData("http://services.hanselandpetal.com/feeds/flowers.xml");

				} else {

					Toast.makeText(getApplicationContext(),
							"Network isn't available", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	private void getData(String uri) {
		MyTask task = new MyTask();
		task.execute(uri);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {

			Toast.makeText(getApplicationContext(), "Hello Android",
					Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateDisplay(List<Flower> flowers) {

		if (flowers != null) {

			for (Flower flower : flowers) {

				tvOutput.append(flower.getName() + "\n");
			}
		}
	}

	private boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	public class MyTask extends AsyncTask<String, String, String> {
		// doinBackground metodundan önce çalýþacak metoddur.
		@Override
		protected void onPreExecute() {

			// updateDisplay("Task Started");

			if (tasks.size() == 0) {

				pb.setVisibility(View.VISIBLE);
			}

			tasks.add(this);
		}

		// Ýlk parametre doInBackground metodunun alacaðý parametredir.Son
		// parametre ise geriye dönüþ tipidir.
		@Override
		protected String doInBackground(String... params) {

			String content = HttpManager.GetData(params[0]);

			return content;
		}

		// doInBackground metodundan dönen deðeri parametre olarak alýr.
		@Override
		protected void onPostExecute(String result) {

			flowerList = FlowerXMLParser.parseFeed(result);

			updateDisplay(flowerList);

			tasks.remove(this);
			if (tasks.size() == 0) {

				pb.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {

			// updateDisplay(values[0]);
		}
	}
}
