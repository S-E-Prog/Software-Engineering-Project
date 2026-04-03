package domain;

import java.io.Serializable;

public class appointment implements Serializable {

	private final String appointmentId;
	private final user bookedBy;
	private final property property;
	private time appointmentTime;
	private AppointmentStatus status;

	public enum AppointmentStatus {
		CONFIRMED,
		CANCELLED,
		COMPLETED
	}

	public appointment(String appointmentId, user bookedBy, property property, time appointmentTime) {
		this.appointmentId = appointmentId;
		this.bookedBy = bookedBy;
		this.property = property;
		this.appointmentTime = appointmentTime;
		this.status = AppointmentStatus.CONFIRMED;
	}

	public void cancel() {
		this.status = AppointmentStatus.CANCELLED;
	}

	public void complete() {
		this.status = AppointmentStatus.COMPLETED;
	}

	public boolean isConfirmed() {
		return status == AppointmentStatus.CONFIRMED && appointmentTime.isstart() && !appointmentTime.isend();
	}

	public String getAppointmentTime() {
		time appTime=this.appointmentTime;
		return this.appointmentTime.toString()+ " to "+appTime.toStringendtime() ;
	}

	public void setAppointmentTime(time appointmentTime) {
		this.appointmentTime = appointmentTime;
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
		return "Appointment{id='" + appointmentId + "', property=" + property.getAddress() + ", time=" + appointmentTime
				+ ", status=" + status + "}";
	}

}
