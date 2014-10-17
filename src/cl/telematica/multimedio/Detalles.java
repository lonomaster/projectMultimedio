package cl.telematica.multimedio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import cl.telematica.multimedio.app.AppController;
import cl.telematica.multimedio.model.Movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Detalles  extends Activity {
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles);

		//Localizar los controles
		TextView name = (TextView)findViewById(R.id.Name);
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView picturepath = (NetworkImageView) this
				.findViewById(R.id.Picturepath);
		TextView price = (TextView)findViewById(R.id.Price);
		TextView description = (TextView)findViewById(R.id.Description);
		TextView tienda = (TextView)findViewById(R.id.Tienda);
		TextView user = (TextView)findViewById(R.id.User);
		TextView direccion = (TextView)findViewById(R.id.Direccion);
		TextView telefono = (TextView)findViewById(R.id.Telefono);
		//Recuperamos la informaci�n pasada en el intent
		final Bundle bundle = this.getIntent().getExtras();


		name.setText(bundle.getString("name"));

		picturepath.setImageUrl(bundle.getString("picturepath"), imageLoader);

		price.setText("$"+bundle.getString("price"));
		description.setText("Descripción: "+bundle.getString("description"));
		tienda.setText("Local: "+bundle.getString("tienda"));
		user.setText("Email: "+bundle.getString("user"));
		direccion.setText("Dirección: "+bundle.getString("direccion"));
		telefono.setText("Telefono: "+bundle.getString("telefono"));
		
		 final Button button = (Button) findViewById(R.id.Buttom);
         button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	//Creamos el Intent
             	Intent intent = new Intent(Detalles.this, Mapa.class);
             	
             	//Creamos la informaci�n a pasar entre actividades
             	Bundle b = new Bundle(); 
            
             	b.putString("latitud", bundle.getString("latitud"));
             	b.putString("longitud", bundle.getString("longitud"));
             	
             	//A�adimos la informaci�n al intent
             	intent.putExtras(b);
             	
             	//Iniciamos la nueva actividad
                 startActivity(intent);   
             }
         });

		Thread t = new Thread() {

			public void run() {
				Looper.prepare(); //For Preparing Message Pool for the child Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {
					HttpPost post = new HttpPost("http://superoffer.cl/admin/login/actualizar/");


					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  
					nameValuePairs.add(new BasicNameValuePair("id", bundle.getString("id")));  
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 


					response = client.execute(post);

					/*Checking response */
					if(response!=null){
						String temp = EntityUtils.toString(response.getEntity());
						Log.i("tag", temp);      }

				} catch(Exception e) {
					e.printStackTrace();

				}

				Looper.loop(); //Loop in the message queue
			}
		};

		t.start();    


	}




}

