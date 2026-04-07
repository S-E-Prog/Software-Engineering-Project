package service;

import domain.appointment;
import domain.property;
import domain.time;
import domain.user;

import java.util.ArrayList;
import java.util.List;


/**
 *  Service layer for managing appointments.
 *  @author sabre
 */
public class AppointmentService {

    private List<appointment> appointments;
    private List<NotificationObserver> observers;

    public AppointmentService() {
        this.appointments = new ArrayList<>();
        this.observers = new ArrayList<>();

    }

    /**
     * book a new appointment.
     * @param id
     * @param user
     * @param property
     * @param time
     * @return appointment
     */
    public appointment bookAppointment(String id, user user, property property, time time) {
        for (appointment a : appointments) {
            if (a.getProperty().equals(property) &&
                    a.getAppointmentTime().equal(time) &&
                    a.getStatus() == appointment.AppointmentStatus.CONFIRMED) {
                System.out.println("Slot already booked!");
                return null;
            }
        }

        appointment appointment = new appointment(id, property, time);
        
        appointment.addBooking(user);
        
        appointments.add(appointment);
        notifyObservers(appointment.getBookedBy(),
                "Your appointment has been confirmed for " + time.toString());
        return appointment;
    }
    /**
     * Cancel an appointment by ID
     */
    public boolean cancelAppointment(String appointmentId) {
        for (appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId)) {
                if (a.getStatus() == appointment.AppointmentStatus.CANCELLED) {
                    System.out.println("Appointment already cancelled.");
                    return false;
                }
                a.cancel();
                notifyObservers(a.getBookedBy(), 
                    "Your appointment " + appointmentId + " has been cancelled.");
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }


    /**
     * @param appointmentId the id of the appointment to cancel.
     * @param admin the admin cancelling the appointment
     * @return true if appointment cancelled successfully.
     */
    public boolean adminCancelAppointment(String appointmentId, user admin) {
        for (appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId)) {

                if (a.getStatus() == appointment.AppointmentStatus.CANCELLED) {
                    System.out.println("Appointment already cancelled.");
                    return false;
                }

                a.cancel();
                notifyObservers(a.getBookedBy(),
                    "Your appointment " + appointmentId + " was cancelled by the administrator.");
                return true;
            }
        }

        System.out.println("Appointment not found.");
        return false;
    }

    /**
     * Check if a specific slot is available
     */
    public boolean isSlotAvailable(property property, time time) {

        for (appointment a : appointments) {
            if (a.getProperty().equals(property) &&
                    a.getAppointmentTime().equal(time) &&
                    a.getStatus() == appointment.AppointmentStatus.CONFIRMED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get available slots for a property
     */
    public List<time> getAvailableSlots(property property, List<time> allSlots) {
        List<time> availableSlots = new ArrayList<>();
        for (time slot : allSlots) {
            if (isSlotAvailable(property, slot)) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }

    /**
     * Modify appointment time
     */
    public boolean modifyAppointment(String appointmentId, time newTime) {
        for (appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId)) {
                if (a.getStatus() != appointment.AppointmentStatus.CONFIRMED) {
                    System.out.println("Only confirmed appointments can be modified.");
                    return false;
                }
                if (a.getAppointmentTime().isstart()) {
                    System.out.println("Cannot modify a past appointment.");
                    return false;
                }
                a.setAppointmentTime(newTime);
                notifyObservers(a.getBookedBy(),
                    "Your appointment has been modified. New time: " + newTime.toString());
                return true;
            }
        }
        System.out.println("Appointment not found.");
        return false;
    }

    /**

     */
    /**
     * @return appointment by time.
     */
    public List<appointment> getAllAppointments() {
        return appointments;
    }

    /**
     * Get appointments for a specific property
     */
    public List<appointment> getAppointmentsByProperty(property property) {
        List<appointment> result = new ArrayList<>();

        for (appointment a : appointments) {
            if (a.getProperty().equals(property)) {
                result.add(a);
            }
        }
        return result;
    }


    /**
     * Get appointments booked by a specific user
     */
    public List<appointment> getAppointmentsByUser(user user) {
        List<appointment> result = new ArrayList<>();

        for (appointment a : appointments) {
            if (a.getBookedBy().equals(user)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * @param observer
     */
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /**
     * @param observer
     */
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * @param arrayList
     * @param message
     */
    private void notifyObservers(ArrayList<user> users, String message) {
        for (NotificationObserver observer : observers) {
            observer.update(users, message);
        }
    }

    /**
     * @param appointmentId
     * Send reminders for users who booked an appointment.
     */
    public void sendReminder(String appointmentId) {
        for (appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId) &&
                    a.getStatus() == appointment.AppointmentStatus.CONFIRMED) {
                notifyObservers(a.getBookedBy(),
                    "Reminder: You have an appointment at " + a.getAppointmentTime().toString());
                return;
            }
        }
    }
}

