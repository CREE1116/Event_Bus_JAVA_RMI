/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import Components.Course.CourseComponent;
import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class StudentMain {
	public static void main(String args[]) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** StudentMain(ID:" + componentId + ") is successfully registered. \n");

		StudentComponent studentsList = new StudentComponent("Students.txt");
		Event event = null;
		boolean done = false;
		while (!done) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent(i);
				switch (event.getEventId()) {
				case ListStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeStudentList(studentsList)));
					break;
				case RegisterStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, registerStudent(studentsList,event.getMessage())));
					break;
				case DeleteStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, deleteStudent(studentsList,event.getMessage())));
					break;
				case RegisterReservations:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.Response, findStudent(studentsList,event.getMessage())));
					break;
				case QuitTheSystem:
					printLogEvent("Get", event);
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
			eventQueue.clearEventQueue();
		}
	}
	private static String registerStudent(StudentComponent studentsList, String message) {
		Student  student = new Student(message);
		if (!studentsList.isRegisteredStudent(student.studentId)) {
			studentsList.vStudent.add(student);
			return "This student is successfully added.";
		} else
			return "This student is already registered.";
	}
	private static String makeStudentList(StudentComponent studentsList) {
		String returnString = "";
		for (int j = 0; j < studentsList.vStudent.size(); j++) {
			returnString += studentsList.getStudentList().get(j).getString() + "\n";
		}
		return returnString;
	}
	private static ArrayList<Student> getStudentList(StudentComponent studentsList) {
		return studentsList.getStudentList();
	}
	private static String findStudent(StudentComponent studentsList,String message) {
		for(Student student : studentsList.getStudentList())
			if(student.match(message.split(" ")[0]))return "S:"+student.getString();
		return "S:0";
	}
	private static String deleteStudent(StudentComponent studentsList,String studentId) {
		if(studentsList.deleteStudent(studentId))
			return studentId+" is deleted";
		return  studentId+" delete fail";
	}
	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
