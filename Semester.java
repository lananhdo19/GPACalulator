
public class Semester {
	private String name;
	private double gpa;
	private double credit;
	
	public Semester (String name, double c, double gpa) {
		// fully complete constructor without status passed in
		this.credit = c;
		this.name = name;
		this.gpa = gpa;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}
	
	public String toString() {
		return "Semester: " + name + "   credit: " +credit+ "   GPA: " + gpa;
	}
	
}
