package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class appointment implements Serializable {

	private final String appointmentId;
	private final ArrayList<user> bookedBy;
	private final property property;
	private time appointmentTime;
	private AppointmentStatus status;
	private AppointmentType type;


	public enum AppointmentStatus {
		AVAILABLE,
		CONFIRMED,
		CANCELLED,
		COMPLETED
	}


	public enum AppointmentType {

		URGENT("Urgent",15,   1,   false ),
		FOLLOW_UP("Follow-Up",30,1,true),
		ASSESSMENT(	"Assessment",45,3,true	),
		VIRTUAL("Virtual",45,10,true),
		IN_PERSON("In-Person",45,5,false),
		INDIVIDUAL("Individual",30,1,true),
		GROUP("Group",45,20,true);

		private final String displayName;
		private final int    maxDurationMinutes;
		private final int    maxParticipants;
		private final boolean canBeVirtual;

		AppointmentType(String displayName, int maxDurationMinutes,int maxParticipants, boolean canBeVirtual) {
			this.displayName                   = displayName;
			this.maxDurationMinutes            = maxDurationMinutes;
			this.maxParticipants               = maxParticipants;
			this.canBeVirtual                  = canBeVirtual;
		}

		public String  getDisplayName()                   { return displayName; }
		public int     getMaxDurationMinutes()             { return maxDurationMinutes; }
		public int     getMaxParticipants()               { return maxParticipants; }
		public boolean canBeVirtual()                     { return canBeVirtual; }

		public String getRulesSummary() {
			String base = String.format("<html>Max duration: %d min \n Max participants: %d \n Can be Virtual: %s<html>", maxDurationMinutes, maxParticipants, canBeVirtual ? "Yes" : "No<html>");
			if (this == URGENT) base += " <html>\n Must be within 1 week from today<html>";
			return base;
		}

		@Override public String toString() { return displayName; }
	}


	public appointment(String appointmentId, property property, time appointmentTime) {
		this(appointmentId, property, appointmentTime, AppointmentType.IN_PERSON);
	}

	public appointment(String appointmentId, property property,
	                   time appointmentTime, AppointmentType type) {
		this.appointmentId   = appointmentId;
		this.bookedBy        = new ArrayList<>();
		this.property        = property;
		this.appointmentTime = appointmentTime;
		this.type            = type;
		this.status          = AppointmentStatus.AVAILABLE;
	}


	public int getEffectiveMaxParticipants() {
		return Math.min(getType().getMaxParticipants(), property.getMaxViewingCapacity());
	}

	public boolean addBooking(user u) {
		for (user existing : bookedBy)
			if (existing.getId().equals(u.getId())) return false;

		if (bookedBy.size() >= getEffectiveMaxParticipants()) return false;

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

	public AppointmentType getType() {
		return (type == null) ? AppointmentType.IN_PERSON : type;
	}

	public void setType(AppointmentType type) {
		this.type = type;
	}

	public String validateDuration(int minutes) {
		int max = getType().getMaxDurationMinutes();
		if (minutes < 1)   return "Duration must be at least 1 minute.";
		if (minutes > max) return "Duration for " + getType().getDisplayName()
		                           + " appointments cannot exceed " + max + " minutes.";
		return null;
	}

	public String validateBookingAllowed() {
		int effectiveMax = getEffectiveMaxParticipants();
		if (bookedBy.size() >= effectiveMax)
			return getType().getDisplayName() + " appointments allow at most "
			       + effectiveMax + " participant(s). This appointment is full.";
		return null;
	}


	public static String validateAppointmentTime(LocalDateTime apptDateTime, AppointmentType type) {
		LocalDateTime now = LocalDateTime.now();
		if (!apptDateTime.isAfter(now))
			return "Cannot add an appointment in the past. Please choose a future date and time.";
		if (type == AppointmentType.URGENT && apptDateTime.isAfter(now.plusWeeks(1)))
			return "Urgent appointments must be scheduled within 1 week from today.";
		return null;
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
		return "Appointment{id='" + appointmentId + "', type=" + getType().getDisplayName()
				+ ", property=" + property.getAddress()
				+ ", time=" + appointmentTime + ", status=" + status
				+ ", bookings=" + bookedBy.size() + "}";
	}
}