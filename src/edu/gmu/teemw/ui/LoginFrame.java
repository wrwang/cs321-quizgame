package edu.gmu.teemw.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.*;

import edu.gmu.teemw.SystemMain;
import edu.gmu.teemw.database.Database;
import edu.gmu.teemw.database.User;
import edu.gmu.teemw.gradeserver.GradeServer;

public class LoginFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -628060727800800897L;
	
	private JPanel contentPane;
	JLabel unametitle = new JLabel("Username: ");
	JTextField uname = new JTextField(15);
	JLabel pwtitle = new JLabel("Password: ");
	JPasswordField pw = new JPasswordField(15);		//may change to JTextField
	JButton loginBut = new JButton("LOGIN");
	JLabel wrnings = new JLabel();
	JButton resetPW = new JButton("Forgot Password");
	
	JLabel unameF = new JLabel("Enter Username: ");
	JTextField unF = new JTextField(15);
	JButton submit = new JButton("Submit");
	
	private String username="";
	private String password="";
	
	public LoginFrame() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(0, 0, 700, 150);  //(x-axis on screen,y-axis on screen,width, height)
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		
		contentPane.setSize(500, 500);;
		setContentPane(contentPane);
		
		contentPane.add(unametitle);
		contentPane.add(uname);
		contentPane.add(pwtitle);
		contentPane.add(pw);
		contentPane.add(loginBut);
		
		contentPane.add(resetPW);
		
		contentPane.add(unameF);
		contentPane.add(unF);
		contentPane.add(submit);
		unameF.setVisible(false);
		unF.setVisible(false);
		submit.setVisible(false);
		contentPane.add(wrnings);
		
		loginBut.addActionListener(this);
		resetPW.addActionListener(this);
		submit.addActionListener(this);
		
		contentPane.getRootPane().setDefaultButton(loginBut);
/*				TEST
		 		USERS
				U1,John Smith,Student,jsmith,passy
				U2,Jane Doe,Student,jdoe,passwerd
				U3,Dr Kinga,Teacher,drk,pass
*/
	}

	private void login(){
		unameF.setVisible(false);
		unF.setVisible(false);
		submit.setVisible(false);
		username=uname.getText();
		password=new String(pw.getPassword());
//		if (username.equals("") && password.equals(""))
//			wrnings.setText("Error: Username and Password empty");
//		else if (username.equals(""))
//			wrnings.setText("Error: Username empty");
//		else if (password.equals(""))
//			wrnings.setText("Error: Password empty");
		
		Database database = Database.getDatabaseInstance();
		User currentUser = database.getUser(username, password);
		if (currentUser != null) {
			wrnings.setText("Login confirmed. Welcome back " + currentUser.getName() + "!");
			SystemMain.gradeServer.record(currentUser.getName() + " (" + currentUser.getUsername() + ") has logged in");
			SystemMain.currentUser=currentUser;
			this.dispose();
		} else {
			wrnings.setForeground(new Color(255, 0, 0));
			wrnings.setText("Wrong username/password!");
			SystemMain.currentUser=null;
		}
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if (source==loginBut){
			login();
		}

		if (source==resetPW){
			uname.setText("");
			pw.setText("");
			wrnings.setText("");
			unF.setText("");
			unameF.setVisible(true);
			unF.setVisible(true);
			submit.setVisible(true);
			contentPane.getRootPane().setDefaultButton(submit);
		}
		if (source==submit){
			if (unF.getText().equals(""))
				wrnings.setText("ERROR: Enter a username");
			else
				wrnings.setText("A message was sent to your e-mail if the username \"" + unF.getText() + "\" is a registered user.");
			unF.setText("");
		}
	}
}
