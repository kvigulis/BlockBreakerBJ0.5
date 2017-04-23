import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * This class is used to display a message when some error happens.
 * You can either specify the error or use the defaylt one.
 * If the object of this type takes no parameter, then display 'An unknown error appeared.
 * Or it takes a String when it is called, display the String
 * Click Back to return to the game.'
 * @author user
 *
 */
public class ErrorDisplay{
	
	private String errorT="An unknown error appeared. Click Back to return to the game.";

	private JFrame frameE = new JFrame();


/**
 * A constructor of Type 'ErrorDisplay' which takes no parameters
 */
	public ErrorDisplay() {
		try {
			initialize();
			} 
		catch (Exception e) {
				e.printStackTrace();
			}
	}
/**
 * A constructor taking 'errorType' when a new object of ErrorDispaly is created, which makes it able to specify what kind of error happens
 * @param errorType
 */
	public ErrorDisplay(String errorType) {
		try {
			this.errorT=errorType;
			initialize();
			} 
		catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 */
	private void initialize() {
		//frameE = new JFrame();
		frameE.setBounds(100, 100, 450, 300);
		frameE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameE.getContentPane().setLayout(null);
		frameE.setVisible(true);
		
		
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frameE.dispose();
			}
		});
		btnBack.setBounds(299, 216, 93, 23);
		frameE.getContentPane().add(btnBack);
		
		JTextPane errorDisplay = new JTextPane();
		errorDisplay.setFont(new Font("Arial", Font.ITALIC, 25));
		errorDisplay.setEnabled(false);
		errorDisplay.setEditable(false);
		errorDisplay.setText(errorT);
		errorDisplay.setBounds(68, 47, 324, 94);
		frameE.getContentPane().add(errorDisplay);
	}
	
}
