package com.example.hipstersum;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialog = new ProgressDialog(this);
		numeroParrafos = (EditText) findViewById(R.id._EditText);
		Button myButton = (Button)findViewById(R.id.Button_hipster);
		myButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				String myvalue = numeroParrafos.getText().toString();
				String url = "http://hipsterjesus.com/api/?paras=" + myvalue;
				dialog.setMessage("Downloading");
				dialog.setCancelable(true);
				new  DownloadUrl().execute(url);
			} });
	}
	ProgressDialog dialog;
	EditText numeroParrafos;



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private class DownloadUrl  extends AsyncTask<String, Void, String> {
		protected void onPreExecute() {
			dialog.setProgress(0);
			dialog.setMax(100);
			dialog.show();  
		}
		protected String doInBackground(String... screenNames) {
			String result = null;
			String url = screenNames[0];
			HttpClient httpclient = new DefaultHttpClient(); 
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpGet request = new HttpGet(url);
			try     { 

				HttpResponse response = httpclient.execute(request);
				HttpEntity resEntity = response.getEntity();
				result=EntityUtils.toString(resEntity); 
			} 
			catch(Exception e1)
			{
				e1.printStackTrace();
			} 
			return result;
		}
		protected void onPostExecute(String result) {
			dialog.dismiss();
			try {
				test(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 
	}
	public void test (String cadena) throws JSONException { 
		JSONObject cadenaurl = new JSONObject(cadena);
		String cadenatext = cadenaurl.getString("text");
		final TextView myView = (TextView)findViewById(R.id.Text_Hispter);
		myView.setText(Html.fromHtml(cadenatext));
		Button myButton2 = (Button)findViewById(R.id.Button_copy);
		myButton2.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {			
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				clipboard.setText(myView.getText());
			} });
	}

}
