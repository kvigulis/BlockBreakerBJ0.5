/*
 * Block Breaker Game
 * @author ZJi
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class calls the game menu
 * Click 'Start' to start the game
 * Click 'Records' to see the records
 * Click 'Settings' to set the difficulty
 */
public class BlockBreakerApp {

	private JFrame frameApp;
	private JTextField txtG;

	/**
	 * Launch the application.
	 * @param args unused
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BlockBreakerApp window = new BlockBreakerApp();
					window.frameApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BlockBreakerApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * run recordGUI when the 'recordGUI' button is clicked
	 * run setting when the 'setting' button is clicked
	 */
	private void initialize() {
		frameApp = new JFrame();
		frameApp.setBounds(100, 100, 568, 396);
		frameApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameApp.getContentPane().setLayout(null);
		frameApp.setResizable(false);
		
		SettingGUI settingGUI = new SettingGUI();
		
		txtG = new JTextField();
		txtG.setEditable(false);
		txtG.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtG.setText("Block Breaker");
		txtG.setBounds(148, 11, 220, 52);
		frameApp.getContentPane().add(txtG);
		txtG.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(212, 118, 89, 23);
		frameApp.getContentPane().add(btnStart);
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				BlockBreakerMain.setLives(3);
				new BlockBreakerMain(settingGUI);//.run();
				
		        
			}
		});
		
		
		
		JButton btnRecords = new JButton("Records");
		btnRecords.setBounds(212, 182, 89, 23);
		frameApp.getContentPane().add(btnRecords);
		
		btnRecords.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {								
				//RecordGUI recordGUI = new RecordGUI();
				//recordGUI.run();
				new RecordGUI();
			}
		});
		
		JButton btnSettings = new JButton("Settings");
		btnSettings.setBounds(212, 256, 89, 23);
		frameApp.getContentPane().add(btnSettings);
		
		btnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {				
				
				//SettingGUI settingGUI = new SettingGUI();
				//settingGUI.run();
				settingGUI.getFrame().setVisible(true);
			}
		});
		
	}
}