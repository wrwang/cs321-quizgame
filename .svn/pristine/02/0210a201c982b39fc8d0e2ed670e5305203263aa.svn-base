package edu.gmu.teemw.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.gmu.teemw.database.quiz.Question;
import edu.gmu.teemw.database.quiz.QuestionShortAnswer;

public class ExampleTest {

	@Test
	public void Test1() {
		Question question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals(question instanceof Question, true);
		assertEquals(question.validateAnswer("Answer"), true);
		assertEquals(question.validateAnswer("Not Answer"), false);
	}
}
