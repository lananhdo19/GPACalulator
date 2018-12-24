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
assuming courses without status defaults to current
previous classes are considered when calc current gpa > prev and current classes accounted for in current gpa
assuming 15 credits will be added as 3 anticipated courses with no grade
assuming user will input letter grades: a- or b+

the user needs to press "Done" after entering course to see course if course list is empty 
 **/

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Stack;

public class GPACalculator {

	//keeps track of all grade inputs allowed
	final private String [] acceptableGrades = {"A+","A", "A-", 
			"B+", "B", "B-",
			"C+", "C", "C-",
			"D+", "D", "F", ""};

	private double semGPA = 0;
	//constructor empty because class only used for methods. no setting fields
	public GPACalculator() {	}



	//returns acceptable grades
	public String[] getAcceptableGrades() {
		return acceptableGrades;
	}


	//if input of grade is an acceptable grade return true
	public boolean isAcceptable(String grade) {

		boolean ans = false;

		for (int i= 0; i<acceptableGrades.length; i++) {
			if (acceptableGrades[i].equals(grade)) return true;
		}

		return ans;
	}




	//calculates currentGPA from prev and current courses
	public double getCurrentGPA(Stack s) {
		double gpa = -99;
		double sumCred = 0;
		double sumGPA = 0;

		for(Object o : s) {		
			//if its a course
			if (o instanceof Course) {
				Course c = (Course)o;

				//only previous current courses with grades will be in currentGPA
				if (c.getStatus().equals("Current") || c.getStatus().equals("Previous")) {


					if(!c.getGrade().equals("")) {
						double currentCourse = c.getGPA() * c.getCredit();
						sumGPA+=currentCourse; //collects all the gpa points per course
						sumCred+=c.getCredit(); //collects all the credits per course
					}

				}
			}
			else if (o instanceof Semester) {
				Semester ss = (Semester)o;
				double currentCourse = ss.getGpa() * ss.getCredit();
				sumGPA+=currentCourse; 
				sumCred+=ss.getCredit();
			}
		}

		//to prevent dividing by 0
		if (sumCred<=0) return 0;


		//for testing and checking total score and total gpa
		//System.out.println("sumGPA"+sumGPA);
		//System.out.println("sumCred"+sumCred);


		gpa = sumGPA/sumCred;
		semGPA = gpa;
		return gpa;
	}





	//calculates required gpa with current gpa and target gpa
	public double getRequiredGPA(Stack s, double targetGPA) {

		//local variables to calculate required gpa from target gpa formula
		double reqGPA = -99;
		double currentGPA = getCurrentGPA(s); //System.out.println("currentgpa"+currentGPA);
		double currentCred = 0;  
		double untakenCred =0;


		for(Object o : s) {   //only courses marked as "Anticipated" will be calculated in untaken credit
			if (o instanceof Course)
			{
				Course c = (Course)o;

				if (c.getStatus().equals("Anticipated")) untakenCred+=c.getCredit(); //sums all the untaken credits
				else currentCred+=c.getCredit(); //sums all the taken credits
			}
			
			//for testing 
			//System.out.println("currcred"+currentCred);
			//System.out.println("untakenCred"+untakenCred);

			double totalCred = currentCred + untakenCred; //System.out.println("totalCred"+totalCred);

			reqGPA = ( (targetGPA * totalCred) - (currentGPA*currentCred) ) / untakenCred;
			//formula for required gpa

		}
		return reqGPA;
	}




	//checks if the user has inputed anticipated credits before calc required gpa
	public boolean hasNoAnticipatedCredits(Stack s) {
		boolean noAnti = true;

		for(Object o : s) {
			if (o instanceof Course) {
				Course c = (Course)o;

				if (c.getStatus().equals("Anticipated")) noAnti = false;
			}
		}
		return noAnti;

	}
}
