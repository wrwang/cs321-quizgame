package edu.gmu.teemw.database;

import java.util.HashSet;
import java.util.Set;
import java.sql.*;

import edu.gmu.teemw.database.quiz.Question;
import edu.gmu.teemw.database.quiz.QuestionCode;
import edu.gmu.teemw.database.quiz.QuestionMultipleChoice;
import edu.gmu.teemw.database.quiz.QuestionShortAnswer;
import edu.gmu.teemw.database.quiz.Quiz;

public class Database {
	private static final String JDBC_DRIVER = "org.sqlite.JDBC";
	private static final String DATABASE = "jdbc:sqlite:test.db";

	private static Database instance;
	public static final Database getDatabaseInstance() {
		if (instance == null)
			instance = new Database();
		return instance;
	}

	Connection connection;

	public Database() {
		instance = this;
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DATABASE);
		} catch (Exception e) {
			System.err.println("Error connecting with the database...");
			System.exit(1);
		}
	}

	public void destroy() {
		try {
			connection.close();
		} catch (Exception e) {
			System.err.println("Error destroying database connection...");
		}
		instance = null;
	}
	
	public Set<User> getUsers() {
		Set<User> users = new HashSet<>();
		try {
			String sql = "SELECT * FROM users";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			while (results.next()) {
				int id = results.getInt("uid");
				String name = results.getString("name");
				String username = results.getString("username");
				String password = results.getString("password");
				int isteacher = results.getInt("isteacher");
				User user = new User(id, name, username, password, (isteacher == 1));
				users.add(user);
			}
			results.close();
			statement.close();
		} catch (Exception e) {
			return null;
		}
		return users;
	}

	public User getUser(String username, String password) {
		User user = null;
		try {
			String sql = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			while (results.next()) {
				int id = results.getInt("uid");
				String name = results.getString("name");
				username = results.getString("username");
				password = results.getString("password");
				int isteacher = results.getInt("isteacher");
				user = new User(id, name, username, password, isteacher == 1);
			}
			results.close();
			statement.close();
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	public Set<Quiz> getQuizzes() {
		Set<Quiz> quizzes = new HashSet<>();
		String sql = "SELECT * FROM quizzes";
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			while (results.next()) {
				int id = results.getInt("qid");
				String name = results.getString("name").replaceAll("\\\\'", "'");;
				Quiz quiz = new Quiz(id, name);
				quizzes.add(quiz);
			}
			results.close();
			statement.close();
			return quizzes;
		} catch (Exception e) {
			return null;
		}
	}
	
	public int addQuiz(String quizName) {
		int key = 0;
		try {
			quizName = quizName.replaceAll("'", "\\\\'");
			String sql = "INSERT INTO quizzes ('name') VALUES ('" + quizName + "')";
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			ResultSet keys = statement.getGeneratedKeys();
			key = keys.getInt(1);
			statement.close();
			return key;
		} catch (Exception e) {
			return 0;
		}
	}

	public Set<Question> getQuestions(int quizID) {
		Set<Question> questions = new HashSet<>();
		try {
			String sql = "SELECT * FROM questions WHERE quiz='" + quizID + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			while (results.next()) {
				int id = results.getInt("qid");
				String questionString = results.getString("question").replaceAll("\\\\'", "'");
				String answerString = results.getString("answer").replaceAll("\\\\'", "'");
				String inputString = results.getString("input");
				if (inputString != null) inputString = inputString.replaceAll("\\\\'", "'");
				int type = results.getInt("type");
				Question question = null;
				switch (type) {
				case 0: // C
					question = new QuestionCode(id, questionString, inputString, answerString);
					break;
				case 1: // MC
					Set<String> choices = new HashSet<>();
					String[] inputSplit = inputString.split(";");
					for (int i = 0; i < inputSplit.length; i++)
						choices.add(inputSplit[i]);
					question = new QuestionMultipleChoice(id, questionString, choices, answerString);
					break;
				case 2: // SA
					question = new QuestionShortAnswer(id, questionString, answerString);
					break;
				}
				if (question != null)
					questions.add(question);
			}
			results.close();
			statement.close();
			return questions;
		} catch (Exception e) {
			return null;
		}
	}

	public Quiz getQuiz(int id) {
		Quiz quiz = null;
		try {
			String sql = "SELECT * FROM quizzes WHERE qid='" + id + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			while (results.next()) {
				int qid = results.getInt("qid");
				String name = results.getString("name");
				quiz = new Quiz(qid, name);
			}
			return quiz;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean addQuestion(Question question, int quizID) {
		boolean success = false;
		try {
			String questionString = question.getQuestionText();
			String inputString = "", answerString = "";
			int type = -1;
			switch (question.getQuestionType()) {
			case Code:
				type = 0;
				QuestionCode qc = (QuestionCode) question;
				inputString = qc.getInput();
				answerString = qc.getAnswer();
				break;
			case MultipleChoice:
				type = 1;
				QuestionMultipleChoice mc = (QuestionMultipleChoice) question;
				for (String choice : mc.getChoices())
					choice = choice.replaceAll(";", "");
				inputString = String.join(";",mc.getChoices());
				answerString = mc.getAnswer();
				break;
			case ShortAnswer:
				type = 2;
				QuestionShortAnswer sa = (QuestionShortAnswer) question;
				answerString = sa.getAnswer();
			}
			questionString = questionString.replaceAll("'", "\\\\'");
			inputString = inputString.replaceAll("'", "\\\\'");
			answerString = answerString.replaceAll("'", "\\\\'");
			
			String sql = "INSERT INTO questions (type, question, input, answer, quiz) VALUES ('" + type + "', '" + questionString + "', '" + inputString + "', '" + answerString + "', '" + quizID + "')";
			
			Statement statement = connection.createStatement();
			success = statement.executeUpdate(sql) == 1;
			statement.close();
			return success;
		} catch (Exception e) {
			return false;
		}
	}
}
