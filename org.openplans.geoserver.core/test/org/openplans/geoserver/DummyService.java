package org.openplans.geoserver;

public class DummyService {

	public boolean executed = false;
	public boolean setter = false;
	
	public void dummy() {
		executed = true;
	}
	
	public void setDummy(String dummy) {
		setter = true;
	}
}
