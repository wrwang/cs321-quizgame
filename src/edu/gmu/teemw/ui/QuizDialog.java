package edu.gmu.teemw.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
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
import edu.gmu.teemw.database.quiz.Question;
import edu.gmu.teemw.database.quiz.Question.TYPE;
import edu.gmu.teemw.database.quiz.QuestionMultipleChoice;
import edu.gmu.teemw.database.quiz.Quiz;
import edu.gmu.teemw.gradeserver.GradeServer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import java.awt.Panel;

import javax.swing.SwingConstants;

public class QuizDialog extends JDialog implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private Panel menuPane;
	private Set<Quiz> quizzes;
	private Set<Grade> grades;
	private String answer;

	private QuizModel tableModel;
	private JButton btnRight;
	/**
	 * Create the frame.
	 */
	public QuizDialog() {
		setTitle("Quizzes");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 400, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		menuPane = new Panel();
		menuPane.setVisible(false);
		contentPane.add(menuPane);
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.X_AXIS));

		btnRight = new JButton("Select Quiz");
		btnRight.setHorizontalAlignment(SwingConstants.RIGHT);
		btnRight.setVisible(true);
		btnRight.addActionListener(this);
		menuPane.add(btnRight);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		quizzes = Database.getDatabaseInstance().getQuizzes();
		grades = SystemMain.gradeServer.getGrades();
		Database.getDatabaseInstance().getUsers();
		if (!SystemMain.currentUser.isTeacher()) {
			Predicate<Grade> notCurrentUser = p -> (p.getUserID() != SystemMain.currentUser.getUserID());
			grades.removeIf(notCurrentUser);
		}

		table = new JTable();
		table.setAutoCreateColumnsFromModel(true);
		tableModel = new QuizModel();
		table.setModel(tableModel);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	class QuizModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int rows, cols;
		Dictionary<Integer, Integer> studentTable;
		Dictionary<Integer, Integer> quizTable;

		List<String> columnNames = new ArrayList<>();
		Dictionary<Integer, String> quizNameTable = new Hashtable<>();
		Dictionary<Integer, String> userTable = new Hashtable<>();

		List<Integer> quizIDList = new ArrayList<>();
		List<Integer> userIDList = new ArrayList<>();
		List<String> choiceList = new ArrayList<>();

		QuizModel() {
			rows = quizzes.size();

			// Setup quiz table
			for (Quiz quiz : quizzes) {
				quizNameTable.put(quiz.getQuizID(), quiz.getQuizName());
			}
			// Setup quizIDList
			quizIDList = Collections.list(quizNameTable.keys());
			Collections.sort(quizIDList);

			// Prepare column headers
			columnNames.add("Quiz");
			cols = 1;

			menuPane.setVisible(true);
			btnRight.setText("Select Quiz");
			btnRight.setVisible(true);
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
			int quizID = quizIDList.get(rowIndex);
			return quizNameTable.get(quizID);
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
	public void mouseClicked(MouseEvent e) {
		table.rowAtPoint(e.getPoint());
		table.columnAtPoint(e.getPoint());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source == btnRight) {
			if (table.getSelectedRow() < 0 || table.getSelectedRow() >= tableModel.rows)
				return;
			//looking at the list of quizzes

			tableModel.quizTable = new Hashtable<>();
			int quizID = tableModel.quizIDList.get(table.getSelectedRow());
			Set<Question> questionSet = Database.getDatabaseInstance().getQuestions(quizID);
			List<Question> questionList= new ArrayList<Question>(questionSet);
			Collections.shuffle(questionList);
			//get questions for selected quizID

			int score = 0;
			for(Question quest : questionList){
				TYPE questionType = quest.getQuestionType();
				if (questionType == TYPE.Code){
					answer = (String)JOptionPane.showInputDialog(null,quest.getQuestionText(),"Code Question",JOptionPane.PLAIN_MESSAGE);
				}else if(questionType == TYPE.MultipleChoice){
					Object[] possibilities = new Object[((QuestionMultipleChoice)quest).getChoices().size()];
					((QuestionMultipleChoice)quest).getChoices().toArray(possibilities);
					answer = (String)JOptionPane.showInputDialog(null,quest.getQuestionText(),"Multiple Choice Question",JOptionPane.PLAIN_MESSAGE,null,possibilities,null);
				}else if(questionType == TYPE.ShortAnswer){
					answer = (String)JOptionPane.showInputDialog(null,quest.getQuestionText(),"Short Answer Question",JOptionPane.PLAIN_MESSAGE);
				}
				if (answer == null) break;
				if(quest.validateAnswer(answer)){
					score++;
				}
			}
			double scorePercent = (double)score/questionList.size() * 100;
			JOptionPane.showMessageDialog(null,"Grade: "+ scorePercent +"%","Quiz Score",JOptionPane.PLAIN_MESSAGE);
			Grade newgrade = new Grade(SystemMain.currentUser.getUserID(), quizID, score);
			SystemMain.gradeServer.setGrade(newgrade);
			SystemMain.gradeServer.record(SystemMain.currentUser.getName() + " (" + SystemMain.currentUser.getUsername() + ") took quiz:\"" + tableModel.quizNameTable.get(quizID) +"\"[" + quizID + "] and got score:" + score);
		}

	}

}
