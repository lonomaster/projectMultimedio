package cl.telematica.multimedio;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Mapa extends FragmentActivity {
	private GoogleMap mapa = null;
	private Marker marcador=null;
	public String JsonResponse = "";
	private Circle mCircle = null;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mapa);
    final Bundle bundle = this.getIntent().getExtras();

    JsonResponse = bundle.getString("datos");
    
    mapa = ((SupportMapFragment) getSupportFragmentManager()
			   .findFragmentById(R.id.Mapa)).getMap();
    CameraUpdate camUpd2 = 
			CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(bundle.getString("lat")), Double.valueOf(bundle.getString("lon"))), 15F);
		mapa.animateCamera(camUpd2);
		mapa.setMyLocationEnabled(true);
		mapa.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
	        @Override
	        public void onMyLocationChange(Location location) {
	            if(mCircle == null || marcador == null){
	                drawMarkerWithCircle(new LatLng(Double.valueOf(bundle.getString("lat")), Double.valueOf(bundle.getString("lon"))));
	            }else{
	                updateMarkerWithCircle(new LatLng(Double.valueOf(bundle.getString("lat")), Double.valueOf(bundle.getString("lon"))));
	            }
	        }
	    });
		
		if (marcador==null){
						
			try {
				
				JSONArray jsonArray = new JSONArray(JsonResponse);
				
				for (int i = 0; i < jsonArray.length(); i++) {
					
					JSONObject obj;
				
						obj = jsonArray.getJSONObject(i);
						if(!(obj.getString("latitud").equals(bundle.getString("latitud")) && obj.getString("longitud").equals(bundle.getString("longitud")))){
						marcador = mapa.addMarker(new MarkerOptions()
							.position(new LatLng(Double.valueOf(obj.getString("latitud")),Double.valueOf(obj.getString("longitud"))))
							.title(obj.getString("tienda")));
					
						}
						else{
							marcador = mapa.addMarker(new MarkerOptions()
				    		.position(new LatLng(Double.valueOf(bundle.getString("latitud")),Double.valueOf(bundle.getString("longitud"))))
				    		.title(bundle.getString("name"))
				    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_places)));
						}
						 

				} 
			}
			catch (Throwable t) {
				    Log.e("My App", "Could not parse malformed JSON: \"" + JsonResponse + "\"");
				}
			/*marcador = mapa.addMarker(new MarkerOptions()
    		.position(new LatLng(-33.0356,-71.5914))
    		.title("test")
    		.snippet("Population: 4,137,400"));*/
    		
			
		}
		else{
			marcador.remove();
			marcador=null;
		}
		
}


private void updateMarkerWithCircle(LatLng position) {
    mCircle.setCenter(position);
}

private void drawMarkerWithCircle(LatLng position){
    double radiusInMeters = 700.0;
    int strokeColor = 0xff33B5E5; //red outline
    int shadeColor = 0x4433B5E5; //opaque red fill

    CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
    mCircle = mapa.addCircle(circleOptions);

}
}
