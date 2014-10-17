package cl.telematica.multimedio;

import cl.telematica.multimedio.adater.CustomListAdapter;
import cl.telematica.multimedio.app.AppController;
import cl.telematica.multimedio.model.Movie;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;



public class MainActivity extends Activity {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// Movies json url
	public String url = "http://superoffer.cl/admin/login/products/";
	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<Movie>();
	private ListView listView;
	private CustomListAdapter adapter;

	private LocationManager locManager;
	private LocationListener locListener;
	private TextView lblLatitud; 
	private TextView lblLongitud;
	private TextView lblPrecision;
	private TextView lblEstado;
	private EditText editText;
	public String currentLocation = "Direccion";
	public String latitud = "";
	public String longitud = "";
	private MiTareaAsincrona tarea1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// changing action bar color
				getActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor("#00A0B5")));
		listView = (ListView) findViewById(R.id.list);
		adapter = new CustomListAdapter(this, movieList);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		
	       
        

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
                 public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
            	
            	
          	  Object o = adapter.getItem(position);
                               Movie obj_itemDetails = (Movie)o;
                             //  Toast.makeText(MainActivity.this, "You have chosen : " + " " + obj_itemDetails.getName(), Toast.LENGTH_LONG).show();

      
            	
            	//Creamos el Intent
            	Intent intent = new Intent(MainActivity.this, Detalles.class);
            	
            	//Creamos la informaci�n a pasar entre actividades
            	Bundle b = new Bundle(); 
            	b.putString("name", obj_itemDetails.getName());
            	b.putString("picturepath", obj_itemDetails.getPicturepath());
            	b.putString("logo", obj_itemDetails.getLogo());
            	b.putString("id", obj_itemDetails.getId());
            	b.putString("price", obj_itemDetails.getPrice());
            	b.putString("description", obj_itemDetails.getDescription());
            	b.putString("tienda", obj_itemDetails.getTienda());
            	b.putString("user", obj_itemDetails.getUser());
            	b.putString("direccion", obj_itemDetails.getDireccion());
            	b.putString("firstname", obj_itemDetails.getFirstname());
            	b.putString("lastname", obj_itemDetails.getLastname());
            	b.putString("telefono", obj_itemDetails.getTelefono());
            	b.putString("latitud", obj_itemDetails.getLatitud());
            	b.putString("longitud", obj_itemDetails.getLongitud());
            	
            	//A�adimos la informaci�n al intent
            	intent.putExtras(b);
            	
            	//Iniciamos la nueva actividad
                startActivity(intent);   
             }  
      });
        
        comenzarLocalizacion();
		
		final EditText editTxt = (EditText) findViewById(R.id.editText1); 
		editTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
				if (count < before) {
					// We're deleting char so we need to reset the adapter data
					adapter.resetData();
				}
					
				adapter.getFilter().filter(s.toString());
				//text.setText(s.toString());
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
			}); 
	
		
		
		
		
		
		 

		
	}

	
	
	private void comenzarLocalizacion()
    {
    	//Obtenemos una referencia al LocationManager
    	locManager = 
    		(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	//Obtenemos la �ltima posici�n conocida
    	Location loc = 
    		locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	
    	//Mostramos la �ltima posici�n conocida
    	mostrarPosicion(loc);
    	
    	//Nos registramos para recibir actualizaciones de la posici�n
    	/*locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		lblEstado.setText("Provider OFF");
	    	}
	    	public void onProviderEnabled(String provider){
	    		lblEstado.setText("Provider ON ");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    		lblEstado.setText("Provider Status: " + status);
	    	}
    	};
    	
    	locManager.requestLocationUpdates(
    			LocationManager.GPS_PROVIDER, 30000, 0, locListener);*/
    }

    private void mostrarPosicion(Location loc) {
    	if(loc != null)
    	{
    		   	
    		tarea1 = new MiTareaAsincrona();
			tarea1.execute(loc.getLatitude(),loc.getLongitude());
			latitud = String.valueOf(loc.getLatitude());
			longitud = String.valueOf(loc.getLongitude());
    		/*lblLatitud.setText("Latitud: "  + String.valueOf(loc.getLatitude()));
    		lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
    		lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));*/
    		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    	}
    	else
    	{
    		/*lblLatitud.setText("Latitud: (sin_datos)");
    		lblLongitud.setText("Longitud: (sin_datos)");
    		lblPrecision.setText("Precision: (sin_datos)");*/
    	}
    }
	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
        item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
            	adapter.destroy();
           	 comenzarLocalizacion();
                return true;
            }
        });
        return true;
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case R.id.action_refresh:
			// refresh
			adapter.destroy();
		  	 comenzarLocalizacion();
			return true;
		case R.id.action_help:
			// help action
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
	
	private class MiTareaAsincrona extends AsyncTask<Double, Integer, Boolean> {
    	
    	@Override
    	protected Boolean doInBackground(Double... params) {
    		
    		
    		try {
				currentLocation = getCurrentLocationViaJSON(params[0], params[1]);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		return true;
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    	
    	}
    	
    	@Override
    	protected void onPreExecute() {
   
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result) {
    		if(result){
    			Toast.makeText(MainActivity.this, "Ubicación Localizada: "+currentLocation, Toast.LENGTH_SHORT).show();
    			//lblEstado.setText("Direccion:"+currentLocation);
    			
    			currentLocation = currentLocation.replace(" ", "%20");
    			String[] parts = currentLocation.split("-");
    			String currentLocationNew = parts[0];
   
    			String posicion = latitud + "a" + longitud;
    			url = url + posicion;
    			Log.i("TEST", currentLocation);
    			// Creating volley request obj
    			JsonArrayRequest movieReq = new JsonArrayRequest(url,
    					new Response.Listener<JSONArray>() {
    						@Override
    						public void onResponse(JSONArray response) {
    							Log.d(TAG, response.toString());
    							hidePDialog();

    							// Parsing json
    							for (int i = 0; i < response.length(); i++) {
    								try {

    									JSONObject obj = response.getJSONObject(i);
    									Movie movie = new Movie();
    									movie.setName(obj.getString("name"));
    									movie.setPicturepath(obj.getString("images"));
    									movie.setLogo(obj.getString("logo"));
    									movie.setPrice(obj.getString("price"));
    											//.doubleValue());
    									movie.setId(obj.getString("id"));

    									// Description is json array
    									/*JSONArray DescriptionArry = obj.getJSONArray("description");
    									ArrayList<String> Description = new ArrayList<String>();
    									for (int j = 0; j < DescriptionArry.length(); j++) {
    										Description.add((String) DescriptionArry.get(j));
    									}*/
    									movie.setDescription(obj.getString("description"));
    									movie.setTienda(obj.getString("tienda"));
    									movie.setUser(obj.getString("user"));
    									movie.setDireccion(obj.getString("direccion"));
    									movie.setFirstname(obj.getString("firstname"));
    									movie.setLastname(obj.getString("lastname"));
    									movie.setTelefono(obj.getString("telefono"));
    									movie.setLatitud(obj.getString("latitud"));
    									movie.setLongitud(obj.getString("longitud"));

    									// adding movie to movies array
    									movieList.add(movie);

    								} catch (JSONException e) {
    									e.printStackTrace();
    								}

    							}

    							// notifying list adapter about data changes
    							// so that it renders the list view with updated data
    							adapter.notifyDataSetChanged();
    						
    						}
    					}, new Response.ErrorListener() {
    						@Override
    						public void onErrorResponse(VolleyError error) {
    							VolleyLog.d(TAG, "Error: " + error.getMessage());
    							hidePDialog();

    						}
    					});

    			// Adding request to request queue
    			AppController.getInstance().addToRequestQueue(movieReq);
    		}
    	}
    	
    	@Override
    	protected void onCancelled() {
    		Toast.makeText(MainActivity.this, "GPS cancelado!", Toast.LENGTH_SHORT).show();
    	}
    }
    
	

	public static JSONObject getLocationInfo(double lat, double lng) {

	    HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }

	    return jsonObject;
	}

	public static String getCurrentLocationViaJSON(double lat, double lng) throws Throwable {

	    JSONObject jsonObj = getLocationInfo(lat, lng);
	    Log.i("JSON string =>", jsonObj.toString());

	    String currentLocation = "testing";
	    String street_address = null;
	    String postal_code = null; 

	    try {
	        String status = jsonObj.getString("status").toString();
	        Log.i("status", status);

	        if(status.equalsIgnoreCase("OK")){
	            JSONArray results = jsonObj.getJSONArray("results");
	            int i = 0;
	            Log.i("i", i+ "," + results.length() ); //TODO delete this
	            do{

	                JSONObject r = results.getJSONObject(i);
	                JSONArray typesArray = r.getJSONArray("types");
	                String types = typesArray.getString(0);

	                if(types.equalsIgnoreCase("street_address")){
	                    street_address = r.getString("formatted_address");
	                    Log.i("street_address", street_address);
	                }else if(types.equalsIgnoreCase("postal_code")){
	                    postal_code = r.getString("formatted_address");
	                    Log.i("postal_code", postal_code);
	                }

	                if(street_address!=null){
	                    currentLocation = street_address;
	                    Log.i("Current Location =>", currentLocation); //Delete this
	                    i = results.length();
	                }

	                i++;
	            }while(i<results.length());

	            Log.i("JSON Geo Locatoin =>", currentLocation);
	            return currentLocation;
	        }

	    } catch (JSONException e) {
	        Log.e("testing","Failed to load JSON");
	        e.printStackTrace();
	    }
	    return null;
	}

}
