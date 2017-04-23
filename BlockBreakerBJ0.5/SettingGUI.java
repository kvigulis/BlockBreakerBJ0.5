import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Display the settings GUI
 * This window allows you to choose the difficulty( the speed of the ball)
 * Click 'confirm' to confirm and continue
 * Click 'Reset game progress' will clear all the records.
 */
public class SettingGUI {
    private JFrame frameS;
    private float ballSpeedX=0.7f;
    private float ballSpeedY=0.7f;
    JRadioButton rdbtnAidan,rdbtnEasy,rdbtnMedium,rdbtnHard;
    ButtonGroup radioBG;

    /**
     * Create the application.
     */
    public SettingGUI(    ) {
        initialize();
        groupButton();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frameS = new JFrame("Click 'Back' to return to the main menu.");
        frameS.setBounds(100, 100, 450, 300);
        frameS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameS.getContentPane().setLayout(null);
        //frameS.setVisible(true);

        JLabel lblDifficulty = new JLabel("Difficulty");
        lblDifficulty.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblDifficulty.setBounds(22, 11, 82, 19);
        frameS.getContentPane().add(lblDifficulty);

        rdbtnAidan = new JRadioButton("Aidan");
        rdbtnAidan.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) 
                {
                    radioBG.clearSelection();
                    rdbtnAidan.setSelected(true);
                    applySettings();
                }
            });
        rdbtnAidan.setBounds(6, 45, 95, 23);
        frameS.getContentPane().add(rdbtnAidan);

        rdbtnEasy = new JRadioButton("Easy");
        rdbtnEasy.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    radioBG.clearSelection();
                    rdbtnEasy.setSelected(true);
                    applySettings();
                }
            });
        rdbtnEasy.setBounds(103, 45, 109, 23);
        frameS.getContentPane().add(rdbtnEasy);

        rdbtnMedium = new JRadioButton("Medium");
        rdbtnMedium.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    radioBG.clearSelection();
                    rdbtnMedium.setSelected(true);
                    applySettings();
                }
            });
        rdbtnMedium.setBounds(213, 45, 95, 23);
        frameS.getContentPane().add(rdbtnMedium);

        rdbtnHard = new JRadioButton("Hard");
        rdbtnHard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    radioBG.clearSelection();
                    rdbtnHard.setSelected(true);
                    applySettings();
                }
            });
        rdbtnHard.setBounds(324, 45, 109, 23);
        frameS.getContentPane().add(rdbtnHard);

        JButton resetButton = new JButton("Reset game progress");
        resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
                {
                    try
                    {                        
                        ScoreStorage.clearFile("score.txt");
                    }
                    catch (IOException e1) 
                    {
                        e1.printStackTrace();
                    }    
                }
            });
        resetButton.setBounds(100, 206, 180, 23);
        frameS.getContentPane().add(resetButton);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frameS.setVisible(false);
                }
            });
        btnBack.setBounds(300, 206, 93, 23);
        frameS.getContentPane().add(btnBack);
    }

    /**
     * This is a setter which can set the X speed of the ball by difficulty
     * @param newSpeedX the new X speed
     */
    public void setBallSpeedX(int newSpeedX)
    {
        ballSpeedX=newSpeedX;
    }

    /**
     * This is a setter which can set the Y speed of the ball by difficulty
     * @param newSpeedY the new Y speed 
     */
    public void setBallSpeedY(int newSpeedY)
    {
        ballSpeedX=newSpeedY;
    }

    /**
     * To group the button, so that you can only choose one button at a time.
     */
    private void groupButton( ) {

        radioBG = new ButtonGroup( );

        radioBG.add(rdbtnAidan);
        radioBG.add(rdbtnEasy);
        radioBG.add(rdbtnMedium);
        radioBG.add(rdbtnHard);
    }

    /**
     * Getter, this will pass this frame to whoever needs this
     * @return frameS
     */
    public JFrame getFrame()
    {
        return this.frameS;
    }
    /**
 	*This method is used to apply all the changes made to the game 
 	*/
    public void applySettings(){
        if(rdbtnAidan.isSelected())
        {
            ballSpeedX = 1.0f;
            ballSpeedY = 1.0f;
            BlockBreakerMain.setNumberOfBalls(20);
            //System.out.println("Aidan is selected");
        }
        else if(rdbtnEasy.isSelected())
        {
            ballSpeedX = 0.45f;
            ballSpeedY = 0.45f;//setSpeed
            //System.out.println("Easy is selected");
        }
        else if(rdbtnMedium.isSelected())
        {
            ballSpeedX = 0.7f;
            ballSpeedY = 0.7f;//setSpeed
            //System.out.println("Medium is selected");
        }
        else if(rdbtnHard.isSelected())
        {
            ballSpeedX = 1.2f;
            ballSpeedY = 1.2f;//setSpeed
            //System.out.println("Hard is selected");
        }
        else 
        {
            ballSpeedX = 0.5f;
            ballSpeedY = 0.5f;//setSpeed
            //System.out.println("Nothing is selected");
        }
    }

    /**
     * Getter, return ballSpeedX to whoever needs it, in which way, we can pass the speed we want to set.
     * @return ballSpeedX
     */
    public float getXSpeed()
    {
        return this.ballSpeedX;
    }

    /**
     * Getter, return ballSpeedX to whoever needs it, in which way, we can pass the speed we want to set.
     * @return ballSpeedY
     */
    public float getYSpeed()
    {
        return this.ballSpeedY;
    }
}