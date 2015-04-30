package edu.gmu.teemw;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import edu.gmu.teemw.database.User;
import edu.gmu.teemw.database.quiz.Quiz;
import edu.gmu.teemw.gradeserver.GradeServer;
import edu.gmu.teemw.ui.LoginFrame;
import edu.gmu.teemw.ui.Menu;

public class SystemMain {
	public static User currentUser;
	public static Quiz currentQuiz;
	public static GradeServer gradeServer;
	
	private static Thread gradeServerThread;

	public static void main(String[] args) {
		CONNECT_TO_GRADE_SERVER();
		
		JFrame login = new LoginFrame();
		login.setVisible(true);
		
		login.addWindowListener(new WindowListener() {
			@Override public void windowOpened(WindowEvent e) {}
			@Override public void windowClosing(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {
				if (SystemMain.currentUser != null) {
					JFrame menu = new Menu();
					menu.setLocationRelativeTo(login);
					menu.setVisible(true);
				}
			}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowActivated(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
		});
	}
	
	public static void CONNECT_TO_GRADE_SERVER() {
		gradeServer = new GradeServer();
		gradeServerThread = new Thread(gradeServer);
		gradeServerThread.start();
	}
	
	public static void DISCONNECT_FROM_GRADE_SERVER() {
		gradeServer.halt();
		try {
			gradeServerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gradeServer = null;
	}
	
	
}
