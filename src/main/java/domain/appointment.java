package domain;

import java.io.Serializable;
import java.util.ArrayList;

public class appointment implements Serializable {

	private final String appointmentId;
	private final ArrayList<user> bookedBy;
	private final property property;
	private time appointmentTime;
	private AppointmentStatus status;

	public enum AppointmentStatus {
		AVAILABLE,
		CONFIRMED,
		CANCELLED,
		COMPLETED
	}

	public appointment(String appointmentId, property property, time appointmentTime) {
		this.appointmentId = appointmentId;
		this.bookedBy = new ArrayList<>();
		this.property = property;
		this.appointmentTime = appointmentTime;
		this.status = AppointmentStatus.AVAILABLE;
	}

	public boolean addBooking(user u) {
		for (user existing : bookedBy)
			if (existing.getId().equals(u.getId())) return false; // مسجل مسبقاً
		bookedBy.add(u);
		return true;
	}


	public boolean removeBooking(user u) {
		return bookedBy.removeIf(existing -> existing.getId().equals(u.getId()));
	}


	public boolean isBookedBy(user u) {
		for (user existing : bookedBy)
			if (existing.getId().equals(u.getId())) return true;
		return false;
	}


	public int getBookingCount() {
		return bookedBy.size();
	}

	public void cancel() {
		this.status = AppointmentStatus.CANCELLED;
	}

	public void complete() {
		this.status = AppointmentStatus.COMPLETED;
	}

	public void confirm() {
		this.status = AppointmentStatus.CONFIRMED;
	}

	public boolean isAvailable() {
		return status == AppointmentStatus.AVAILABLE;
	}


	public boolean isConfirmed() {
		return status == AppointmentStatus.CONFIRMED;
	}

	public boolean isExpired() {
		return appointmentTime.isend();
	}

	public String getAppointmentTimeString() {
		return this.appointmentTime.toString() + " to " + this.appointmentTime.toStringendtime();
	}
	public time getAppointmentTime() {
    return this.appointmentTime;
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


	public ArrayList<user> getBookedBy() {
		return bookedBy;
	}

	public property getProperty() {
		return property;
	}

	@Override
	public String toString() {
		return "Appointment{id='" + appointmentId + "', property=" + property.getAddress()
				+ ", time=" + appointmentTime + ", status=" + status
				+ ", bookings=" + bookedBy.size() + "}";
	}

}