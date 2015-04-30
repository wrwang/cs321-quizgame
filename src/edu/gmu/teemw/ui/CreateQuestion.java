package edu.gmu.teemw.ui;

import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;

import edu.gmu.teemw.SystemMain;
import edu.gmu.teemw.database.Database;
import edu.gmu.teemw.database.quiz.Question;
import edu.gmu.teemw.database.quiz.QuestionCode;
import edu.gmu.teemw.database.quiz.QuestionMultipleChoice;
import edu.gmu.teemw.database.quiz.QuestionShortAnswer;
import edu.gmu.teemw.database.quiz.Quiz;
//import edu.gmu.teemw.ui.GradeFrame.TeacherState;
//import edu.gmu.teemw.ui.GradeFrame.TeacherState;

public class CreateQuestion extends JDialog implements ActionListener {
	private static final long serialVersionUID = -5273163625781613625L;
	private JTextField txtQuestion;
	String questionString;
	private JTextField txtShortAnswer;
	private JTextField txtCodeAnswer;
	private JTextField txtCodeInput;
	private JTextField[] txtAnswerChoice;

	private String answerString;
	private Set<String> answerChoices;
	private String inputString;

	JPanel panelAnswerMultipleChoice;
	JPanel panelAnswerShort;
	JPanel panelAnswerCode;
	JPanel panelType;
	JPanel panelQuestion;
	JPanel panelQuestionCode;
	JPanel panelAnswer;
	JPanel panelQuiz;
	private Panel menuPane;
	JPanel panel_in;

	private JLabel lblTitle;
	ButtonGroup groupType;
	ButtonGroup groupType1;
	JButton btnPrevButton;
	JButton btnNextButton;
	JButton btnNextButton1;
	JButton btnNewButton_2; 
	JButton btnAddQuiz;

	GradeModel tableModel;

	JTable table;
	private Set<Quiz> quizzes;

	Box horizontalBox; 



	JRadioButton rdbtnType[] = new JRadioButton[3];
	JRadioButton rdbtnType1[] = new JRadioButton[4];

	int questionType = 0;

	static enum STATE {TYPE, QUESTION, ANSWER, QUIZ};
	STATE current = STATE.TYPE;
	List<Integer> quizIDList = new ArrayList<>();

	public CreateQuestion() {
		getContentPane().setLayout(new BorderLayout(5, 5));
		setBounds(0, 0, 500, 300);
		setLocationRelativeTo(null);
		JPanel panelMenu = new JPanel();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().add(panelMenu, BorderLayout.NORTH);
		panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.X_AXIS));

		btnPrevButton = new JButton("Back");
		panelMenu.add(btnPrevButton);
		btnPrevButton.addActionListener(this);

		Component horizontalGlue = Box.createHorizontalGlue();
		panelMenu.add(horizontalGlue);

		btnNextButton = new JButton("Next");
		panelMenu.add(btnNextButton);
		btnNextButton.addActionListener(this);

		JPanel panelMain = new JPanel();
		getContentPane().add(panelMain, BorderLayout.CENTER);
		panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.X_AXIS));

		//quiz panel

		panelQuiz = new JPanel();
		panelMain.add(panelQuiz);
		panelQuiz.setLayout(new BoxLayout(panelQuiz, BoxLayout.Y_AXIS));
		menuPane = new Panel();
		panelQuiz.add(menuPane);
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.X_AXIS));
		menuPane.add(Box.createHorizontalGlue());
		lblTitle = new JLabel("Quizzes");
		menuPane.add(lblTitle);
		menuPane.add(Box.createHorizontalGlue());
		btnAddQuiz = new JButton("New Quiz");
		menuPane.add(btnAddQuiz);
		btnAddQuiz.addActionListener(this);

		quizzes = Database.getDatabaseInstance().getQuizzes();
		table = new JTable();
		//menuPane.add(table);
		table.setAutoCreateColumnsFromModel(true);
		tableModel = new GradeModel();
		table.setModel(tableModel);
		//scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane= new JScrollPane();
		panelQuiz.add(scrollPane);
		scrollPane.setViewportView(table);
		panelQuiz.setVisible(false);

		//short answer panel

		panelAnswerShort = new JPanel();
		panelMain.add(panelAnswerShort);
		JLabel lblShortAnswer = new JLabel("Short Answer:");
		panelAnswerShort.add(lblShortAnswer);
		panelAnswerShort.setVisible(false);
		txtShortAnswer = new JTextField();
		panelAnswerShort.add(txtShortAnswer);
		txtShortAnswer.setColumns(30);
		panelAnswerShort.setVisible(false);


		//code answer panel
		panelAnswerCode = new JPanel();
		panelMain.add(panelAnswerCode);
		JLabel lblCodeInput = new JLabel("Input:");
		panelAnswerCode.add(lblCodeInput);
		txtCodeInput = new JTextField();
		panelAnswerCode.add(txtCodeInput);
		txtCodeInput.setColumns(30);

		JLabel lblCodeAnswer = new JLabel("Code Answer:");
		panelAnswerCode.add(lblCodeAnswer);
		txtCodeAnswer = new JTextField();
		panelAnswerCode.add(txtCodeAnswer);
		txtCodeAnswer.setColumns(30);

		panelAnswerCode.setVisible(false);

		/* Multiple Choice Answers */
		panelAnswerMultipleChoice = new JPanel();
		panel_in = new JPanel();
		panelAnswerMultipleChoice.setAlignmentY(Component.TOP_ALIGNMENT);
		panelMain.add(panelAnswerMultipleChoice);
		panelAnswerMultipleChoice.setLayout(new BorderLayout(0, 0));
		panelAnswerMultipleChoice.add(panel_in, BorderLayout.NORTH);

		panel_in.setLayout(new BoxLayout(panel_in, BoxLayout.Y_AXIS));

		Box horizontalBox = Box.createHorizontalBox();
		panel_in.add(horizontalBox);
		rdbtnType1[0] = new JRadioButton("");
		horizontalBox.add(rdbtnType1[0]);
		txtAnswerChoice = new JTextField[4];
		for (int i = 0; i < txtAnswerChoice.length; i++) {
			txtAnswerChoice[i] = new JTextField();
		}
		horizontalBox.add(txtAnswerChoice[0]);
		txtAnswerChoice[0].setColumns(10);

		horizontalBox = Box.createHorizontalBox();
		panel_in.add(horizontalBox);
		rdbtnType1[1] = new JRadioButton("");
		horizontalBox.add(rdbtnType1[1]);
		horizontalBox.add(txtAnswerChoice[1]);
		txtAnswerChoice[1].setColumns(10);

		horizontalBox = Box.createHorizontalBox();
		panel_in.add(horizontalBox);
		rdbtnType1[2] = new JRadioButton("");
		horizontalBox.add(rdbtnType1[2]);
		horizontalBox.add(txtAnswerChoice[2]);
		txtAnswerChoice[2].setColumns(10);

		horizontalBox = Box.createHorizontalBox();
		panel_in.add(horizontalBox);
		rdbtnType1[3] = new JRadioButton("");
		horizontalBox.add(rdbtnType1[3]);
		horizontalBox.add(txtAnswerChoice[3]);
		txtAnswerChoice[3].setColumns(10);

		groupType1 = new ButtonGroup();
		groupType1.add(rdbtnType1[0]);
		groupType1.add(rdbtnType1[1]);
		groupType1.add(rdbtnType1[2]);
		groupType1.add(rdbtnType1[3]);
		panelAnswerMultipleChoice.setVisible(false);

		/* TYPE PANEL */
		panelType = new JPanel();
		panelMain.add(panelType);

		rdbtnType[0] = new JRadioButton("Code");
		panelType.add(rdbtnType[0]);

		rdbtnType[1] = new JRadioButton("Multiple Choice");
		panelType.add(rdbtnType[1]);

		rdbtnType[2] = new JRadioButton("Short Answer");
		panelType.add(rdbtnType[2]);

		groupType = new ButtonGroup();
		groupType.add(rdbtnType[0]);
		groupType.add(rdbtnType[1]);
		groupType.add(rdbtnType[2]);

		
		/* QUESTION PANEL */
		panelQuestion = new JPanel();
		panelMain.add(panelQuestion);

		JLabel lblQuestions = new JLabel("Questions:");
		panelQuestion.add(lblQuestions);
		panelQuestion.setVisible(false);

		txtQuestion = new JTextField();
		panelQuestion.add(txtQuestion);
		txtQuestion.setColumns(30);
		panelQuestion.setVisible(false);
	}

	class GradeModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int rows, cols;

		Dictionary<Integer, Integer> quizTable;
		//<User, Dictionary<Quiz, Grade>>
		//<Quiz, Grade>

		List<String> columnNames = new ArrayList<>();
		Dictionary<Integer, String> quizNameTable = new Hashtable<>();


		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			int quizID = quizIDList.get(rowIndex);
			if (columnIndex == 0) {
				return quizNameTable.get(quizID);
			}
			return null;
		}

		GradeModel() {
			rows = quizzes.size();

			// Setup quiz table
			for (Quiz quiz : quizzes) {
				quizNameTable.put(quiz.getQuizID(), quiz.getQuizName());
			}

			// Setup quizIDList
			quizIDList = Collections.list(quizNameTable.keys());
			Collections.sort(quizIDList);

		}
		@Override
		public int getRowCount() {
			return quizzes.size();
		}
		@Override
		public int getColumnCount() {
			return 1;
		}
		@Override
		public String getColumnName(int columnIndex) {
			return "Quiz";
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		}
		@Override
		public void addTableModelListener(TableModelListener l) {

		}
		@Override
		public void removeTableModelListener(TableModelListener l) {

		}

	}





	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnNextButton) {
			nextButton();
		} else if (source == btnPrevButton) {
			previousButton();
		} else if (source == btnAddQuiz) {
			String quizName = JOptionPane.showInputDialog(null, "New Quiz Title", "What should the title of the new quiz be?", JOptionPane.QUESTION_MESSAGE);
			Database.getDatabaseInstance().addQuiz(quizName);
			quizzes = Database.getDatabaseInstance().getQuizzes();
			table.setModel(new GradeModel());
			table.updateUI();
		}
	}

	void nextButton() {
		if (current == STATE.TYPE) {
			questionType = -1;
			for (int i = 0; i < rdbtnType.length; i++) {
				JRadioButton button = rdbtnType[i];
				if (button.isSelected()) {
					questionType = i;
					break;
				}
			}
			if (questionType < 0) {
				JOptionPane.showMessageDialog(this, "Please select a question type!");
				return;
			}

			current = STATE.QUESTION;
			panelType.setVisible(false);
			panelQuestion.setVisible(true);
		} else if(current == STATE.QUESTION) {
			questionString = txtQuestion.getText();
			if (questionString.trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a question!");
				return;
			}
			if(questionType == 0) {
				current = STATE.ANSWER;
				panelQuestion.setVisible(false);
				panelAnswerCode.setVisible(true);
			} else if(questionType == 1) {
				current = STATE.ANSWER;
				panelQuestion.setVisible(false);
				panelAnswerMultipleChoice.setVisible(true);
			} else if(questionType == 2) {
				current = STATE.ANSWER;
				panelQuestion.setVisible(false);
				panelAnswerShort.setVisible(true);
			}
		} else if(current == STATE.ANSWER) {
			if (questionType ==1) {
				int choiceCount = 0;
				boolean choiceSelected = false;
				answerChoices = new HashSet<>();
				for (int i = 0; i < rdbtnType1.length; i++) {
					JRadioButton button = rdbtnType1[i];
					JTextField textfield = txtAnswerChoice[i];
					if (button.isSelected() && !textfield.getText().trim().equals("")) {
						answerString = textfield.getText().trim();
						choiceSelected = true; 
					}
					if (!textfield.getText().trim().equals("")) {
						answerChoices.add(textfield.getText().trim());
						choiceCount++;
					}
				}
				if (!choiceSelected) {
					JOptionPane.showMessageDialog(this, "Please select a correct answer!");
					return;
				}
				if(choiceCount < 2) {
					JOptionPane.showMessageDialog(this, "Please enter at least two possible solutions.");
					return;
				}
				current = STATE.QUIZ;

				panelAnswerMultipleChoice.setVisible(false);
				panelQuiz.setVisible(true);
			} else {
				if (questionType == 2) {
					answerString = txtShortAnswer.getText();
				} else if (questionType == 0) {
					answerString = txtCodeAnswer.getText();
					inputString = txtCodeInput.getText().trim();
				} 

				if (answerString.trim().equals("")) {
					JOptionPane.showMessageDialog(this, "Please enter a correct answer!");
					return;
				}
				current = STATE.QUIZ;
				panelAnswerCode.setVisible(false);
				panelAnswerShort.setVisible(false);
				panelQuiz.setVisible(true);
			}

		} else if (current == STATE.QUIZ) {
			if (table.getSelectedRow() < 0 || table.getSelectedRow() >= quizzes.size()) {
				JOptionPane.showMessageDialog(this, "Please select a valid quiz");
				return;
			}
			int quizID = quizIDList.get(table.getSelectedRow());
			int type = questionType;
			String question = questionString;
			String answer = answerString;
			Question newQuestion = null;
			switch (type) {
			case 0: // code
				if (question.trim().equals("")) return;
				if (answer.trim().equals("")) return;
				String input = (inputString == null ? "" : inputString);
				newQuestion = new QuestionCode(0, question, input, answer);
				break;
			case 1: // multiple choice
				if (question.trim().equals("")) return;
				if (answer.trim().equals("")) return;
				if (answerChoices.size() < 2) return;
				newQuestion = new QuestionMultipleChoice(0, question, answerChoices, answer);
				break;
			case 2: // short answer
				if (question.trim().equals("")) return;
				if (answer.trim().equals("")) return;
				newQuestion = new QuestionShortAnswer(0, question, answer);
				break;
			}
			Database.getDatabaseInstance().addQuestion(newQuestion, quizID);
			SystemMain.gradeServer.record(SystemMain.currentUser.getName() + " (" + SystemMain.currentUser.getUsername() + ") has created a question for quiz:" + quizID);
			JOptionPane.showMessageDialog(this, "Successfully added your question!");
			this.dispose();
		}
	}

	void previousButton() {
		if (current == STATE.QUESTION) {
			current = STATE.TYPE;

			panelQuestion.setVisible(false);
			panelType.setVisible(true);

		} else if(current == STATE.ANSWER) {
			current = STATE.QUESTION;
			panelAnswerCode.setVisible(false);
			panelAnswerMultipleChoice.setVisible(false);
			panelAnswerShort.setVisible(false);
			panelQuestion.setVisible(true);
		} else if(current == STATE.QUIZ) {
			current = STATE.ANSWER;
			if(questionType == 0) {
				panelQuiz.setVisible(false);
				panelAnswerCode.setVisible(true);

			} else if(questionType ==1) {
				panelQuiz.setVisible(false);
				panelAnswerMultipleChoice.setVisible(true);

			} else if(questionType ==2) {
				panelQuiz.setVisible(false);
				panelAnswerShort.setVisible(true);

			}
		}
	}
}
