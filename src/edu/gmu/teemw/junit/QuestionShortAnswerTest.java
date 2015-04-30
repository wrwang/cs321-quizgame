//Eric Franklin
package edu.gmu.teemw.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.gmu.teemw.database.quiz.Question;
import edu.gmu.teemw.database.quiz.QuestionShortAnswer;

public class QuestionShortAnswerTest {

	@Test
	public void Test1() {
		QuestionShortAnswer question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals(question instanceof QuestionShortAnswer, true);
	}
	@Test
	public void Test2() {
		QuestionShortAnswer question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals("Answer",question.getAnswer() );
	}
	@Test
	public void Test3() {
		QuestionShortAnswer question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals("Question",question.getQuestionText());
	}
	@Test
	public void Test4() {
		QuestionShortAnswer question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals(question.validateAnswer("Answer"),true);
	}
	@Test
	public void Test5() {
		QuestionShortAnswer question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals(question == null, false);
	}
	@Test
	public void Test6() {
		QuestionShortAnswer question = new QuestionShortAnswer(0,"Question","Answer");
		assertEquals(question.validateAnswer("Wrong"),false);
	}
}