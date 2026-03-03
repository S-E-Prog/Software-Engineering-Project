package domain;


public class appointment {

	 private final String appointmentId;
	 private final user   bookedBy;
	 private final property property;
	 private timeslot timeSlot;
	 private AppointmentStatus status;	 
	 
	 
	 public enum AppointmentStatus {
		    CONFIRMED,
		    CANCELLED,
		    COMPLETED
		}
	 
	 
	  public appointment(String appointmentId, user bookedBy, property property,timeslot timeSlot) {
		  this.appointmentId = appointmentId;
		  this.bookedBy      = bookedBy;
		  this.property      = property;
		  this.timeSlot      = timeSlot;
		  this.status        = AppointmentStatus.CONFIRMED;
		  }
	  


	    public void cancel() {
	        this.status = AppointmentStatus.CANCELLED;
	    }

	    public void complete() {
	        this.status = AppointmentStatus.COMPLETED;
	    }
	    
	    public boolean isConfirmed() {
	        return status == AppointmentStatus.CONFIRMED &&timeSlot.getStartTime().isAfter(java.time.LocalDateTime.now());
	    }
	   

		public timeslot getTimeSlot() {
			return timeSlot;
		}

		public void setTimeSlot(timeslot timeSlot) {
			this.timeSlot = timeSlot;
		}

		public AppointmentStatus getStatus() {
			return status;
		}

		public void setStatus(AppointmentStatus status) {
			this.status = status;
		}


		public String getAppointmentId() {
			return appointmentId;
		}

		public user getBookedBy() {
			return bookedBy;
		}

		public property getProperty() {
			return property;
		}
		
		 @Override
		    public String toString() {
		        return "Appointment{id='" + appointmentId + "', property=" + 
		               property.getAddress() + ", time=" + timeSlot + 
		               ", status=" + status + "}";
		    }
	  
}
