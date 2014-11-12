package cl.telematica.multimedio.adater;

import cl.telematica.multimedio.app.AppController;
import cl.telematica.multimedio.R;
import cl.telematica.multimedio.model.Movie;

import java.io.IOException;
import java.io.InputStream;
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
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Filter.*;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class adapterProduct extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Movie> movieItems;
	private List<Movie> originalMovieItems;
	private Filter listFilter;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public adapterProduct(Activity activity, List<Movie> movieItems) {
		this.activity = activity;
		this.movieItems = movieItems;
		this.originalMovieItems = movieItems;
	}

	@Override
	public int getCount() {
		return movieItems.size();
	}

	@Override
	public Object getItem(int location) {
		return movieItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView Name = (TextView) convertView.findViewById(R.id.Name);
		TextView Price = (TextView) convertView.findViewById(R.id.Price);
		//TextView Description = (TextView) convertView.findViewById(R.id.Description);
		//TextView Id = (TextView) convertView.findViewById(R.id.releaseId);

		// getting movie data for the row
		Movie m = movieItems.get(position);
		

		// thumbnail image
		thumbNail.setImageUrl(m.getPicturepath(), imageLoader);
		
		// Name
		Name.setText(m.getName());
		
		// Price
		Price.setText("$" + String.valueOf(m.getPrice()));
		
		//Description.setText("Descripcion: " + String.valueOf(m.getDescription()));

		// release Id
	//	Id.setText(String.valueOf(m.getId()));

		return convertView;
	}
	public void resetData() {
		movieItems = originalMovieItems;
	}
	
	public void destroy() {
		movieItems.clear();
		originalMovieItems.clear();
	}
	
	public Filter getFilter() {
		if (listFilter == null)
			listFilter = new ListFilter();
		
		return listFilter;
	}

	private class ListFilter extends Filter {

		
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = originalMovieItems;
				results.count = originalMovieItems.size();
			}
			else {
				// We perform filtering operation
				List<Movie> nMovieList = new ArrayList<Movie>();
				
				for (Movie p : movieItems) {
					if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
						nMovieList.add(p);
				}
				
				results.values = nMovieList;
				results.count = nMovieList.size();

			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				movieItems = (List<Movie>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
}