package edu.gmu.teemw.database.quiz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class QuestionCode extends Question {
	private String questionString;
	private String inputString;
	private String answerString;
	
	public QuestionCode(int questionID, String questionString, String inputString, String answerString) {
		questionType = TYPE.Code;
		this.questionID = questionID;
		this.questionString = questionString;
		this.inputString = inputString;
		this.answerString = answerString;
	}
	
	@Override
	public String getQuestionText() {
		return questionString;
	}
	
	public String getInput() {
		return inputString;
	}
	
	public String getAnswer() {
		return answerString;
	}

	@Override
	public boolean validateAnswer(String answer) {
		if (answer == null) return false;
		
		StringBuffer correctResult = new StringBuffer();
		StringBuffer testResult = new StringBuffer();
		try {
			//TODO make sure exceptions are similar/same
			wrapCodeInFile(answerString,"Correct");
			wrapCodeInFile(answer,"Test");
			BufferedWriter input = new BufferedWriter(new FileWriter("input.txt"));
			input.write(inputString+"\n");
			input.close();
			
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec("javac Correct.java");
			pr.waitFor();
			pr = run.exec("java Correct");
			PrintWriter inputWriter = new PrintWriter(pr.getOutputStream());
			inputWriter.print(inputString);
			inputWriter.close();
			Scanner scanner = new Scanner(pr.getInputStream());
			while (scanner.hasNextLine())
				correctResult.append(scanner.nextLine() + "\n");
			pr.waitFor();
			scanner.close();
			pr.getInputStream().close();
			pr.destroy();
			
			pr = run.exec("javac Test.java");
			scanner = new Scanner(pr.getInputStream());
			while (scanner.hasNextLine())
				testResult.append(scanner.nextLine() + "\n");
			if (!testResult.toString().trim().equals("")) {
				scanner.close();
				return false;
			}
			pr.waitFor();
			scanner.close();
			pr = run.exec("java Test");
			inputWriter = new PrintWriter(pr.getOutputStream());
			inputWriter.print(inputString);
			inputWriter.close();
			scanner = new Scanner(pr.getInputStream());
			while (scanner.hasNextLine())
				testResult.append(scanner.nextLine() + "\n");
			pr.waitFor();
			scanner.close();
			pr.getInputStream().close();
			pr.destroy();
			
			return correctResult.toString().trim().equals(testResult.toString().trim());
		} catch (Exception e) {
			return false;
		}
	}

	private void wrapCodeInFile(String code, String fileName) throws IOException {
		code = "public class " + fileName + " {\n"
				+ "\tpublic static void main(String[] args){\n"
				+ code + "\n"
				+ "\t}\n"
				+ "}\n";
		BufferedWriter file = new BufferedWriter(new FileWriter(fileName+".java"));
		file.write(code);
		file.close();
	}
}
