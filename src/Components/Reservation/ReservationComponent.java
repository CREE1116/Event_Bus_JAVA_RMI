package Components.Reservation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Components.Course.Course;

public class ReservationComponent {
	
		protected ArrayList<Reservations> vReservations;
		private String sReservationFileName;
		public ReservationComponent(String sReservationFileName) throws FileNotFoundException, IOException {
			this.sReservationFileName = sReservationFileName;
			BufferedReader objStudentFile = new BufferedReader(new FileReader(this.sReservationFileName));
			this.vReservations = new ArrayList<Reservations>();
			while (objStudentFile.ready()) {
				String stuInfo = objStudentFile.readLine();
				if (!stuInfo.equals("")) {
					this.vReservations.add(new Reservations(stuInfo));
				}
			}
			objStudentFile.close();
		}
		public ArrayList<Reservations> getReservationList() {
	        return this.vReservations;
	    }
	    public boolean deleteReservation(Reservations reservations) {
	    	for(Reservations reservation : vReservations) {
	    		if(reservation.match(reservations)) return vReservations.remove(reservation);
	    	}
	    	return false;
	    }
	    public boolean isRegistered(Reservations reservations) {
	        for (int i = 0; i < this.vReservations.size(); i++) {
	            if(((Reservations) this.vReservations.get(i)).match(reservations)) return true;
	        }
	        return false;
	    }
	}


		



