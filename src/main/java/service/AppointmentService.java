package service;

import domain.appointment;
import domain.mangfile;
import domain.property;
import domain.time;
import domain.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for managing appointments, properties, and users.
 * Contains ALL business logic — the GUI only calls this class.
 * @author sabre
 * @author yamen
 */
public class AppointmentService {

    // ================================================================
    //  STATE
    // ================================================================
    private ArrayList<user>        customList;
    private ArrayList<property>    propertiesList;
    private ArrayList<appointment> appointmentsList;

    private List<NotificationObserver> observers;

    // ================================================================
    //  CONSTRUCTOR
    // ================================================================
    public AppointmentService() {
        this.observers = new ArrayList<>();
        loadAll();
    }

    // ================================================================
    //  PERSISTENCE
    // ================================================================

    /** Load all data from disk into memory. */
    public void loadAll() {
        customList       = mangfile.loadFromFile(mangfile.FileType.CUSTOMER);
        propertiesList   = mangfile.loadFromFile(mangfile.FileType.PROPERTY);
        appointmentsList = mangfile.loadFromFile(mangfile.FileType.APPOINTMENT);
    }

    private void saveAppointments() {
        mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
    }

    private void saveProperties() {
        mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
    }

    private void saveUsers() {
        mangfile.saveToFile(mangfile.FileType.CUSTOMER, customList);
    }

    // ================================================================
    //  READ-ONLY GETTERS FOR GUI DISPLAY
    // ================================================================

    public ArrayList<user>        getAllUsers()        { return customList; }
    public ArrayList<property>    getAllProperties()   { return propertiesList; }
    public ArrayList<appointment> getAllAppointments() { return appointmentsList; }

    /** Appointments owned by a specific user (property owner). */
    public List<appointment> getAppointmentsByOwner(user owner) {
        List<appointment> result = new ArrayList<>();
        for (appointment a : appointmentsList)
            if (a.getProperty().getOwner() != null
                    && a.getProperty().getOwner().getId().equals(owner.getId()))
                result.add(a);
        return result;
    }

    /** Appointments the user has booked as a participant. */
    public List<appointment> getBookedByUser(user u) {
        List<appointment> result = new ArrayList<>();
        for (appointment a : appointmentsList)
            if (a.isBookedBy(u)) result.add(a);
        return result;
    }

    /** Properties owned by a specific user. */
    public List<property> getPropertiesByOwner(user owner) {
        List<property> result = new ArrayList<>();
        for (property p : propertiesList)
            if (p.getOwner() != null && p.getOwner().getId().equals(owner.getId()))
                result.add(p);
        return result;
    }

    /** Appointments that are AVAILABLE and not already booked by the given user. */
    public List<appointment> getAvailableAppointmentsFor(user currentUser) {
        List<appointment> result = new ArrayList<>();
        for (appointment a : appointmentsList) {
            if (a.getStatus() != appointment.AppointmentStatus.AVAILABLE) continue;
            if (currentUser != null && a.isBookedBy(currentUser)) continue;
            result.add(a);
        }
        return result;
    }

    // ================================================================
    //  USER MANAGEMENT
    // ================================================================

 
    public String registerUser(String name, String email, String password) {
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty())
            return "Fill all fields.";

        String emailError = user.validateEmail(email);
        if (emailError != null) return emailError;

        if (password.length() < 4) return "Password must be at least 4 characters!";

        loadAll();
        for (user u : customList) {
            if (u.getName().equalsIgnoreCase(name))  return "This name is already registered!";
            if (u.getEmail().equalsIgnoreCase(email)) return "This email is already registered!";
        }

        String uid = "USR" + String.format("%03d", customList.size() + 1);
        customList.add(new user(uid, name, email.trim(), password));
        saveUsers();
        return null; // success
    }

    /**
     * Attempt login. Returns the matched user, or null if credentials are wrong.
     * Pass emailOrName="admin" and password="1234" to get admin access
     * (caller should check for the special "ADMIN" sentinel).
     */
    public user login(String emailOrName, String password) {
        for (user u : customList)
            if ((u.getName().equalsIgnoreCase(emailOrName) ||
                 u.getEmail().equalsIgnoreCase(emailOrName))
                && u.getPassword().equals(password))
                return u;
        return null;
    }

    /**
     * Add a user directly (admin use). Returns error string or null on success.
     */
    public String addUser(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty())
            return "Please fill all fields!";
        String emailError = user.validateEmail(email);
        if (emailError != null) return emailError;
        if (password.length() < 4) return "Password must be at least 4 characters!";
        for (user u : customList) {
            if (u.getName().equalsIgnoreCase(name))   return "This name is already registered!";
            if (u.getEmail().equalsIgnoreCase(email)) return "This email is already registered!";
        }
        String uid = "USR" + String.format("%03d", customList.size() + 1);
        customList.add(new user(uid, name, email, password));
        saveUsers();
        return null;
    }

   
    public boolean deleteUser(String userId) {
        boolean removed = customList.removeIf(u -> u.getId().equals(userId));
        if (removed) saveUsers();
        return removed;
    }

    // ================================================================
    //  PROPERTY MANAGEMENT
    // ================================================================

   
    public String addProperty(String name, String address, double price, int capacity, user owner) {
        if (name == null || name.trim().isEmpty() || address == null || address.trim().isEmpty())
            return "Name and Address are required!";
        if (customList.isEmpty()) return "You need at least one user to assign as owner!";
        String newId = "P" + String.format("%03d", propertiesList.size() + 1);
        propertiesList.add(new property(newId, name.trim(), address.trim(), price, capacity, owner));
        saveProperties();
        return null;
    }

    /**
     * Edit an existing property. Returns error string or null on success.
     */
    public String editProperty(String propertyId, String name, String address,
                               double price, int capacity, user owner) {
        property prop = findProperty(propertyId);
        if (prop == null) return "Property not found!";
        if (name.trim().isEmpty() || address.trim().isEmpty()) return "Name and Address are required!";
        prop.setName(name.trim());
        prop.setAddress(address.trim());
        prop.setPrice(price);
        prop.setMaxViewingCapacity(capacity);
        prop.setOwner(owner);
        saveProperties();
        return null;
    }

    /**
     * Delete a property by ID. Returns false if not found.
     */
    public boolean deleteProperty(String propertyId) {
        boolean removed = propertiesList.removeIf(p -> p.getPropertyId().equals(propertyId));
        if (removed) saveProperties();
        return removed;
    }

    public property findProperty(String propertyId) {
        for (property p : propertiesList)
            if (p.getPropertyId().equals(propertyId)) return p;
        return null;
    }

    // ================================================================
    //  APPOINTMENT MANAGEMENT
    // ================================================================


    public String addAppointment(property prop, appointment.AppointmentType type,
                                 time t, int durationMinutes) {
        if (prop == null) return "Please select a property.";
        if (durationMinutes < 1 || durationMinutes > type.getMaxDurationMinutes())
            return "Duration for " + type.getDisplayName()
                   + " must be between 1 and " + type.getMaxDurationMinutes() + " minutes.";
        String timeError = appointment.validateAppointmentTime(t.getdatetime(), type);
        if (timeError != null) return timeError;

        String appId = "APT" + String.format("%03d", appointmentsList.size() + 1);
        appointment newApp = new appointment(appId, prop, t, type);
        newApp.setStatus(appointment.AppointmentStatus.AVAILABLE);
        appointmentsList.add(newApp);
        saveAppointments();
        return null;
    }

    /**
     * Admin edit of an existing appointment (time, type, status).
     * Returns error string or null on success.
     */
    public String adminEditAppointment(String appointmentId, appointment.AppointmentType type,
                                       time newTime, appointment.AppointmentStatus newStatus) {
        appointment appt = findAppointment(appointmentId);
        if (appt == null) return "Appointment not found!";
        // Only validate the new time if it actually changed (admin may just be changing status)
        boolean timeChanged = !appt.getAppointmentTime().equal(newTime);
        if (timeChanged) {
            String timeError = appointment.validateAppointmentTime(newTime.getdatetime(), type);
            if (timeError != null) return timeError;
        }
        appt.setAppointmentTime(newTime);
        appt.setType(type);
        appt.setStatus(newStatus);
        saveAppointments();
        return null;
    }

    /**
     * Owner edits their own appointment (time and type only, not status).
     * Returns error string or null on success.
     */
    public String ownerEditAppointment(String appointmentId, user owner,
                                       appointment.AppointmentType type, time newTime) {
        appointment appt = appointmentsList.stream()
            .filter(a -> a.getAppointmentId().equals(appointmentId)
                && a.getProperty().getOwner() != null
                && a.getProperty().getOwner().getId().equals(owner.getId()))
            .findFirst().orElse(null);
        if (appt == null) return "Appointment not found or not yours!";
        if (appt.getStatus() == appointment.AppointmentStatus.CANCELLED
                || appt.getStatus() == appointment.AppointmentStatus.COMPLETED)
            return "Cannot edit a cancelled or completed appointment.";
        String timeError = appointment.validateAppointmentTime(newTime.getdatetime(), type);
        if (timeError != null) return timeError;
        appt.setAppointmentTime(newTime);
        appt.setType(type);
        saveAppointments();
        return null;
    }

    /**
     * Delete an appointment by ID (admin). Returns false if not found.
     */
    public boolean deleteAppointment(String appointmentId) {
        boolean removed = appointmentsList.removeIf(a -> a.getAppointmentId().equals(appointmentId));
        if (removed) saveAppointments();
        return removed;
    }

    /**
     * Owner cancels one of their own appointments.
     * Returns error string or null on success.
     */
    public String ownerCancelAppointment(String appointmentId, user owner) {
        appointment appt = appointmentsList.stream()
            .filter(a -> a.getAppointmentId().equals(appointmentId)
                && a.getProperty().getOwner() != null
                && a.getProperty().getOwner().getId().equals(owner.getId()))
            .findFirst().orElse(null);
        if (appt == null) return "Appointment not found or not yours!";
        if (appt.getStatus() == appointment.AppointmentStatus.CANCELLED)
            return "This appointment is already cancelled.";
        if (appt.getStatus() == appointment.AppointmentStatus.COMPLETED)
            return "You cannot cancel a completed appointment.";
        appt.setStatus(appointment.AppointmentStatus.CANCELLED);
        saveAppointments();
        return null;
    }

    /**
     * Validate if owner can delete this appointment — does NOT delete.
     * Returns error string, or null if deletion is allowed.
     */
    public String validateOwnerDelete(String appointmentId, user owner) {
        appointment appt = appointmentsList.stream()
            .filter(a -> a.getAppointmentId().equals(appointmentId)
                && a.getProperty().getOwner() != null
                && a.getProperty().getOwner().getId().equals(owner.getId()))
            .findFirst().orElse(null);
        if (appt == null) return "Appointment not found or not yours!";
        if (appt.getStatus() == appointment.AppointmentStatus.CONFIRMED)
            return "Cannot delete a confirmed appointment. Cancel it first.";
        return null;
    }

    /**
     * Delete owner's appointment. Call validateOwnerDelete first,
     * then call this only after the user confirms.
     */
    public String ownerDeleteAppointment(String appointmentId, user owner) {
        appointment appt = appointmentsList.stream()
            .filter(a -> a.getAppointmentId().equals(appointmentId)
                && a.getProperty().getOwner() != null
                && a.getProperty().getOwner().getId().equals(owner.getId()))
            .findFirst().orElse(null);
        if (appt == null) return "Appointment not found or not yours!";
        if (appt.getStatus() == appointment.AppointmentStatus.CONFIRMED)
            return "Cannot delete a confirmed appointment. Cancel it first.";
        appointmentsList.remove(appt);
        saveAppointments();
        return null;
    }

    // ================================================================
    //  BOOKING MANAGEMENT
    // ================================================================

  
    public String bookAppointment(String appointmentId, user currentUser) {
        appointment appt = findAppointment(appointmentId);
        if (appt == null) return "Appointment not found.";
        if (appt.isBookedBy(currentUser)) return "You already booked this appointment!";
        if (appt.getStatus() != appointment.AppointmentStatus.AVAILABLE)
            return "This appointment is no longer available.";
        String bookingError = appt.validateBookingAllowed();
        if (bookingError != null) return bookingError;
        int cap = appt.getEffectiveMaxParticipants();
        if (appt.getBookingCount() >= cap) return "This appointment is fully booked!";

        appt.addBooking(currentUser);
        if (appt.getBookingCount() >= cap) appt.confirm();
        saveAppointments();
        notifyObservers(appt.getBookedBy(),
            "Your appointment has been confirmed for " + appt.getAppointmentTime());
        return null;
    }

    /**
     * Cancel a user's own booking (removes them from the booking list).
     * Returns error string or null on success.
     */
    public String cancelMyBooking(String appointmentId, user currentUser) {
        appointment appt = findAppointment(appointmentId);
        if (appt == null) return "Appointment not found.";
        if (appt.getStatus() == appointment.AppointmentStatus.COMPLETED
                || appt.getStatus() == appointment.AppointmentStatus.CANCELLED)
            return "Cannot cancel a completed or already cancelled appointment.";
        appt.removeBooking(currentUser);
        if (appt.getBookingCount() == 0)
            appt.setStatus(appointment.AppointmentStatus.AVAILABLE);
        saveAppointments();
        return null;
    }

    /**
     * Validate if user can remove this booking — does NOT remove.
     * Returns error string, or null if removal is allowed.
     */
    public String validateRemoveFromBooked(String appointmentId) {
        appointment appt = findAppointment(appointmentId);
        if (appt == null) return "Appointment not found.";
        if (appt.getStatus() == appointment.AppointmentStatus.AVAILABLE
                || appt.getStatus() == appointment.AppointmentStatus.CONFIRMED)
            return "The appointment cannot be deleted until it has been cancelled or completed.";
        return null;
    }

    /**
     * Remove a booked appointment from the user's list
     * (only allowed when CANCELLED or COMPLETED).
     * Returns error string or null on success.
     */
    public String removeFromMyBooked(String appointmentId, user currentUser) {
        appointment appt = findAppointment(appointmentId);
        if (appt == null) return "Appointment not found.";
        if (appt.getStatus() == appointment.AppointmentStatus.AVAILABLE
                || appt.getStatus() == appointment.AppointmentStatus.CONFIRMED)
            return "The appointment cannot be deleted until it has been cancelled or completed.";
        appt.removeBooking(currentUser);
        saveAppointments();
        return null;
    }

    // ================================================================
    //  STATUS AUTO-UPDATE
    // ================================================================


    public void checkAndUpdateAppointmentStatuses() {
        boolean changed = false;
        for (appointment a : appointmentsList) {
            if (a.getStatus() == appointment.AppointmentStatus.CANCELLED
                    || a.getStatus() == appointment.AppointmentStatus.COMPLETED) continue;

            if (a.getStatus() == appointment.AppointmentStatus.CONFIRMED && a.isExpired()) {
                a.complete(); changed = true; continue;
            }
            if (a.getStatus() == appointment.AppointmentStatus.AVAILABLE && a.isExpired()) {
                if (a.getBookingCount() <= 0) { a.cancel(); }
                else                          { a.complete(); }
                changed = true; continue;
            }
            if (a.getStatus() == appointment.AppointmentStatus.AVAILABLE
                    && a.getBookingCount() >= a.getEffectiveMaxParticipants()) {
                a.confirm(); changed = true;
            }
        }
        if (changed) saveAppointments();
    }

    // ================================================================
    //  DASHBOARD STATS
    // ================================================================

    public int countByStatus(appointment.AppointmentStatus status) {
        int c = 0;
        for (appointment a : appointmentsList)
            if (a.getStatus() == status) c++;
        return c;
    }

    // ================================================================
    //  FIND HELPERS
    // ================================================================

    public appointment findAppointment(String appointmentId) {
        for (appointment a : appointmentsList)
            if (a.getAppointmentId().equals(appointmentId)) return a;
        return null;
    }

    public user findUserById(String userId) {
        for (user u : customList)
            if (u.getId().equals(userId)) return u;
        return null;
    }

    // ================================================================
    //  OBSERVER / NOTIFICATION
    // ================================================================

    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(ArrayList<user> users, String message) {
        for (NotificationObserver obs : observers)
            obs.update(users, message);
    }

    /**
     * Send a manual reminder for a CONFIRMED appointment.
     */
    public void sendReminder(String appointmentId) {
        appointment a = findAppointment(appointmentId);
        if (a != null && a.getStatus() == appointment.AppointmentStatus.CONFIRMED)
            notifyObservers(a.getBookedBy(),
                "Reminder: You have an appointment at " + a.getAppointmentTime());
    }

    // ================================================================
    //  LEGACY-COMPATIBLE METHODS
    // ================================================================

    /** @deprecated Use {@link #addAppointment} instead. */
    public appointment bookAppointment(String id, user user, property property, time time) {
        for (appointment a : appointmentsList) {
            if (a.getProperty().getPropertyId().equals(property.getPropertyId()) &&
                    a.getAppointmentTime().equal(time) &&
                    (a.getStatus() == appointment.AppointmentStatus.CONFIRMED ||
                            a.getStatus() == appointment.AppointmentStatus.AVAILABLE)) {
                System.out.println("Slot already booked!");
                return null;
            }
        }
        appointment appt = new appointment(id, property, time);
        appt.addBooking(user);
        appointmentsList.add(appt);
        notifyObservers(appt.getBookedBy(),
            "Your appointment has been confirmed for " + time.toString());
        return appt;
    }

    /** @deprecated Use {@link #ownerCancelAppointment} instead. */
    public boolean cancelAppointment(String appointmentId) {
        for (appointment a : appointmentsList) {
            if (a.getAppointmentId().equals(appointmentId)) {
                if (a.getStatus() == appointment.AppointmentStatus.CANCELLED) return false;
                a.cancel();
                notifyObservers(a.getBookedBy(),
                    "Your appointment " + appointmentId + " has been cancelled.");
                saveAppointments();
                return true;
            }
        }
        return false;
    }

    /** @deprecated Use {@link #ownerCancelAppointment} instead. */
    public boolean adminCancelAppointment(String appointmentId, user admin) {
        return cancelAppointment(appointmentId);
    }

    public boolean isSlotAvailable(property property, time time) {
        for (appointment a : appointmentsList) {
            if (a.getProperty().getPropertyId().equals(property.getPropertyId()) &&
                    a.getAppointmentTime().equal(time) &&
                    (a.getStatus() == appointment.AppointmentStatus.CONFIRMED ||
                            a.getStatus() == appointment.AppointmentStatus.AVAILABLE))
                return false;
        }
        return true;
    }

    public List<time> getAvailableSlots(property property, List<time> allSlots) {
        List<time> result = new ArrayList<>();
        for (time slot : allSlots)
            if (isSlotAvailable(property, slot)) result.add(slot);
        return result;
    }

    public boolean modifyAppointment(String appointmentId, time newTime) {
        for (appointment a : appointmentsList) {
            if (a.getAppointmentId().equals(appointmentId)) {
                if (a.getStatus() == appointment.AppointmentStatus.CANCELLED ||
                        a.getStatus() == appointment.AppointmentStatus.COMPLETED) return false;
                if (a.getAppointmentTime().isstart()) return false;
                a.setAppointmentTime(newTime);
                notifyObservers(a.getBookedBy(),
                    "Your appointment has been modified. New time: " + newTime.toString());
                saveAppointments();
                return true;
            }
        }
        return false;
    }
}