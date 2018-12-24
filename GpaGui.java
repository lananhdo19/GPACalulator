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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ConcurrentModificationException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.awt.event.ActionEvent;
public class GpaGui {

	public static void main(String[] args) {
		new GpaGui();

	}

	//fields for the sizes of window, stack length, and statuses
	static int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	static int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static int stackLength = 1;
	static String status = "Current"; //default status of course is current
	static String [] statuses = {"Current","Previous", "Anticipated"};


	//the components for the courses panel
	final static JPanel coPanel = new JPanel(); //shows all courses and course menu options
	static JPanel coOptions = new JPanel(new FlowLayout()); //add and remove menu buttons
	static Stack coStack = new Stack(); //contains courses
	static JList coList = new JList(coStack);//displays the stack of courses vertically
	static JScrollPane scroll = new JScrollPane(coList);  //makes colist scrollable


	static JPanel addPanel = new JPanel(); //panel for adding new courses
	static JPanel removePanel = new JPanel();

	static final JPanel sumPanel = new JPanel(); //summary panel w required and current gpa


	static GPACalculator calc = new GPACalculator(); //to do all gpa calculations



	public GpaGui() {

		//the ultimate jframe for all panels and components
		JFrame guiFrame = new JFrame();

		//make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Calculating GPA");
		guiFrame.setSize(width/2,height-300);

		//This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		refresh();

		//provides sample course to show course display
		//does not affect gpa: no grade and no credits
		Course c1 = new Course("Example Course", 0, ""); 
		coStack.add(c1);




		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//summary panel with req, target, current gpas, and advice
		sumPanel.setLayout(new BorderLayout());


		//all the components for summary panel

		//textbox for entering desired target gpa
		JTextField tarField = new JTextField(10);
		tarField.setPreferredSize(new Dimension(width/4,50));

		//display current gpa w label
		JLabel currGpaLabel = new JLabel("Current GPA:");
		currGpaLabel.setFont(currGpaLabel.getFont().deriveFont(22f));
		currGpaLabel.setHorizontalAlignment(JLabel.CENTER);

		//display target gpa w label
		JLabel tarGpaLabel = new JLabel("Target GPA:");
		tarGpaLabel.setFont(tarGpaLabel.getFont().deriveFont(18f));

		//display required gpa w label
		JLabel reqGpaLabel = new JLabel("Required GPA:");
		reqGpaLabel.setFont(reqGpaLabel.getFont().deriveFont(18f));

		//displays feedback for wrong inputs and advice about required gpa
		JLabel adviceLabel = new JLabel("Advice: Have anticipated credits to calculate required GPA");
		adviceLabel.setFont(adviceLabel.getFont().deriveFont(18f));




		//button calulates ONLY current gpa and displays it on current gpa label
		JButton calcBut = new JButton("Calculate Current GPA");
		calcBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//so that gpa double is displayed with 2 decimal points
				NumberFormat formatter = new DecimalFormat("#0.00"); 

				double currGPA = calc.getCurrentGPA(coStack);

				currGpaLabel.setText("Current GPA: " + formatter.format(currGPA));

				refresh();
			}
		});




		//calculate required gpa using current gpa, anticipated credits, and target gpa
		JButton reqCalcBut = new JButton("Calculate Required GPA to Reach Target GPA");
		reqCalcBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//current gpa calculated
				currGpaLabel.setText("Current GPA: " + calc.getCurrentGPA(coStack));

				try {//will try to convert target textfield text into double
					double targetGPA = Double.parseDouble(tarField.getText());

					NumberFormat formatter = new DecimalFormat("#0.00");     

					adviceLabel.setText("Advice:");
					tarGpaLabel.setText("Target GPA: " + formatter.format(targetGPA));

					//doesnt allow gpa to be negative
					if(targetGPA<0) adviceLabel.setText("Invalid Target GPA!");
					else if (calc.hasNoAnticipatedCredits(coStack))  adviceLabel.setText("No Anticipated Credits!");
					else {

						double req = calc.getRequiredGPA(coStack, targetGPA);

						tarGpaLabel.setText("Target GPA: " + formatter.format(targetGPA));
						tarField.setText("");
						reqGpaLabel.setText("Required GPA: " + formatter.format(req));


						//if required gpa more than 4, advice is less credits
						//if less than  2, advice is more credits
						if (req > 4.0) adviceLabel.setText("Try Adding More Credit Hours and Recalculate!");
						else if (req < 2.0) adviceLabel.setText("You Can Take Fewer Credit Hours If You Want!");
					}
				}

				//if input is not a number, say invalid target gpa
				catch(NumberFormatException exception) {
					adviceLabel.setText("Invalid Target GPA!");
				}

				refresh();
			}
		});




		//clear button sets all the labels and fields to default setting
		JButton clearBut = new JButton("Clear");
		clearBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				tarGpaLabel.setText("Target GPA:");
				currGpaLabel.setText("Current GPA:");
				reqGpaLabel.setText("Required GPA:");
				adviceLabel.setText("Advice:");
				tarField.setText("");
				refresh();
			}
		});



		//panel so that calculate current gpa and clear button are in flowlayout next to each other
		JPanel sumOptions = new JPanel(new FlowLayout());
		sumOptions.add(reqCalcBut);
		sumOptions.add(calcBut);
		sumOptions.add(clearBut); 

		//separate panel for target gpa and its textfield so they are side by side
		JPanel tarPanel = new JPanel(new FlowLayout());
		tarField.setPreferredSize(new Dimension(100, 30));
		tarPanel.add(tarGpaLabel);
		tarPanel.add(tarField);

		//center panel in the middle displays target required and advice
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(width/2, height/4));
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(tarPanel);
		infoPanel.add(reqGpaLabel);
		infoPanel.add(adviceLabel);


		//adds current gpa to summary panel up top,
		//menu options at the bottom
		//then infoPanel in center
		sumPanel.add(currGpaLabel, BorderLayout.NORTH);

		sumPanel.add(sumOptions, BorderLayout.SOUTH);

		sumPanel.add(infoPanel);

		sumPanel.setVisible(false); //starts program on course panel





		///////////////////////////////////////////////////////////////////////////////////////////////////////      


		JLabel coLabel = new JLabel("Courses:"); //label for instruction and feedback
		JButton removeSpecifcBut = new JButton("Remove Specific");

		//make panel to add a custom new course with labels for intruction
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));
		addPanel.add(new JLabel());//for spacing

		addPanel.add(new JLabel("Credits"));
		JTextField credField = new JTextField();
		addPanel.add(credField);

		addPanel.add(new JLabel("Grade (Optional)"));
		JTextField gradeField = new JTextField();
		addPanel.add(gradeField);

		addPanel.add(new JLabel("Name (Optional)"));
		JTextField nameField = new JTextField();
		addPanel.add(nameField);

		//comboBox for statuses, default status is current
		addPanel.add(new JLabel("Status (Defaults to Current)"));

		JComboBox statusCombo = new JComboBox(statuses);
		statusCombo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				status = (String) statusCombo.getSelectedItem();
			}
		});

		addPanel.add(statusCombo);



		//checks if credits and grade fulfill requirements then adds the course to stack
		JButton addBut = new JButton("Add Course");
		addBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//try to make credits into a double, if not report invalid grade
				try {
					double cred = Integer.parseInt(credField.getText());

					//if cred less than 0, invalid grade, course not made
					if (cred>= 0) {

						String grade = gradeField.getText().toUpperCase();

						//only add course if grade and credit is acceptable
						if (calc.isAcceptable(grade)) {
							//checks input against list of acceptable grades in GPACalculator


							//creates the course and gives feedback
							coStack.add(new Course(nameField.getText(), cred, grade, status));
							coLabel.setText("New Course Added!");
							stackLength++;
							refresh();

						}
						else {
							coLabel.setText("Invalid Grade!");
						}
					}
					else {
						coLabel.setText("Invalid Credit!");
					}

				}
				catch(NumberFormatException exception) {
					coLabel.setText("Invalid Credit!");
				}

				//clears inputs after rejecting or creating course
				gradeField.setText("");
				nameField.setText("");
				credField.setText("");

				refresh();
			}
		});
		
		JButton addBut2 = new JButton("Add Past Semester");
		addBut2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//try to make credits into a double, if not report invalid grade
				try {
					double cred = Integer.parseInt(credField.getText());

					//if cred less than 0, invalid grade, course not made
					if (cred>= 0) {

						Double gpa = Double.parseDouble(gradeField.getText());

						//only add course if grade and credit is acceptable
						try {
							//checks input against list of acceptable grades in GPACalculator


							//creates the course and gives feedback
							coStack.add(new Semester(nameField.getText(), cred, gpa));
							coLabel.setText("New Course Added!");
							stackLength++;
							refresh();

						}
						catch(NumberFormatException exception) {
							coLabel.setText("Invalid GPA!");
						}
					}
					else {
						coLabel.setText("Invalid Credit!");
					}

				}
				catch(NumberFormatException exception) {
					coLabel.setText("Invalid Credit!");
				}

				//clears inputs after rejecting or creating course
				gradeField.setText("");
				nameField.setText("");
				credField.setText("");

				refresh();
			}
		});


		//add button for adding new course
		addPanel.add(addBut);
		addPanel.add(addBut2);
		addPanel.setVisible(false); //only course panel should be seen normally
		////////////////////////////////////////////////////////////////////////////////////////////////////
	
		//create components of and remove specific panel
		removePanel.setLayout(new BoxLayout(removePanel, BoxLayout.PAGE_AXIS));

		removePanel.add(new JLabel("Name"));
		JTextField nameRField = new JTextField();
		removePanel.add(nameRField);

		JButton removeByNameButR = new JButton(" Remove by Name ");
		removePanel.add(removeByNameButR);

		removePanel.add(new JLabel("Credit"));
		JTextField credRField = new JTextField();
		removePanel.add(credRField);
		
		JButton removeByCreditButR = new JButton("Remove by Credit");
		removePanel.add(removeByCreditButR);
		
		JButton removeByBothButR = new JButton(" Remove by Both ");
		removePanel.add(removeByBothButR);
		
		removePanel.setVisible(false);

		
		//removes by matching name only
		removeByNameButR.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{	
				try {
					removeCourse(nameRField.getText());
				}
				catch (NumberFormatException exception) { 
					//if there is input in the credits or in case
				}
				catch (ConcurrentModificationException exception) {
					//for editing the coStack
				}
				finally {
					coLabel.setText("Attempted to Remove " + nameRField.getText() + " Course");
					nameRField.setText("");
					credRField.setText("");
					refresh();
				}
			}
		});

		//removing latest course that matches credit
		removeByCreditButR.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{	
				try {
					removeCourse(Double.parseDouble(credRField.getText()));
				}
				catch (NumberFormatException exception) {
				}
				catch (ConcurrentModificationException exception) {
				}
				finally {
					coLabel.setText("Attempted to Remove " + credRField.getText()+ " Credit Course");
					nameRField.setText("");
					credRField.setText("");
					refresh();
				}
			}
		});
		
		//removes course that matches both name and credit
		removeByBothButR.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{	
				try {
					removeCourse(nameRField.getText()
							, Double.parseDouble(credRField.getText()));
				}
				catch (NumberFormatException exception) {
				}
				catch (ConcurrentModificationException exception) {
				}
				finally {
					coLabel.setText("Attempted to Remove " + credRField.getText() + " Credit " +nameRField.getText() +" Course");
					nameRField.setText("");
					credRField.setText("");
					refresh();
				}
			}
		});

		///////////////////////////////////////////////////////////////////////////////////////////////////
		//make course panel with visibility opposite of summary panel
		coPanel.setLayout(new BoxLayout(coPanel, BoxLayout.PAGE_AXIS)); 
		refresh();

		coList.setLayoutOrientation(JList.VERTICAL_WRAP); //components stacked on top of each other
		coList.setFixedCellWidth(width/2);
		coList.setVisibleRowCount(30); //allows for 30 courses displayed default before scrollable

		coList.setValueIsAdjusting(true);



		//button triggers add panel visibility and the functions of the add panel
		JButton newBut = new JButton("New Course");
		newBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//New Course Button also triggers turning off the add panel
				addPanel.setVisible(!addPanel.isVisible());
				if(removePanel.isVisible()) {
					removePanel.setVisible(false);
					removeSpecifcBut.setText("Remove by Name");
				}
				//prepares add panel with instructions and clearing past input
				if (addPanel.isVisible()) {
					coLabel.setText("Enter New Course Information:");
					newBut.setText("Done"); 
				}
				else {
					//if add panel is not visible, clear everything for course panel
					coLabel.setText("Courses:");
					newBut.setText("New Course");
					gradeField.setText("");
					nameField.setText("");
					credField.setText("");
				}

				refresh();
			}
		});



		//assuming player only removes the latest course
		//removes latest added class
		JButton removeBut = new JButton("Remove Latest");
		removeBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try{
					if (coStack.size()>=1) { //checks again if stack empty
						coStack.pop();//remove latest added class
						checkStack(); // for testing
					}

					refresh();
				}
				//handles code if stack is empty
				catch(EmptyStackException exception) {
					stackLength=0;
				}
			}
		});



		//just adds 5 courses 3 credit each with no grade
		JButton add15But = new JButton("Add 15 Credits");
		add15But.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				for (int i = 0; i<5; i++) {
					coStack.add(new Course("", 3,"" ,"Anticipated"));
					stackLength++;
					checkStack();
				}
				refresh();
			}
		});



		//remove every course and refresh
		JButton removeAllBut = new JButton("Remove All");
		removeAllBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try{
					if (coStack.size()>=1) {
						coStack.removeAllElements();
						checkStack(); //testing
						stackLength=0;
						scroll.setMinimumSize(coPanel.getMinimumSize());
						refresh();
					}
				} //handles code if stack empty
				catch(EmptyStackException exception) {
					stackLength=0;
				}
				refresh();
			}
		});

		//remove every course and refresh
		
		removeSpecifcBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				removePanel.setVisible(!removePanel.isVisible());
				if (addPanel.isVisible()) {
					addPanel.setVisible(false);
					newBut.setText("New Course");
				}

				if (removePanel.isVisible()) {
					removeSpecifcBut.setText("Done");
				}
				else {
					coLabel.setText("Courses:");
					removeSpecifcBut.setText("Remove by Name");
				}
				refresh();
			}
		});



		//add all the buttons to the courses menu
		coOptions.add(newBut);
		coOptions.add(removeBut);
		coOptions.add(removeAllBut);
		coOptions.add(removeSpecifcBut);
		coOptions.add(add15But);


		//add the menu, label, and list to courses panel
		coPanel.add(coLabel); 
		coPanel.add(scroll);    //add to coPanel the scrollable list of courses
		coPanel.add(addPanel);
		coPanel.add(removePanel);
		coPanel.add(coOptions);



		//button for switching between summary or courses panels at very bottom of gui
		JButton sumCoBut = new JButton( "Summary or Courses"); 
		sumCoBut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				//opposite visibilty so one is seen at a time
				coPanel.setVisible(!coPanel.isVisible());
				sumPanel.setVisible(!sumPanel.isVisible());

			}
		});


		//The JFrame uses the BorderLayout layout manager.
		//Put the two JPanels and JButton in different areas.
		guiFrame.add(sumPanel, BorderLayout.NORTH); // "top"
		guiFrame.add(coPanel, BorderLayout.CENTER); // "middle"
		guiFrame.add(sumCoBut,BorderLayout.SOUTH); // "bottom"

		//make sure the JFrame is visible
		guiFrame.setVisible(true); //the JFrame is set to visible
	}


	//updates copanel, summary panel, and colist to adjust with new changes
	public static void refresh() {
		coPanel.setMinimumSize(new Dimension(width/3,height/3));
		coPanel.setPreferredSize(new Dimension(width/2,height/2));
		coPanel.revalidate();
		coPanel.repaint();

		//if coList grows to have more than 30 classes, adjust scroll length
		if (stackLength>30) {
			coList.setVisibleRowCount(stackLength);
		}

		scroll.setVisible(coList.isVisible());
		scroll.setPreferredSize(new Dimension(width/3,height/3));

		sumPanel.revalidate();
		sumPanel.repaint();


	}

	//for testing to see what in the stack of courses
	public static void checkStack() {
		System.out.println("I Run!");
		for(Object o : coStack) {

			System.out.println(o);
		}
	}

	//removes course with matching name 
		//returns true if removed : unused feature due to errors
	public static boolean removeCourse(String name) {
		boolean removed = false;

		for (Object o : coStack) {
			if (o instanceof Course ) {
				Course c = (Course)o;
				if (c.getName().equals(name)) {
					coStack.remove(c);
					removed = true;
				}
			}
		}
		return false;
	} 
	
	//removes course with matching credit. 
		//returns true if removed : unused feature due to errors
	public static boolean removeCourse(Double credit) {
		boolean removed = false;

		for (Object o : coStack) {
			if (o instanceof Course ) {
				Course c = (Course)o;
				if(c.getCredit() == credit) {
					coStack.remove(c);
					removed = true;
				}
			}
		}
		return false;
	} 
	
	//removes course with matching name and credit. 
	//returns true if removed : unused feature due to errors
	public static boolean removeCourse(String name, Double credit) {
		boolean removed = false;

		for (Object o : coStack) {
			if (o instanceof Course ) {
				Course c = (Course)o;
				if(c.getCredit() == credit && c.getName().equals(name)) {
					coStack.remove(c);
					removed = true;
				}
			}
		}
		return false;
	} 
}
