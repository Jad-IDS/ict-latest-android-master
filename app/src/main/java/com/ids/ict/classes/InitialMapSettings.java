package com.ids.ict.classes;

public class InitialMapSettings {
	private String locationX;
	private String locationY;
	private String zoomSettings;
	private boolean showServiceProvider;
	private String threshold;

	public String getLocationX() {
		return locationX;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String tg) {
		this.threshold = tg;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getZoomSettings() {
		return zoomSettings;
	}

	public void setZoomSettings(String zoomSettings) {
		this.zoomSettings = zoomSettings;
	}

	public boolean isShowServiceProvider() {
		return showServiceProvider;
	}

	public void setShowServiceProvider(boolean showServiceProvider) {
		this.showServiceProvider = showServiceProvider;
	}

}
