package edu.gmu.teemw.database.quiz;

public class Quiz {
	private final String quizName;
	private final int quizID;
	
	public Quiz(int quizID, String quizName) {
		this.quizID = quizID;
		this.quizName = quizName;
	}

	public int getQuizID() {
		return quizID;
	}
	
	public String getQuizName() {
		return quizName;
	}
	
	@Override
	public String toString() {
		return quizID + ". " + quizName;
	}
}
