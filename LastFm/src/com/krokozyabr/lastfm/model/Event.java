package com.krokozyabr.lastfm.model;

public class Event {
	
	private long id;
	private String title;
	private Venue venue;
	private String headliner;
	
	public Event(){
		venue = new Venue();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public String getHeadliner() {
		return headliner;
	}

	public void setHeadliner(String headliner) {
		this.headliner = headliner;
	}
}
