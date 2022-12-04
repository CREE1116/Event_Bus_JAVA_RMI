/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.Reservation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class ReservationMain {
	static String cousreString ="";
	static String studentString ="";
	public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("ReservationMain (ID:" + componentId + ") is successfully registered...");

		ReservationComponent reservationList = new ReservationComponent("Reservations.txt");
		Event event = null;
		boolean done = false;
		while (!done) {
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent(i);
				switch (event.getEventId()) {
				case ListReservations:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeReservationList(reservationList)));
					break;
				case Response:
					printLogEvent("Get", event);
					if(event.getMessage().split(":")[0].equals("C"))cousreString = event.getMessage().split(":")[1];
					if(event.getMessage().split(":")[0].equals("S"))studentString = event.getMessage().split(":")[1];
					if(cousreString.length() >0 && studentString.length()>0 ) {
						eventBus.sendEvent(new Event(EventId.ClientOutput,registerReservation(reservationList)));
						studentString = ""; cousreString="";
					}
					break;
				case QuitTheSystem:
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
	private static String registerReservation(ReservationComponent reservationList) throws RemoteException {
		Student student=null;
		Course course=null;
		if(studentString.equals("0"))return "Student not exist: Reservation fail";
		if(cousreString.equals("0"))return "Course not exist: Reservation fail";
		student = new Student(studentString);
		course = new Course(cousreString);
		if(!checkCompletedCourses(student,course))return "I didn't take the prerequisite course";
		Reservations reservation = new Reservations(student.studentId+" "+course.courseId);
		if (!reservationList.isRegistered(reservation)) {
			reservationList.getReservationList().add(reservation);
			return "This Reservation is successfully added.";
		} else
			return "This Reservation is already registered.";
	}
	private static boolean checkCompletedCourses(Student student, Course course) {
		ArrayList<String> CompletedCoursesList = student.getCompletedCourses();
		for(String PrerequisiteCourses : course.getPrerequisiteCoursesList())
			if(!CompletedCoursesList.contains(PrerequisiteCourses))return false;
		return true;
	}
	private static String makeReservationList(ReservationComponent reservationList) {
		String returnString = "";
		for (int j = 0; j < reservationList.getReservationList().size(); j++) {
			returnString += reservationList.getReservationList().get(j).getString() + "\n";
		}
		return returnString;
	}
	private static String deleteReservation(ReservationComponent reservationList,String message) {
		Reservations reservation = new Reservations(message);
		if(reservationList.deleteReservation(reservation))
			return reservation.getString()+" is deleted";
		return  reservation.getString()+" delete fail";
	}
	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
