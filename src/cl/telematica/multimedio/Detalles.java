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
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;
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
	NetworkImageView mapa = (NetworkImageView) this
			.findViewById(R.id.Mapa);
	TextView price = (TextView)findViewById(R.id.Price);
	TextView description = (TextView)findViewById(R.id.Description);
	TextView tienda = (TextView)findViewById(R.id.Tienda);
	TextView user = (TextView)findViewById(R.id.User);
	TextView direccion = (TextView)findViewById(R.id.Direccion);
	TextView telefono = (TextView)findViewById(R.id.Telefono);
    //Recuperamos la informaci�n pasada en el intent
    final Bundle bundle = this.getIntent().getExtras();
   

    name.setText(bundle.getString("name"));
    String url = "http://maps.googleapis.com/maps/api/staticmap?center="+Double.valueOf(bundle.getString("latitud"))+","+Double.valueOf(bundle.getString("longitud"))+"&zoom=16&size=1000x600&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+Double.valueOf(bundle.getString("latitud"))+","+Double.valueOf(bundle.getString("longitud"))+"&sensor=false";
    picturepath.setImageUrl(bundle.getString("picturepath"), imageLoader);
    mapa.setImageUrl(url, imageLoader);
    price.setText("$"+bundle.getString("price"));
    description.setText("Descripción: "+bundle.getString("description"));
    tienda.setText("Local: "+bundle.getString("tienda"));
    user.setText("Email: "+bundle.getString("user"));
    direccion.setText("Dirección: "+bundle.getString("direccion"));
    telefono.setText("Telefono: "+bundle.getString("telefono"));
    
    Thread t = new Thread() {

        public void run() {
            Looper.prepare(); //For Preparing Message Pool for the child Thread
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response;
            JSONObject json = new JSONObject();

            try {
                HttpPost post = new HttpPost("http://www.connectic.cl/superoffer/index.php/admin/login/actualizar/");
               
                                   
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

