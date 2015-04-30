package edu.gmu.teemw.database;

public class Grade {
	private final int userID;
	private final int quizID;
	private final int grade;
	
	public Grade(int userID, int quizID, int grade) {
		this.userID = userID;
		this.quizID = quizID;
		this.grade = grade;
	}

	public int getUserID() {
		return userID;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getGrade() {
		return grade;
	}
}
