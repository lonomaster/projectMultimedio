package cl.telematica.multimedio.model;

//import java.util.ArrayList;

public class Movie {
	private String Name;
	private String Picturepath;
	private String Id;
	private String Price;
	private String Description;
	private String Tienda;
	private String User;
	private String Direccion;
	private String Firstname;
	private String Lastname;
	private String Telefono;
	private String Latitud;
	private String Longitud;
	

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
	
	public String getTienda() {
		return Tienda;
	}

	public void setTienda(String tienda) {
		this.Tienda = tienda;
	}
	
	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		this.User = user;
	}
	
	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		this.Direccion = direccion;
	}
	
	public String getFirstname() {
		return Firstname;
	}

	public void setFirstname(String firstname) {
		this.Firstname = firstname;
	}
	
	public String getLastname() {
		return Lastname;
	}

	public void setLastname(String lastname) {
		this.Lastname = lastname;
	}
	
	public String getTelefono() {
		return Telefono;
	}

	public void setTelefono(String telefono) {
		this.Telefono = telefono;
	}
	
	public String getLatitud() {
		return Latitud;
	}

	public void setLatitud(String latitud) {
		this.Latitud = latitud;
	}
	
	public String getLongitud() {
		return Longitud;
	}

	public void setLongitud(String longitud) {
		this.Longitud = longitud;
	}

}
