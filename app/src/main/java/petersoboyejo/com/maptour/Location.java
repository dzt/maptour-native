package petersoboyejo.com.maptour;

import java.io.Serializable;

public class Location implements Serializable {

    private String name, wikiLink;
    private double longitude, latitude;

    public Location(String name, String wikiLink, double latitude, double longitude) {
        this.name = name;
        this.wikiLink = wikiLink;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public String getWikiLink() {
        return this.wikiLink;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

}
