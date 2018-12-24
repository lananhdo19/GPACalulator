import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class test {
  public static void main(String[] argv) throws Exception {
	  JFrame guiFrame = new JFrame();

		//make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Calculating GPA");
		guiFrame.setSize(700,700);

		//This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);
		
    String[] items = { "A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" ,"A", "B", "C", "D" };
    JList list = new JList(items);
    JScrollPane scrollingList = new JScrollPane(list);

    // Change orientation to top-to-bottom, left-to-right layout
    list.setLayoutOrientation(JList.VERTICAL);
    
    guiFrame.add(scrollingList);
    guiFrame.setVisible(true);
  }
}