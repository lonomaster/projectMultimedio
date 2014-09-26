package com.desarrollodroide.fragmenttrasitionextendedexample;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.app.ListFragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class tercerFragment extends ListFragment {

	private static int HTTP_TIMEOUT = 30000;
	public String result = "";
	public String url= "http://www.connectic.cl/restaurant/index.php/webserve/menu/";

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);

		return inflater.inflate(R.layout.tercer_fragment_layout, container, false);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new MyListAdapter());
		
		TextView textview1 = (TextView) getView().findViewById(R.id.textView1);
		
		textview1.setMovementMethod(new ScrollingMovementMethod());
		
		result = readStaticFeed(url);
		
		textview1.setText(result);
		
	}

	private class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 30;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView result = (TextView) convertView;
			if (result == null) {
				result = (TextView) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_view_item2, parent, false);
			}
			result.setText("My tercer fragment #" + position);

			return result;
		}
	}

	public static String readStaticFeed(String url){
		StringBuilder builder = new StringBuilder();
		HttpGet httpGet = new HttpGet(url);
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, HTTP_TIMEOUT);

		HttpClient client = new DefaultHttpClient(httpParameters);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e("error", "Failed to download file");
			}
			return builder.toString();
		} catch (ClientProtocolException e) {
			Log.i("ProcessData","Error de red");
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			Log.i("ProcessData","Error de i/o");
			e.printStackTrace();
			return "";
		}

	}

}
