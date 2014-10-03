package info.androidhive.customlistviewvolley.adater;

import info.androidhive.customlistviewvolley.R;
import info.androidhive.customlistviewvolley.app.AppController;
import info.androidhive.customlistviewvolley.model.Movie;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Movie> movieItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Movie> movieItems) {
		this.activity = activity;
		this.movieItems = movieItems;
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
		TextView Description = (TextView) convertView.findViewById(R.id.Description);
		//TextView Id = (TextView) convertView.findViewById(R.id.releaseId);

		// getting movie data for the row
		Movie m = movieItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getPicturepath(), imageLoader);
		
		// Name
		Name.setText(m.getName());
		
		// Price
		Price.setText("Price: " + String.valueOf(m.getPrice()));
		
		Description.setText("Descripcion: " + String.valueOf(m.getDescription()));

		// release Id
	//	Id.setText(String.valueOf(m.getId()));

		return convertView;
	}

}