import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * This class make a window which ask the player's name after the game is over.
 * After the game is over, player will be asked to input his name. Then click 'Confirm' or press 'ENTER' will confirm and continue.
 * If the player confirm( in either way ) without inputting anything, he will be named 'naughtykid'
 * Player can also click 'Cancel', in which way, his name won't be stored, but his score will be stored, with a name 'tourist'
 *
 */
public class AskName implements KeyListener{

    private JFrame frameA;
    private JTextField getNameTxt;
    private String name;
    private JTextField txtClickConfirmOr;
    private boolean isClosed = false;



    /**
     * Create the application. If some how it fails, print "FailInIntializing"
     */
    public AskName() 
    {
    	try
        {
    		initialize();
        }
    	catch(Exception e)
    	{
    		System.err.println("FailInIntializing");
    	}
    }

    /**
     * Initialize the contents of the frame.
     * 
     */
    private void initialize() 
    {
        frameA = new JFrame("Clicking the 'X' will exit the game");
        frameA.setBounds(100, 100, 417, 307);
        frameA.getContentPane().setLayout(null);
        frameA.setVisible(true);
        
        frameA.addKeyListener(this);

        frameA.setFocusable(true);
        

        JLabel lblWhatsYourName = new JLabel("What's Your Name?");
        lblWhatsYourName.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblWhatsYourName.setBounds(57, 25, 282, 58);
        frameA.getContentPane().add(lblWhatsYourName);

        getNameTxt = new JTextField();
        getNameTxt.addKeyListener(this);
        getNameTxt.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		int key = e.getKeyCode();
        		if (key == KeyEvent.VK_ENTER)
        		{
        			name=getNameTxt.getText();
                    if(name.equals(null)||name.equals(""))	//if the player didnt put anything in the textfield and he clicked the confirm
                    {name = "NaghtyKid";}
        			//System.out.println(name);
        			frameA.requestFocus();
        			isClosed = true;
        		}
        	}
        });
        getNameTxt.setBounds(70, 112, 214, 52);
        frameA.getContentPane().add(getNameTxt);
        getNameTxt.setColumns(10);

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    name=getNameTxt.getText();
                    if(name.equals(null)||name.equals(""))	//if the player didnt put anything in the textfield and he clicked the confirm
                    {name = "NaghtyKid";}
                    isClosed = true;
                    frameA.dispose();
    				System.out.println(name);
    				try {
    					ScoreStorage storeThis=new ScoreStorage(name, BlockBreakerMain.getPoints());  // BlockBreakerMain.getPoints()
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
                }
                
            });
        
        btnConfirm.setBounds(170, 199, 89, 23);
        frameA.getContentPane().add(btnConfirm);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    name = "Tourist";
                    frameA.dispose();
                    isClosed = true;
                }
            });
        btnCancel.setBounds(273, 199, 89, 23);
        frameA.getContentPane().add(btnCancel);
        
        txtClickConfirmOr = new JTextField();
        txtClickConfirmOr.setEditable(false);
        txtClickConfirmOr.setEnabled(false);
        txtClickConfirmOr.setFont(new Font("Arial", Font.ITALIC, 10));
        txtClickConfirmOr.setText("Click Confirm(or press 'Enter') or Cancel to proceed.");
        txtClickConfirmOr.setBounds(10, 237, 249, 21);
        frameA.getContentPane().add(txtClickConfirmOr);
        txtClickConfirmOr.setColumns(10);
        // put code here :  add name to the file
    }

    /**
     * This getter return the name to whoever want it
     * 
     * @return name Name of the player
     */
    public String getName()
    {
        return name;
    }

    /**
     * implements keylistener
     */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * implements keylistener
	 */
	@Override
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * implements keylistener
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ENTER)
		{
			name=getNameTxt.getText();
            if(name.equals(null)||name.equals(""))	//if the player didnt put anything in the textfield and he clicked the confirm
            {name = "NaghtyKid";}
            frameA.dispose();
			System.out.println(name);
			try {
				ScoreStorage storeThis=new ScoreStorage(name, BlockBreakerMain.getPoints());  // BlockBreakerMain.getPoints()
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	public boolean getIsClosed(){
		return isClosed;
	}
}