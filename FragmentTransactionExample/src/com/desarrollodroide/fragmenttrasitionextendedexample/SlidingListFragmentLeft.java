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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONStringer;

import android.app.ListFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;



public class SlidingListFragmentLeft extends ListFragment {

	private static int HTTP_TIMEOUT = 30000;
	public String result2 = "";
	public String url= "http://www.connectic.cl/restaurant/index.php/webserve/menu/";
	private JSONObject jsonOne;
	public JSONArray arrayJson;
	

	Typeface MYRIADPRO_REGULAR = MainActivity.MYRIADPRO_REGULAR;
	Typeface myriadpro_boldit = MainActivity.myriadpro_boldit;	
	Typeface champagne_bold = MainActivity.champagne_bold;
	Typeface champagne_italic = MainActivity.champagne_italic;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//jsonOne = new JSONObject();
		//arrayJson = jsonOne.getString("menuitems");
		
		//result2 = readStaticFeed(url);
		//arrayJson.get(index);
		
		return inflater.inflate(R.layout.sliding_fragment_layout_left, container, false);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new MyListAdapter());
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
			LinearLayout result = (LinearLayout) convertView;
			//TextView textView_1;
			TextView textView_titulo ; 
			TextView TextView_descripcion;
			
			if (result == null) {
				result = (LinearLayout) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_view_item, parent, false);
			}
			//textView_1 = (TextView) result.findViewById(R.id.TextView1);
			textView_titulo = (TextView) result.findViewById(R.id.TextView_titulo);
			TextView_descripcion = (TextView) result.findViewById(R.id.TextView_descripcion);
			
			textView_titulo.setText("TITULO");
			textView_titulo.setTypeface(champagne_bold);
			
			
			//TextView_descripcion.setText(result2);
			TextView_descripcion.setText("ONeLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
			TextView_descripcion.setTypeface(champagne_italic);
			
			//textView_1.setText("My custom element #" + position);

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
