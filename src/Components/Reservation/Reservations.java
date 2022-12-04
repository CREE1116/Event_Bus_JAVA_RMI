package Components.Reservation;
import java.io.Serializable;
import java.util.StringTokenizer;

public class Reservations implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String studentId;
	protected String courseId;
	
	 public Reservations(String inputString) {
	        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
	    	this.studentId = stringTokenizer.nextToken();
	    	this.courseId = stringTokenizer.nextToken();
	    }
	 public String getStudentId() {
		 return this.studentId;
	 }
	 public String getCourseId() {
		 return this.courseId;
	 }
	 public String getString() {
		 return studentId+" "+courseId;
	 }
	 public boolean matchCourseId(String courseId) {
		 if(courseId.equals(this.courseId))return true;
		 return false;
	 }
	 public boolean matchStudentId(String studentId) {
		 if(studentId.equals(this.studentId))return true;
		 return false;
	 }
	 public boolean match(String studentId,String courseId) {
		 return matchStudentId(studentId)&&matchCourseId(courseId);
	 }
	 public boolean match(Reservations reservations) {
		 return matchStudentId(reservations.getStudentId())&&matchCourseId(reservations.getCourseId());
	 }
}
