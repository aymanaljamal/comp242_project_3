package application;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Objects;

public class Martyr implements Comparable<Martyr>,Comparator<Martyr>{
    private String name;
    private Integer age;
    private String gender;
    private String district;
    private String location;
    private Calendar date;

    public Martyr(String name, Integer age, String gender, String district, String location, Calendar date) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.district = district;
        this.location = location;
        this.date = date;
    }

    public Martyr(Calendar date, String name2) {
        this.date = date;
        this.name = name2;
    }

    public Martyr() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Martyr other = (Martyr) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name + "," + age + "," + gender + "," + district + "," + location + "," + date.getTime();
    }

    @Override
    public int compareTo(Martyr o) {
        int districtComparison = this.district.compareToIgnoreCase(o.district);
        if (districtComparison != 0) {
            return districtComparison;
        }
        return this.name.compareTo(o.name);
    }

	@Override
	public int compare(Martyr o1, Martyr o2) {
		
		return o1.compareTo(o2);
	}
}
