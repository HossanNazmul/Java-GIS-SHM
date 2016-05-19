package mainGUIPackage;

import importDAT.SelectNode2DAT;
import importJDBC.SelectNode2JDBC;
import importXML.SelectNode2XML;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

import java.awt.Font;
import javax.swing.SwingConstants;

public class MainGUI {

	private JFrame frame;
	private JTextField nameOfService;
	private JTextField SosServerUrl;
	static String filePath;
	static JTextArea textArea;
	private PrintStream printStream;
	JPanel panel = new JPanel();
	String userName;
	String userPassword;
	String mode = "0";
	static MainGUI window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
			    	System.err.println(e.getClass().getName()+":"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}	

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("istSOS Data Insertion Manager");
		frame.getContentPane().setLayout(null);
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					filePath="";
					filePath = FileLocator.jfilechooser();
					
					if (filePath.equals(""))
					{
				    	JOptionPane.showMessageDialog(frame, "You must select a sensor data file (.CSV)");
					}
					else 
					{
					System.out.println("File loaded from : " + filePath);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
			    	System.err.println(e.getClass().getName()+":"+e.getMessage());
			    	JOptionPane.showMessageDialog(frame, "Something is wrong to get the data file");
				}
			}
		});
		btnOpenFile.setBounds(353, 122, 162, 43);
		frame.getContentPane().add(btnOpenFile);

		JLabel lblNameOfService = new JLabel("Name of Service");
		lblNameOfService.setBounds(45, 124, 128, 38);
		frame.getContentPane().add(lblNameOfService);
		
		nameOfService = new JTextField();
		nameOfService.setBounds(160, 124, 162, 38);
		frame.getContentPane().add(nameOfService);
		nameOfService.setColumns(10);
		nameOfService.setText("bridgedemoservice");
		
		JButton btnInsertObservation = new JButton("Execute");
		btnInsertObservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String nameOfService; // sosServerURL;
				try 
				{
//					sosServerURL = MainGUI.this.SosServerUrl.getText();
//					DBConnection.DBConnect(sosServerURL, userName, userPassword);
//					System.out.println(sosServerURL);
					nameOfService = MainGUI.this.nameOfService.getText();
					
					if (mode.equals("1"))
					{
						SelectNode2DAT.csv2DAT(filePath);

					}
					else if (mode.equals("2"))
					{
						SelectNode2XML.importXML(nameOfService, filePath );
					}
					else if (mode.equals("3"))
					{
//						userName = JOptionPane.showInputDialog("Type your Database User Name");
////						System.out.println(userName);
//						
//						userPassword = JOptionPane.showInputDialog("Type your Database password");
////						System.out.println(userPassword);
						
						SelectNode2JDBC.DBInsertion(filePath, nameOfService);
					}
					else if (mode.equals("0"))
					{
				    	JOptionPane.showMessageDialog(frame, "Please select one of the methods listed");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
			    	System.err.println(e.getClass().getName()+":"+e.getMessage());
			    	JOptionPane.showMessageDialog(frame, "Unable to Insert Observation. See the status for details about error");
				}
			}
		});
		btnInsertObservation.setBounds(554, 122, 162, 43);
		frame.getContentPane().add(btnInsertObservation);
		
		JButton btnCloseWindow = new JButton("Quit");
		btnCloseWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
//				window.frame.dispose();
			}
		});
		btnCloseWindow.setBounds(634, 11, 82, 23);
		frame.getContentPane().add(btnCloseWindow);
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				window.frame.setVisible(false);
				MainGUI.main(null);
				}
		});
		btnReload.setBounds(634, 45, 82, 23);
		frame.getContentPane().add(btnReload);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(1, 194, 713, 127);
		frame.getContentPane().add(textArea);
		
		//redirect and show all console output to the GUI output inside textArea
		printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		
		//Jpanel is used to create auto scroll bar in the textArea 
		JPanel centralPanel = new JPanel();
		centralPanel.setBounds(30, 198, 715, 322);
		frame.getContentPane().add(centralPanel);
		centralPanel.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(textArea);
		//what comes here to auto scroll? so the bottom of the textarea will be seen? (currently I have to manually scrolldown to see new data)
		centralPanel.add(scroll, BorderLayout.CENTER); 
		
		JLabel lblCopyright = new JLabel("Developed by the Department of Geodesy & Geoinformation Science, TU Berlin. Email:{hossan,thomas.kouseras}@campus.tu-berlin.de");
		lblCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopyright.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblCopyright.setBounds(30, 531, 713, 14);
		frame.getContentPane().add(lblCopyright);
		
		JLabel lblSosServerUrl = new JLabel("SOS server URL");
		lblSosServerUrl.setBounds(46, 87, 100, 20);
		frame.getContentPane().add(lblSosServerUrl);
		
		SosServerUrl = new JTextField();
		SosServerUrl.setBounds(160, 87, 354, 23);
		frame.getContentPane().add(SosServerUrl);
		SosServerUrl.setColumns(10);
		SosServerUrl.setText("//quader.igg.tu-berlin.de:5432/istsos");
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(46, 173, 46, 14);
		frame.getContentPane().add(lblStatus);
		
		final JRadioButton rdbtnCreatedatFiles = new JRadioButton("Create .DAT");
//		rdbtnCreatedatFiles.setText("DAT");
		rdbtnCreatedatFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				mode = "1";
//				System.out.println(mode);
				}
		});
		rdbtnCreatedatFiles.setBounds(45, 21, 110, 23);
		frame.getContentPane().add(rdbtnCreatedatFiles);
		
		final JRadioButton rdbtnHttpPost = new JRadioButton("HTTP POST");
		rdbtnHttpPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				mode = "2";
//				System.out.println(mode);
				}
		});
		rdbtnHttpPost.setBounds(177, 21, 110, 23);
		frame.getContentPane().add(rdbtnHttpPost);
		
		final JRadioButton rdbtnJdbcConnection = new JRadioButton("JDBC Connection");
		rdbtnJdbcConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				mode = "3";
//				System.out.println(mode);
				}
		});
		rdbtnJdbcConnection.setBounds(285, 21, 135, 23);
		frame.getContentPane().add(rdbtnJdbcConnection);
		
		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnCreatedatFiles);
	    group.add(rdbtnHttpPost);
	    group.add(rdbtnJdbcConnection);

	}
}
