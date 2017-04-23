
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * This class call a GUI to display player scores
 * Create a window which will display the highest 5 scores players ever have along with the name of the player
 * If no record exists, display '--'
 * Click 'back' to go back to the game
 */
public class RecordGUI {

	private JFrame frame1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField name1;
	private JTextField name2;
	private JTextField name3;
	private JTextField name4;
	private JTextField name5;
	private JTextField score1;
	private JTextField score2;
	private JTextField score3;
	private JTextField score4;
	private JTextField score5;
	
	private BufferedReader br;
	private ArrayList<Integer> score = new ArrayList<>();
	private ArrayList<String> name= new ArrayList<>();
	


	/**
	 * Create the application. It it fails in initializing, print an error
	 *  
	 */
	public RecordGUI(){
		
		try
		{
			initialize();
		}
		catch(IOException e)
		{
			new ErrorDisplay("FailureInInitializing");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * All the records will be read from the file called 'score.txt'
	 * 
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
				
		frame1 = new JFrame("Click 'Back' to return to the game.");
		frame1.setResizable(false);
		frame1.setBounds(100, 100, 440, 303);
		frame1.getContentPane().setLayout(null);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(true);
		
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblName.setEnabled(false);
		lblName.setBounds(196, 42, 43, 23);
		frame1.getContentPane().add(lblName);
		
		JLabel lblScore = new JLabel("Score");
		lblScore.setEnabled(false);
		lblScore.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblScore.setBounds(324, 46, 46, 14);
		frame1.getContentPane().add(lblScore);
		
		JLabel lblRank = new JLabel("Rank");
		lblRank.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblRank.setEnabled(false);
		lblRank.setBounds(37, 48, 46, 14);
		frame1.getContentPane().add(lblRank);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setText("1");
		textField.setBounds(20, 77, 86, 20);
		frame1.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setText("2");
		textField_1.setBounds(20, 108, 86, 20);
		frame1.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setText("3");
		textField_2.setBounds(20, 139, 86, 20);
		frame1.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setText("4");
		textField_3.setBounds(20, 170, 86, 20);
		frame1.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setText("5");
		textField_4.setBounds(20, 201, 86, 20);
		frame1.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblRecords = new JLabel("Records");
		lblRecords.setFont(new Font("Thames", Font.BOLD | Font.ITALIC, 16));
		lblRecords.setBounds(191, 11, 115, 30);
		frame1.getContentPane().add(lblRecords);
		
		name1 = new JTextField();
		name1.setEditable(false);
		name1.setBounds(176, 76, 86, 20);
		frame1.getContentPane().add(name1);
		name1.setColumns(10);

		
		name2 = new JTextField();
		name2.setEditable(false);
		name2.setBounds(176, 108, 86, 20);
		frame1.getContentPane().add(name2);
		name2.setColumns(10);
		
		
		name3 = new JTextField();
		name3.setEditable(false);
		name3.setBounds(176, 139, 86, 20);
		frame1.getContentPane().add(name3);
		name3.setColumns(10);
		
		
		name4 = new JTextField();
		name4.setEditable(false);
		name4.setBounds(176, 170, 86, 20);
		frame1.getContentPane().add(name4);
		name4.setColumns(10);
		
		
		name5 = new JTextField();
		name5.setEditable(false);
		name5.setBounds(176, 201, 86, 20);
		frame1.getContentPane().add(name5);
		name5.setColumns(10);
		
		
		score1 = new JTextField();
		score1.setEditable(false);
		score1.setBounds(308, 71, 86, 20);
		frame1.getContentPane().add(score1);
		score1.setColumns(10);
		
		score2 = new JTextField();
		score2.setEditable(false);
		score2.setBounds(308, 108, 86, 20);
		frame1.getContentPane().add(score2);
		score2.setColumns(10);
		
		score3 = new JTextField();
		score3.setEditable(false);
		score3.setBounds(308, 139, 86, 20);
		frame1.getContentPane().add(score3);
		score3.setColumns(10);
		
		score4 = new JTextField();
		score4.setEditable(false);
		score4.setBounds(308, 170, 86, 20);
		frame1.getContentPane().add(score4);
		score4.setColumns(10);
		
		score5 = new JTextField();
		score5.setEditable(false);
		score5.setBounds(308, 201, 86, 20);
		frame1.getContentPane().add(score5);
		score5.setColumns(10);
		
		JButton btnBack = new JButton("Back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame1.dispose();
			}
		});
		btnBack.setBounds(277, 241, 93, 23);
		frame1.getContentPane().add(btnBack);
		

		try
		{
			br = new BufferedReader(new FileReader("score.txt"));
			System.out.println("Success in opening the file");
			
		}
		catch(IOException e)
		{
			new ErrorDisplay("NoSuchFile");
		}
		
		String line="nothing3.1415926535897";
		ArrayList<String> tok =new ArrayList<>();
		while(br.ready())
		{
			try
			{
				line = br.readLine();
				String[] temp=line.split(" ");
				tok.add(temp[0]);
				tok.add(temp[1]);
			}
			catch (IOException e)
			{
				new ErrorDisplay("NoSuchFile");;
			}
			
			if(line.equalsIgnoreCase("nothing3.1415926535897"))
			{
				new ErrorDisplay("NoRecordsYet!");
				//System.out.println("Line 238");
			}
		}

		for(int i=0;i<tok.size();i+=2)
		{
				try
				{		
					score.add(Integer.parseInt(tok.get(i+1)));
					name.add(tok.get(i));				
				}
				catch(NumberFormatException e)
				{	
					new ErrorDisplay("FailureInParsing");
				}
		}
		
		try
		{
			br.close();
		}
		catch(IOException e)
		{
			new ErrorDisplay("FailureInClosingTheFile");
		}

		for(int i = 0; i<score.size()-1;i++)
		{
			for(int k=i+1;k<score.size();k++)
			{
				if(score.get(i)<score.get(k))
				{
					int tempInt;
					tempInt=Integer.valueOf(score.get(i));
					score.set(i,Integer.valueOf(score.get(k)));
					score.set(k,Integer.valueOf(tempInt));
					String tempName;
					tempName=String.valueOf(name.get(i));
					name.set(i,String.valueOf(name.get(k)));
					name.set(k,tempName);
				}
			}
		}//selection sorting,after this, score[] is sorted.
		//name is also changed
		
		
		System.out.println("the size is "+score.size());
		if(score.size()>=1)
			{
			name1.setText(name.get(0));
			score1.setText(Integer.toString(score.get(0)));
			}
		else
		{
			name1.setText("--");
			score1.setText("--");
		}

		
		if(score.size()>=2)
		{
			name2.setText(name.get(1));
			score2.setText(Integer.toString(score.get(1)));
		}
		else
		{
			name2.setText("--");
			score2.setText("--");
		}
			
		if(score.size()>=3)
		{
			name3.setText(name.get(2));
			score3.setText(Integer.toString(score.get(2)));
		}
		else
		{
			name3.setText("--");
			score3.setText("--");
		}
		
		if(score.size()>=4)
		{
			name4.setText(name.get(3));
			score4.setText(Integer.toString(score.get(3)));
		}
		else
		{
			name4.setText("--");
			score4.setText("--");
		}
		
		if(score.size()>=5)
		{
			name5.setText(name.get(4));
			score5.setText(Integer.toString(score.get(4)));
		}
		else
		{
			name5.setText("--");
			score5.setText("--");
		}
	}

}