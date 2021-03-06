package edu.gmu.teemw.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.gmu.teemw.database.Database;
import edu.gmu.teemw.database.Grade;
import edu.gmu.teemw.database.quiz.Question;
import edu.gmu.teemw.database.quiz.QuestionCode;
import edu.gmu.teemw.database.quiz.QuestionMultipleChoice;
import edu.gmu.teemw.database.quiz.QuestionShortAnswer;
import edu.gmu.teemw.database.quiz.Quiz;

public class DatabaseTest {
	@Test
	public void TestDestroy() {
		Database db = Database.getDatabaseInstance();
		db.destroy();
		assertEquals(null, db.getUsers());
		assertEquals(null, db.getQuizzes());
	}

	@Test
	public void TestGetUsers() {
		Database db = Database.getDatabaseInstance();
		assertEquals(true, db.getUsers() != null);
	}

	@Test
	public void TestGetUser() {
		Database db = Database.getDatabaseInstance();
		assertEquals(true, db.getUser("jsmith", "passy") != null);
		assertEquals(null, db.getUser("jsmith", "pass"));
	}

	@Test
	public void TestGetQuizzes() {
		Database db = Database.getDatabaseInstance();
		assertEquals(false, db.getQuizzes() == null);
	}

	@Test
	public void getQuiz() {
		Database db = Database.getDatabaseInstance();
		assertEquals(true, db.getQuiz(1) != null);
		assertEquals(null, db.getQuiz(-1));
	}

	@Test
	public void addQuiz() {
		prepareBackup();
		Database db = Database.getDatabaseInstance();

		db.addQuiz("Test Quiz123");
		Set<Quiz> quizzes = db.getQuizzes();
		boolean testQuizFound = false;
		for (Quiz quiz : quizzes)
			if (quiz.getQuizName().equals("Test Quiz123")) testQuizFound = true;
		assertEquals(true, testQuizFound);

		testQuizFound = false;
		for (Quiz quiz : quizzes)
			if (quiz.getQuizName().equals("Test Quiz1234")) testQuizFound = true;
		assertEquals(false, testQuizFound);

		restoreBackup();
	}

	@Test
	public void addQuestion() {
		prepareBackup();
		Database db = Database.getDatabaseInstance();

		QuestionCode questioncode = new QuestionCode(0, "Question", "Input", "System.out.println(\"answer\");");
		Set<String> choices = new HashSet<String>();
		choices.add("not answer");
		choices.add("answer");
		QuestionMultipleChoice questionmultiplechoice = new QuestionMultipleChoice(0, "Question", choices, "Answer");
		QuestionShortAnswer questionshortanswer = new QuestionShortAnswer(0, "Question", "Answer");

		assertEquals(true, db.addQuestion(questioncode, 1));
		assertEquals(true, db.addQuestion(questionmultiplechoice, 1));
		assertEquals(true, db.addQuestion(questionshortanswer, 1));

		Set<Question> questions = db.getQuestions(1);
		boolean code = false, mc = false, sa = false;
		for (Question question : questions) {
			switch (question.getQuestionType()) {
			case Code:
				QuestionCode qc = (QuestionCode) question;
				if (qc.getQuestionText().equals(questioncode.getQuestionText()) &&
						qc.getAnswer().equals(questioncode.getAnswer()) &&
						qc.getInput().equals(questioncode.getInput()))
					code = true;
				break;
			case MultipleChoice:
				QuestionMultipleChoice qmc = (QuestionMultipleChoice) question;
				if (qmc.getQuestionText().equals(questionmultiplechoice.getQuestionText()) &&
						qmc.getAnswer().equals(questionmultiplechoice.getAnswer()) &&
						qmc.getChoices().containsAll(questionmultiplechoice.getChoices()))
					mc = true;
				break;
			case ShortAnswer:
				QuestionShortAnswer qsa = (QuestionShortAnswer) question;
				if (qsa.getQuestionText().equals(questionshortanswer.getQuestionText()) &&
						qsa.getAnswer().equals(questionshortanswer.getAnswer()))
					sa = true;
				break;
			}
		}

		assertEquals(true, code);
		assertEquals(true, mc);
		assertEquals(true, sa);

		restoreBackup();
	}

	public static void prepareBackup() {
		Database.getDatabaseInstance().destroy();
		try {
			copyFile(new File("test.db"), new File("testbackup.db"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void restoreBackup() {
		Database.getDatabaseInstance().destroy();
		try {
			copyFile(new File("testbackup.db"), new File("test.db"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Taken from StackOverflow (http://stackoverflow.com/questions/106770/standard-concise-way-to-copy-a-file-in-java)
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) {
				source.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
	}
}
