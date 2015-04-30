package edu.gmu.teemw.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.gmu.teemw.SystemMain;
import edu.gmu.teemw.ui.GradeDialog;

import java.awt.FlowLayout;
import java.awt.Component;

public class Menu extends JFrame {
	private static final long serialVersionUID = 1714910446803090985L;
	private JPanel contentPane;
	private JButton takeQuiz, viewQuiz, createQuestion;
	private JDialog take, view, create;

	public Menu() {
		setTitle("Quiz Game - Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 482, 136);
		//setLocationRelativeTo(null);
		
		JPanel tbPanel = new JPanel();
		tbPanel.setLayout(new BoxLayout(tbPanel, BoxLayout.Y_AXIS));
		
		JLabel image = new JLabel();
		image.setAlignmentX(Component.CENTER_ALIGNMENT);
		tbPanel.add(image);
		ImageIcon ic = new ImageIcon(Menu.class.getResource("/edu/gmu/teemw/ui/main.png"));
		image.setIcon(ic);
		
		contentPane = new JPanel();
		tbPanel.add(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(tbPanel);
		takeQuiz = new JButton("Take Quiz");
		viewQuiz = new JButton("View Grades");
		createQuestion = new JButton("Create a Question");
		takeQuiz.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				take = new QuizDialog();
				take.setModal(true);
				take.setVisible(true);
			}

		});
		viewQuiz.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				view = new GradeDialog();
				view.setModal(true);
				view.setVisible(true);
			}

		});
		createQuestion.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				create = new CreateQuestion();
				create.setModal(true);
				create.setVisible(true);
			}

		});
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(takeQuiz);
		contentPane.add(viewQuiz);
		if (SystemMain.currentUser.isTeacher())
			contentPane.add(createQuestion);
		else
			createQuestion = null;
		pack();
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			SystemMain.gradeServer.record(SystemMain.currentUser.getName() + " (" + SystemMain.currentUser.getUsername() + ") is logging out.");
			SystemMain.DISCONNECT_FROM_GRADE_SERVER();
			System.exit(0);
		}
	}
}
