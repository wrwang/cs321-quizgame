// Theodore Hoffer
package edu.gmu.teemw.junit;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.gmu.teemw.database.quiz.QuestionMultipleChoice;

public class QuestionMultipleChoiceTest {

	@Test
	public void Test1() {
		Set<String> answerChoices= new HashSet<String>(); 
		answerChoices.add("Wrong Answer 1");
		answerChoices.add("Right Answer");
		answerChoices.add("Wrong Answer 2");
		QuestionMultipleChoice question = new QuestionMultipleChoice(0,"Question",answerChoices,"Right Answer");
		assertEquals(question instanceof QuestionMultipleChoice, true);
	}
	@Test
	public void Test2() {
		Set<String> answerChoices= new HashSet<String>(); 
		answerChoices.add("Wrong Answer 1");
		answerChoices.add("Right Answer");
		answerChoices.add("Wrong Answer 2");
		QuestionMultipleChoice question = new QuestionMultipleChoice(0,"Question",answerChoices,"Right Answer");
		assertEquals("Right Answer",question.getAnswer() );
	}
	@Test
	public void Test3() {
		Set<String> answerChoices= new HashSet<String>(); 
		answerChoices.add("Wrong Answer 1");
		answerChoices.add("Right Answer");
		answerChoices.add("Wrong Answer 2");
		QuestionMultipleChoice question = new QuestionMultipleChoice(0,"Question",answerChoices,"Right Answer");
		assertEquals("Question",question.getQuestionText());
	}
	@Test
	public void Test4() {
		Set<String> answerChoices= new HashSet<String>(); 
		answerChoices.add("Wrong Answer 1");
		answerChoices.add("Right Answer");
		answerChoices.add("Wrong Answer 2");
		QuestionMultipleChoice question = new QuestionMultipleChoice(0,"Question",answerChoices,"Right Answer");
		assertEquals(question.validateAnswer("Right Answer"),true);
	}
	@Test
	public void Test5() {
		Set<String> answerChoices= new HashSet<String>(); 
		answerChoices.add("Wrong Answer 1");
		answerChoices.add("Right Answer");
		answerChoices.add("Wrong Answer 2");
		QuestionMultipleChoice question = new QuestionMultipleChoice(0,"Question",answerChoices,"Right Answer");
		assertEquals(question.validateAnswer("Wrong Answer 1"),false);
	}
	public void Test6() {
		Set<String> answerChoices= new HashSet<String>(); 
		answerChoices.add("Wrong Answer 1");
		answerChoices.add("Right Answer");
		answerChoices.add("Wrong Answer 2");
		QuestionMultipleChoice question = new QuestionMultipleChoice(0,"Question",answerChoices,"Right Answer");
		assertEquals(question == null, false);
	}
}
