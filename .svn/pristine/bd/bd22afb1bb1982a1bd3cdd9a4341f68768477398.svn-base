package edu.gmu.teemw.database.quiz;

public class QuestionShortAnswer extends Question {
	private String questionString;
	private String answerString;
	
	public QuestionShortAnswer(int questionID, String questionString, String answerString) {
		questionType = TYPE.ShortAnswer;
		this.questionID = questionID;
		this.questionString = questionString;
		this.answerString = answerString;
	}
	
	@Override
	public String getQuestionText() {
		return questionString;
	}
	
	public String getAnswer() {
		return answerString;
	}
	
	@Override
	public boolean validateAnswer(String answer) {
		if (answer == null) return false;
		return answer.equalsIgnoreCase(answerString);
	}

}
