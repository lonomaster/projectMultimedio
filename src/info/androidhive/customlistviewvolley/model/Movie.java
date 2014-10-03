package info.androidhive.customlistviewvolley.model;

//import java.util.ArrayList;

public class Movie {
	private String Name, Picturepath;
	private String Id;
	private String Price;
	private String Description;

	public Movie() {
	}

	public Movie(String name, String Picturepath, String Id, String Price,
			String Description) {
		this.Name = name;
		this.Picturepath = Picturepath;
		this.Id = Id;
		this.Price = Price;
		this.Description = Description;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getPicturepath() {
		return Picturepath;
	}

	public void setPicturepath(String Picturepath) {
		this.Picturepath = Picturepath;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String Price) {
		this.Price = Price;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

}
