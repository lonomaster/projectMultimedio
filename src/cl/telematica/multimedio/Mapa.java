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

public class Mapa  extends Activity {
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mapa);
	
	if (imageLoader == null)
		imageLoader = AppController.getInstance().getImageLoader();
	NetworkImageView mapa = (NetworkImageView) this
			.findViewById(R.id.Mapa);
    //Recuperamos la informaci�n pasada en el intent
    final Bundle bundle = this.getIntent().getExtras();
   


    String url = "http://maps.googleapis.com/maps/api/staticmap?center="+Double.valueOf(bundle.getString("latitud"))+","+Double.valueOf(bundle.getString("longitud"))+"&zoom=17&size=400x600&maptype=roadmap&markers=color:blue%7Clabel:S%7C"+Double.valueOf(bundle.getString("latitud"))+","+Double.valueOf(bundle.getString("longitud"))+"&sensor=false";
    mapa.setImageUrl(url, imageLoader);
    
    
	
}




}

