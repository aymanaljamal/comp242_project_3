package application;

import java.util.Objects;

public class District implements Comparable<District> {
    private String name;
    private LinkedLists<String> locations;

    public District(String name) {
        this.name = name;
        this.locations = new LinkedLists<>();
    }

    public String getName() {
        return name;
    }

    public LinkedLists<String> getLocations() {
        return locations;
    }

    public void addLocation(String location) {
        if (locations.find(location)==null) {
            locations.insert(location);
        }
    }
	@Override
	public boolean equals(Object obj) {
		
		District other = (District) obj;
		return other.getName().equals(this.getName());
	}

	@Override
	public int compareTo(District o) {
		
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return "" + name ;
	}
	
}
