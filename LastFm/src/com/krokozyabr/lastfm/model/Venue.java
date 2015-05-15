package com.krokozyabr.lastfm.model;

public class Venue {

	private long id;
	private String name;
	private Location location;
	
	public Venue(){
		location = new Location();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
