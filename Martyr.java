import java.time.LocalDate;

public class Martyr {
	private String name;
	private LocalDate date;
	private int age;
	private Location location;
	private District district;
	private char gender;

	public Martyr(String name, LocalDate date, int age, Location location, District district, char gender) {
		super();
		this.name = name;
		this.date = date;
		this.age = age;
		this.location = location;
		this.district = district;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return name + ", " + date + ", " + age + ", " + location.toString() + ", "
				+ district.getName() + ", " + gender + "\n";
	}

}
