/**Lan Anh Do ld9hu CS2110
 * Sources:
https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
https://docs.oracle.com/javase/7/docs/api/java/util/Stack.html
https://stackoverflow.com/questions/17608421/how-to-reload-a-jpanel
https://pages.collegeboard.org/how-to-convert-gpa-4.0-scale
https://stackoverflow.com/questions/21049360/why-does-jscrollpane-with-jlist-disappear-when-the-jlist-becomes-empty-after-las
https://stackoverflow.com/questions/12806278/double-decimal-formatting-in-java
http://gpacalculator.net/how-to-calculate-gpa/
 * **/


/**Assumptions
assuming gpa cannot be negative
assuming no d- grade
allowing no upper limit on credit as long as it doesnt exceed a double's capacity
	example) user can input a single 15 credit class with their semester gpa to save time
assuming courses without status defaults to current
previous and current classes accounted for in current gpa
previous does not have special calculations or treatment: basically treated like current classes
assuming 15 credits will be added as 3 anticipated courses with no grade
assuming user will input letter grades: a- or b+ which will then be converted to double

the user needs to press "Done" after entering course to see course if course list is empty 
**/
public class Course {
	private String name;
	private String grade;
	private double credit;
	private String status;
	
	public Course (double c) {
		//just hours course constructor since other params are optional
		this.credit = c;
		this.name = "";
		this.grade = "";
		this.status = "Current";
	}
	
	public Course (String name, double c, String grade) {
		// fully complete constructor without status passed in
		this.credit = c;
		this.name = name;
		this.grade = grade.toUpperCase();
		this.status = "Current";
	}
	
	public Course (String name, double c, String grade, String status) {
		// fully complete constructor w all info passed in
		this.credit = c;
		this.name = name;
		this.grade = grade.toUpperCase();
		this.status = status;
	}

	
	//getters and setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade.toUpperCase();    //uppercase for display purposes
	}
	
	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}
	
	
	
	//to print out course info
	public String toString() {
		return "Credits:  "+this.credit + "   grade: " + this.grade + 
				"   name: " + this.name + "   Status: "+ this.status + "   GPA: " + getGPA();
	}
	
	
	//converts grade string to double gpa
	public double getGPA() {
		double gpa = -1;
		
		switch (grade) 
		{
				case "A+":  gpa = 4.0;
				        break;
				case "A":  gpa = 4.0;
		        		break;
				case "A-":  gpa = 3.7;
						break;
				case "B+":  gpa = 3.3;
						break;
				case "B":  gpa = 3.0;
						break;
				case "B-":  gpa = 2.7;
						break;
				case "C+":  gpa = 2.3;
						break;
				case "C":  gpa = 2.0;
						break;
				case "C-":  gpa = 1.7;
						break;
				case "D+":  gpa = 1.3;
						break;
				case "D":  gpa = 1.0;
						break;
				case "F":  gpa = 0.0;
						break;
				case "":  gpa = 0.0;
						break;
				default:
		                System.out.println("Something wrong!");
		                break;
		}
		return gpa;
	}

}
