package edu.gmu.teemw.database.quiz;

public abstract class Question {
	public static enum TYPE {Code, MultipleChoice, ShortAnswer};
	
	protected TYPE questionType;
	protected int questionID;
	
	public TYPE getQuestionType() {
		return questionType;
	}
	public int getQuestionID() {
		return questionID;
	}
	public abstract String getQuestionText();
	public abstract boolean validateAnswer(String answer);
}
