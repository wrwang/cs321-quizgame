package edu.gmu.teemw.gradeserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.gmu.teemw.database.Grade;

public class GradeServer implements Runnable {
	private static final String JDBC_DRIVER = "org.sqlite.JDBC";
	private static final String DATABASE = "jdbc:sqlite:grades.db";

	private StringBuffer buffer = new StringBuffer();
	
	Connection connection;
	private static boolean running = false;

	public GradeServer() {
		running = true;
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DATABASE);
		} catch (Exception e) {
			System.err.println("Error connecting with the database...");
			System.exit(1);
		}
	}

	@Override
	public void run() {
		while (running) {
		    try {
		        Thread.sleep(6 * 1000);
		        flush();
		    }
		    catch (Exception ie) {
		        ie.printStackTrace();
		    }
		}
	}
	
	public void record(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String datetime = sdf.format(new Date(System.currentTimeMillis()));
		buffer.append("[" + datetime + "] " + s + "\n");
	}
	
	private void flush() throws IOException {
		if (buffer.length() < 1) return;
		BufferedWriter gradeFile = new BufferedWriter(new FileWriter("log.txt",true));
		gradeFile.append(buffer.toString());
		buffer = new StringBuffer();
		gradeFile.close();
		//System.out.println("Log flushed!");
	}
	
	public boolean setGrade(Grade grade) {
		boolean success = false;
		try {
			String sql = "SELECT COUNT(*) AS count FROM grades WHERE uid='" + grade.getUserID() + "' AND qid='" + grade.getQuizID() + "'";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			if (results.getInt("count") >= 1) {
				// UPDATE TABLE
				sql = "UPDATE grades SET grade='" + grade.getGrade() + "' WHERE uid='" + grade.getUserID() + "' AND qid='" + grade.getQuizID() + "'";
				Statement update = connection.createStatement();
				if (update.executeUpdate(sql) >= 1) success = true;
				update.close();
			} else {
				// INSERT NEW
				sql = "INSERT INTO grades VALUES ('" + grade.getUserID() + "', '" + grade.getQuizID() + "', '" + grade.getGrade() + "')";
				Statement insert = connection.createStatement();
				if (insert.execute(sql)) success = true;
				insert.close();
			}
			results.close();
			statement.close();
			return success;
		} catch (Exception e) {
			return false;
		}
	}
 
	public Set<Grade> getGrades() {
		Set<Grade> grades = new HashSet<>();
		try {
			String sql = "SELECT * FROM grades";
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			while (results.next()) {
				int uid = results.getInt("uid");
				int qid = results.getInt("qid");
				int grade = results.getInt("grade");
				grades.add(new Grade(uid, qid, grade));
			}
			results.close();
			statement.close();
			return grades;
		} catch (Exception e) {
			return null;
		}
	}

	public void halt() {
		running = false;
		try {
			flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
