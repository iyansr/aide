package com.eofel.wp.utils;

public class ItemContent {
	
	private String name;
	
	private String url;

	public ItemContent(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public ItemContent() {
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
