package edu.gmu.teemw.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import edu.gmu.teemw.SystemMain;
import edu.gmu.teemw.database.Database;
import edu.gmu.teemw.database.Grade;
import edu.gmu.teemw.database.User;
import edu.gmu.teemw.database.quiz.Quiz;
import edu.gmu.teemw.gradeserver.GradeServer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.Component;

import javax.swing.Box;

public class GradeDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JButton btnRight;
	private JButton btnLeft;
	private Panel menuPane;

	private Set<Quiz> quizzes;
	private Set<Grade> grades;
	private Set<User> users;

	private enum TeacherState {QUIZ, GRADEQUIZ, USER, GRADEUSER};
	private TeacherState teacherState = TeacherState.QUIZ;

	GradeModel tableModel;
	private int selectedUserQuiz = 0;
	private JLabel lblTitle;
	private Component horizontalGlue;
	private Component horizontalGlue_1;

	/**
	 * Create the frame.
	 */
	public GradeDialog() {
		setTitle("Grades");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 400, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		menuPane = new Panel();
		menuPane.setVisible(false);
		contentPane.add(menuPane, BorderLayout.NORTH);
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.X_AXIS));

		btnLeft = new JButton("New button");
		btnLeft.setHorizontalAlignment(SwingConstants.LEFT);
		btnLeft.setVisible(false);
		btnLeft.addActionListener(this);
		menuPane.add(btnLeft);
		
		horizontalGlue = Box.createHorizontalGlue();
		menuPane.add(horizontalGlue);
		
		lblTitle = new JLabel("Quizzes");
		menuPane.add(lblTitle);

		btnRight = new JButton("New button");
		btnRight.setHorizontalAlignment(SwingConstants.RIGHT);
		btnRight.setVisible(false);
		btnRight.addActionListener(this);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		menuPane.add(horizontalGlue_1);
		menuPane.add(btnRight);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		quizzes = Database.getDatabaseInstance().getQuizzes();
		grades = SystemMain.gradeServer.getGrades();
		users = Database.getDatabaseInstance().getUsers();
		if (!SystemMain.currentUser.isTeacher()) {
			Predicate<Grade> notCurrentUser = p -> (p.getUserID() != SystemMain.currentUser.getUserID());
			grades.removeIf(notCurrentUser);
		}

		table = new JTable();
		table.setAutoCreateColumnsFromModel(true);
		tableModel = new GradeModel();
		table.setModel(tableModel);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	class GradeModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int rows, cols;
		Dictionary<Integer, Integer> studentTable;
		Dictionary<Integer, Integer> quizTable;
		//<User, Dictionary<Quiz, Grade>>
		//<Quiz, Grade>

		List<String> columnNames = new ArrayList<>();
		Dictionary<Integer, String> quizNameTable = new Hashtable<>();
		Dictionary<Integer, String> userTable = new Hashtable<>();

		List<Integer> quizIDList = new ArrayList<>();
		List<Integer> userIDList = new ArrayList<>();

		GradeModel() {
			rows = quizzes.size();

			// Setup quiz table
			for (Quiz quiz : quizzes) {
				quizNameTable.put(quiz.getQuizID(), quiz.getQuizName());
			}

			// Setup quizIDList
			quizIDList = Collections.list(quizNameTable.keys());
			Collections.sort(quizIDList);

			if (!SystemMain.currentUser.isTeacher()) {
				// Setup student grade table
				studentTable = new Hashtable<>();
				for (Grade grade : grades) {
					studentTable.put(grade.getQuizID(), grade.getGrade());
				}

				// Prepare column headers
				columnNames.add("Quiz");
				columnNames.add("Grade");
				cols = 2;
			} else {
				//Generate user table
				for (User user : users)
					userTable.put(user.getUserID(), user.getName());
				
				userIDList = Collections.list(userTable.keys());
				Collections.sort(userIDList);

				// Prepare column headers
				columnNames.add("Quiz");
				cols = 1;

				// Prepare menu
				menuPane.setVisible(true);
				btnRight.setText("View Grades");
				btnRight.setVisible(true);
				btnLeft.setText("User List");
				btnLeft.setVisible(true);
			}

		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return rows;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return cols;
		}

		@Override
		public String getColumnName(int columnIndex) {
			// TODO Auto-generated method stub
			return columnNames.get(columnIndex);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// TODO Auto-generated method stub
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (!SystemMain.currentUser.isTeacher()) {
				int quizID = quizIDList.get(rowIndex);
				if (columnIndex == 0) {
					return quizNameTable.get(quizID);
				} else if (columnIndex == 1) {
					Integer mygrade = studentTable.get(quizID);
					return (mygrade == null ? 0 : mygrade);
				}
			} else {
				if (teacherState == TeacherState.QUIZ) {
					int quizID = quizIDList.get(rowIndex);
					return quizNameTable.get(quizID);
				} else if (teacherState == TeacherState.GRADEQUIZ) {
					int userID = userIDList.get(rowIndex);
					if (columnIndex == 0) {
						return userTable.get(userID);
					} else if (columnIndex == 1) {
						Integer mygrade = quizTable.get(userID);
						return (mygrade == null ? 0 : mygrade);
					}
				} else if (teacherState == TeacherState.USER) {
					int userID = userIDList.get(rowIndex);
					return userTable.get(userID);
				} else if (teacherState == TeacherState.GRADEUSER) {
					int quizID = quizIDList.get(rowIndex);
					if (columnIndex == 0) {
						return quizNameTable.get(quizID);
					} else if (columnIndex == 1) {
						Integer quizgrade = quizTable.get(quizID);
						return (quizgrade == null ? 0 : quizgrade);
					}
				}
			}
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub

		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source == btnRight) {
			if (table.getSelectedRow() < 0 || table.getSelectedRow() >= tableModel.rows)
				return;
			if (teacherState == TeacherState.QUIZ) {
				tableModel.quizTable = new Hashtable<>();
				Set<Grade> gradeList = new HashSet<>(grades);
				int quizID = tableModel.quizIDList.get(table.getSelectedRow());
				selectedUserQuiz = quizID;
				Predicate<Grade> notFromQuiz = p -> p.getQuizID() != quizID;
				gradeList.removeIf(notFromQuiz);
				for (Grade grade : gradeList)
					tableModel.quizTable.put(grade.getUserID(), grade.getGrade());
				tableModel.rows = users.size();
				tableModel.cols = 2;
				tableModel.columnNames = new ArrayList<>();
				tableModel.columnNames.add("Name");
				tableModel.columnNames.add("Grade");
				table.createDefaultColumnsFromModel();
				table.updateUI();
				teacherState = TeacherState.GRADEQUIZ;
				
				btnLeft.setText("Back");
				lblTitle.setText("Quiz: " + tableModel.quizNameTable.get(quizID) + "/" + Database.getDatabaseInstance().getQuestions(quizID).size());
				btnRight.setText("Modify Grade");
			} else if (teacherState == TeacherState.USER) {
				tableModel.quizTable = new Hashtable<>();
				Set<Grade> gradeList = new HashSet<>(grades);
				int userID = tableModel.userIDList.get(table.getSelectedRow());
				selectedUserQuiz = userID;
				Predicate<Grade> notFromUser = p -> p.getUserID() != userID;
				gradeList.removeIf(notFromUser);
				for (Grade grade : gradeList)
					tableModel.quizTable.put(grade.getQuizID(), grade.getGrade());
				tableModel.rows = quizzes.size();
				tableModel.cols = 2;
				tableModel.columnNames = new ArrayList<>();
				tableModel.columnNames.add("Quiz");
				tableModel.columnNames.add("Grade");
				table.createDefaultColumnsFromModel();
				table.updateUI();
				teacherState = TeacherState.GRADEUSER;
				
				btnLeft.setText("Back");
				lblTitle.setText("User: " + tableModel.userTable.get(userID));
				btnRight.setText("Modify Grade");
			} else if (teacherState == TeacherState.GRADEQUIZ) {
				int quizID = selectedUserQuiz;
				int userID = tableModel.userIDList.get(table.getSelectedRow());
				String grade = JOptionPane.showInputDialog(null, "Replace with value", "What should this grade be?", JOptionPane.QUESTION_MESSAGE);
				if (grade != null) {
					int gradeVal;
					try {
						gradeVal = Integer.parseInt(grade);
					} catch (Exception exception) {
						return;
					}
					Grade newgrade = new Grade(userID, quizID, gradeVal);
					SystemMain.gradeServer.setGrade(newgrade);
					SystemMain.gradeServer.record(SystemMain.currentUser.getName() + " (" + SystemMain.currentUser.getUsername() + ") has modified grade for user:\"" + tableModel.userTable.get(userID) + "\"[" + userID + "] on quiz:\"" + tableModel.quizNameTable.get(quizID) + "\"[" + quizID + "] to " + gradeVal);
					grades = SystemMain.gradeServer.getGrades();
					tableModel.quizTable.put(userID, gradeVal);
					table.updateUI();
				}
			} else if (teacherState == TeacherState.GRADEUSER) {
				int quizID = tableModel.quizIDList.get(table.getSelectedRow());
				int userID = selectedUserQuiz;
				String grade = JOptionPane.showInputDialog(null, "Replace with value", "What should this grade be?", JOptionPane.QUESTION_MESSAGE);
				if (grade != null) {
					int gradeVal;
					try {
						gradeVal = Integer.parseInt(grade);
					} catch (Exception exception) {
						return;
					}
					Grade newgrade = new Grade(userID, quizID, gradeVal);
					SystemMain.gradeServer.setGrade(newgrade);
					SystemMain.gradeServer.record(SystemMain.currentUser.getName() + " (" + SystemMain.currentUser.getUsername() + ") has modified grade for user:" + userID + " on quiz:" + quizID + " to " + gradeVal);
					grades = SystemMain.gradeServer.getGrades();
					tableModel.quizTable.put(quizID, gradeVal);
					table.updateUI();
				}
			}
		} else if (source == btnLeft) {
			if (teacherState == TeacherState.GRADEQUIZ) {
				tableModel.rows = quizzes.size();
				tableModel.cols = 1;
				tableModel.columnNames = new ArrayList<>();
				tableModel.columnNames.add("Quiz");
				
				table.createDefaultColumnsFromModel();
				table.updateUI();
				teacherState = TeacherState.QUIZ;
				btnLeft.setText("User List");
				lblTitle.setText("Quizzes");
				btnRight.setText("View Grades");
			} else if (teacherState == TeacherState.GRADEUSER) {
				tableModel.rows = users.size();
				tableModel.cols = 1;
				tableModel.columnNames = new ArrayList<>();
				tableModel.columnNames.add("User");
				
				table.createDefaultColumnsFromModel();
				table.updateUI();
				teacherState = TeacherState.USER;
				btnLeft.setText("Quiz List");
				lblTitle.setText("Users");
				btnRight.setText("View Grades");
			} else if (teacherState == TeacherState.QUIZ) {
				tableModel.rows = users.size();
				tableModel.columnNames = new ArrayList<>();
				tableModel.columnNames.add("User");
				
				table.createDefaultColumnsFromModel();
				table.updateUI();
				teacherState = TeacherState.USER;
				btnLeft.setText("Quiz List");
				lblTitle.setText("Users");
				btnRight.setText("View Grades");
			} else if (teacherState == TeacherState.USER) {
				tableModel.rows = quizzes.size();
				tableModel.columnNames = new ArrayList<>();
				tableModel.columnNames.add("Quiz");
				
				table.createDefaultColumnsFromModel();
				table.updateUI();
				teacherState = TeacherState.QUIZ;
				btnLeft.setText("User List");
				lblTitle.setText("Quizzes");
				btnRight.setText("View Grades");
			}
		}

	}

}
