package cl.telematica.multimedio;

import cl.telematica.multimedio.adater.adapterProduct;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.location.Criteria;
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



public class Productos extends Activity {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// Movies json url
	
	private ProgressDialog pDialog;
	private ArrayList<Movie> movieList = new ArrayList<Movie>();
	private ListView listView;
	private adapterProduct adapter;

	private LocationManager locManager;
	private LocationListener locListener;
	private LocationManager manejador;
	private Location mejorLocaliz;
	private TextView lblLatitud; 
	private TextView lblLongitud;
	private TextView lblPrecision;
	private TextView lblEstado;
	private EditText editText;
	public int cate_actual = 200;
	public String currentLocation = "Direccion";
	public double latitud;
	public double longitud;
	public String lat;
	public String lon;
	private boolean mensaje = true;
	public String JsonResponse = "";
	public String Email = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos);
		
		final Bundle bundle = this.getIntent().getExtras();
		
		Email = bundle.getString("user");
		Email = Email.replace("@", "qqqqq");
		Log.i("Email", Email);
				if (!exiteConexionInternet()){
					 AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
				        dialogo1.setTitle("Importante");  
				        dialogo1.setMessage("Se necesita de conexión a internet");            
				        dialogo1.setCancelable(false);  
				        dialogo1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				            public void onClick(DialogInterface dialogo1, int id) {  
				                finish();  
				            }  
				        });              
				        dialogo1.show(); 
				}
				else {
				
		listView = (ListView) findViewById(R.id.list);
		adapter = new adapterProduct(this, movieList);
		listView.setAdapter(adapter);

		
	       
		comenzarLocalizacion();

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
                 public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
            	
            	
          	  Object o = adapter.getItem(position);
                               Movie obj_itemDetails = (Movie)o;
                             //  Toast.makeText(MainActivity.this, "You have chosen : " + " " + obj_itemDetails.getName(), Toast.LENGTH_LONG).show();

      
            	
            	//Creamos el Intent
            	Intent intent = new Intent(Productos.this, Detalles.class);
            	
            	//Creamos la informaci�n a pasar entre actividades
            	Bundle b = new Bundle(); 
            	
            	b.putString("picturepath", obj_itemDetails.getPicturepath());
            	b.putString("logo", obj_itemDetails.getLogo());
            	b.putString("id", obj_itemDetails.getId());
            	b.putString("price", obj_itemDetails.getPrice());
            	b.putString("description", obj_itemDetails.getDescription());
            	b.putString("tienda", obj_itemDetails.getTienda());
            	b.putString("user", obj_itemDetails.getUser());
            	b.putString("direccion", obj_itemDetails.getDireccion());
            	b.putString("visitas", obj_itemDetails.getVisitas());
            	b.putString("fecha", obj_itemDetails.getFecha());
            	b.putString("firstname", obj_itemDetails.getFirstname());
            	b.putString("lastname", obj_itemDetails.getLastname());
            	b.putString("telefono", obj_itemDetails.getTelefono());
            	b.putString("name", obj_itemDetails.getName());
            	b.putString("latitud", obj_itemDetails.getLatitud());
            	b.putString("longitud", obj_itemDetails.getLongitud());
            	b.putString("latitud", obj_itemDetails.getLatitud());
            	b.putString("lat",lat);
            	b.putString("lon", lon);
            	b.putString("datos", JsonResponse);
            	//b.putSerializable("datos", movieList);
            	
      
            	
            //	b.pu .putStringArrayList("list", movieList);
            	//A�adimos la informaci�n al intent
            	intent.putExtras(b);
            	
            	//Iniciamos la nueva actividad
                startActivity(intent);   
             }  
      });
        
        
		
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
	
}

	
	
	private void comenzarLocalizacion()
    {
		
	
		//Obtenemos una referencia al LocationManager
    	locManager = 
    		(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    
		if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	        //Do what you need if enabled...
	    
			pDialog = new ProgressDialog(this);
			// Showing progress dialog before making http request
			pDialog.setMessage("Localizando...");
			pDialog.show();
    	//Obtenemos la �ltima posici�n conocida
    	Location loc = 
    		locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	
    	
    	//Nos registramos para recibir actualizaciones de la posici�n
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		
	    		//Toast.makeText(Productos.this, "Ubicación Localizada", Toast.LENGTH_SHORT).show();
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		Log.i("", "Provider Status: OFF ");
	    	}
	    	public void onProviderEnabled(String provider){
	    		Log.i("", "Provider Status: ON ");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    		
	    	}
    	};
    	
    	locManager.requestLocationUpdates(
    			LocationManager.NETWORK_PROVIDER, 60000,200, locListener);
		}
		else {
			 AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
		        dialogo1.setTitle("Importante");  
		        dialogo1.setMessage("Se necesita usar el servicio de ubicación de redes inalámbricas. Vaya a Config > Servicios de ubicación > Usar redes inalámbricas");            
		        dialogo1.setCancelable(false);  
		        dialogo1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialogo1, int id) {  
		                finish();  
		            }  
		        });              
		        dialogo1.show(); 
		}
   
    }

    private void mostrarPosicion(Location loc) {
    	if(loc != null)
    	{
    		hidePDialog();
    		//tarea1 = new MiTareaAsincrona();
			//tarea1.execute(loc.getLatitude(),loc.getLongitude());
    		latitud = loc.getLatitude();
			longitud = loc.getLongitude();	
			lat = String.valueOf(loc.getLatitude());
			lon = String.valueOf(loc.getLongitude());	
			
			cate_actual = 200;
			loadContent();
			adapter.notifyDataSetChanged();
    		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    	}
    	else Toast.makeText(Productos.this, "Ubicación no Localizada", Toast.LENGTH_SHORT).show();
		
 
    }
	@Override
	public void onDestroy() {
		super.onDestroy();
		locManager.removeUpdates(locListener); 
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

		case R.id.action_map:
			
			//Creamos el Intent
        	Intent intent = new Intent(Productos.this, Mapa.class);
        	
        	//Creamos la informaci�n a pasar entre actividades
        	Bundle b = new Bundle(); 
        	
        
        	b.putString("name", "432");
        	b.putString("longitud", "324");
        	b.putString("latitud","324");
        	b.putString("lat",lat);
        	b.putString("lon", lon);
        	b.putString("datos", JsonResponse);
        	//b.putSerializable("datos", movieList);
        	
  
        	
        //	b.pu .putStringArrayList("list", movieList);
        	//A�adimos la informaci�n al intent
        	intent.putExtras(b);
        	
        	//Iniciamos la nueva actividad
            startActivity(intent);  
            return true;
		case R.id.action_refresh:	
            cate_actual = 200;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.all:
			cate_actual = 200;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate1:
			cate_actual = 1;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate2:
			cate_actual = 2;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate3:
			cate_actual = 3;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate4:
			cate_actual = 4;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate5:
			cate_actual = 5;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate6:
			cate_actual = 6;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate7:
			cate_actual = 7;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate8:
			cate_actual = 8;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate9:
			cate_actual = 9;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate10:
			cate_actual = 10;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate11:
			cate_actual = 11;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate12:
			cate_actual = 12;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate13:
			cate_actual = 13;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate14:
			cate_actual = 14;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate15:
			cate_actual = 15;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate16:
			cate_actual = 16;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate17:
			cate_actual = 17;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate18:
			cate_actual = 18;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		case R.id.cate19:
			cate_actual = 19;
			loadContent();
			adapter.notifyDataSetChanged();
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
    
	private void loadContent() {
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Cargando datos de nuestro servidor...");
		pDialog.show();
		
		//lblEstado.setText("Direccion:"+currentLocation);
		
		
		/*currentLocation = currentLocation.replace(" ", "%20");
		String[] parts = currentLocation.split("-");
		String currentLocationNew = parts[0];*/
		String url = "http://superoffer.cl/admin/login/products/";
		String posicion = latitud + "qqqqq" + longitud + "qqqqq" + cate_actual + "qqqqq" + Email;
		url = url + posicion;
		Log.i("TEST", currentLocation);
		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						JsonResponse = response.toString();
						hidePDialog();
						adapter.destroy();
						// Parsing json
						if(response.length()==0){
							Toast.makeText(Productos.this, "No se encontraron ofertas", Toast.LENGTH_SHORT).show();
						}
						
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
								movie.setVisitas(obj.getString("visitas"));
								movie.setFecha(obj.getString("disable_on"));
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
	
		
	public boolean exiteConexionInternet() {
		   ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		   NetworkInfo netInfo = cm.getActiveNetworkInfo();
		   if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		      return true;
		   }
		   return false;
		}
	

}
