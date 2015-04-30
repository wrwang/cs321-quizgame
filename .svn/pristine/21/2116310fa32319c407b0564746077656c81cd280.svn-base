package edu.gmu.teemw.database.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class QuestionMultipleChoice extends Question {
	String questionString;
	Set<String> choiceStrings;
	String answerString;

	public QuestionMultipleChoice(int questionID, String questionString, Set<String> choiceStrings, String answerString) {
		questionType = TYPE.MultipleChoice;
		this.questionID = questionID;
		this.questionString = questionString;
		this.choiceStrings = choiceStrings;
		this.answerString = answerString;
	}

	@Override
	public String getQuestionText() {
		return questionString;
	}

	public String getAnswer() {
		return answerString;
	}
	
	public List<String> getChoices() {
		List<String> choices = new ArrayList<>();
		choices.addAll(choiceStrings);
		Collections.shuffle(choices);
		return choices;
	}

	@Override
	public boolean validateAnswer(String answer) {
		if (answer == null) return false;
		return answer.equalsIgnoreCase(answerString);
	}

}
