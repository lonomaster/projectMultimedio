package cl.telematica.multimedio;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import cl.telematica.multimedio.app.AppController;
import cl.telematica.multimedio.model.Movie;

import android.app.Activity;
import android.os.Bundle;
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
	TextView id = (TextView)findViewById(R.id.Id);
	TextView price = (TextView)findViewById(R.id.Price);
	TextView description = (TextView)findViewById(R.id.Description);
	TextView tienda = (TextView)findViewById(R.id.Tienda);
	TextView user = (TextView)findViewById(R.id.User);
	TextView direccion = (TextView)findViewById(R.id.Direccion);
	TextView firstname = (TextView)findViewById(R.id.Firstname);
	TextView lastname = (TextView)findViewById(R.id.Lastname);
	TextView telefono = (TextView)findViewById(R.id.Telefono);
	TextView latitud = (TextView)findViewById(R.id.Latitud);
	TextView longitud = (TextView)findViewById(R.id.Longitud);
    //Recuperamos la informaci�n pasada en el intent
    Bundle bundle = this.getIntent().getExtras();
   

    name.setText(bundle.getString("name"));
    String url = "http://maps.googleapis.com/maps/api/staticmap?center="+Double.valueOf(bundle.getString("latitud"))+","+Double.valueOf(bundle.getString("longitud"))+"&zoom=16&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+Double.valueOf(bundle.getString("latitud"))+","+Double.valueOf(bundle.getString("longitud"))+"&sensor=false";
    picturepath.setImageUrl(bundle.getString("picturepath"), imageLoader);
    mapa.setImageUrl(url, imageLoader);
    id.setText(bundle.getString("id"));
    price.setText(bundle.getString("price"));
    description.setText(bundle.getString("description"));
    tienda.setText(bundle.getString("tienda"));
    user.setText(bundle.getString("user"));
    direccion.setText(bundle.getString("direccion"));
    firstname.setText(bundle.getString("firstname"));
    lastname.setText(bundle.getString("lastname"));
    telefono.setText(bundle.getString("telefono"));
    latitud.setText(bundle.getString("latitud"));
    longitud.setText(bundle.getString("longitud"));
    
	
}




}

