package service;

import domain.appointment;
import domain.property;
import domain.time;
import domain.user;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    private List<appointment> appointments;

    // Constructor
    public AppointmentService() {
        this.appointments = new ArrayList<>();
    }

    /**
     * Book a new appointment
     */
    public appointment bookAppointment(String id, user user, property property, time time) {

        // Check if slot already booked
        for (appointment a : appointments) {
            if (a.getProperty().equals(property) &&
                    a.getAppointmentTime().equal(time) &&
                    a.getStatus() == appointment.AppointmentStatus.CONFIRMED) {

                System.out.println("Slot already booked!");
                return null;
            }
        }

        appointment appointment = new appointment(id, user, property, time);
        appointments.add(appointment);

        System.out.println("Appointment booked successfully.");
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
                System.out.println("Appointment cancelled successfully.");
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
     * Get all appointments
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
}
