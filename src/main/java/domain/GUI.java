package domain;
import java.util.concurrent.*;
import java.util.*;
import java.time.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

public class GUI implements Serializable {
    public static void main(String[] args) {
         InnerGUI gui = new InnerGUI();
         gui.setVisible(true);
   
    }
}

class InnerGUI extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel;

    ArrayList<user>        customList       = new ArrayList<>();
    ArrayList<property>    propertiesList   = new ArrayList<>();
    ArrayList<appointment> appointmentsList = new ArrayList<>();


    // --- Login fields ---
    JTextField     emailField;
    JPasswordField passField;
    JLabel welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);

    // --- Register fields ---
    JTextField     nameField, emailRegField;
    JPasswordField passRegField;

    // --- Table models ---
    JTable             usersTable, propertiesTable, appointmentsTable;
    DefaultTableModel  usersModel, propertiesModel, appointmentsModel;

    JComboBox<String> statusFilter;
    JTextField        searchField;
    JTextField        propSearchField;
    JTextField        apptSearchField;

    // --- Current logged-in user ---
    user currentUser = null;


    // --- User panel table models ---
    DefaultTableModel availableApptModel;
    DefaultTableModel myApptModel;       
    DefaultTableModel myBookedModel;    
    DefaultTableModel myPropsModel;
    JTable            availableApptTable;
    JTable            myApptTable;
    JTable            myBookedTable;
    JTable            myPropsTable;

    // --- User panel search fields ---
    JTextField availableApptSearchField;
    JTextField myApptSearchField;
    JTextField myBookedSearchField;
    JTextField myPropSearchField;

    // --- Color palette ---
    final Color PRIMARY  = new Color(34,  139,  34);
    final Color BLUE     = new Color(70,  130, 180);
    final Color BG       = new Color(245, 247, 250);
    final Color PANEL_BG = new Color(220, 255, 220);
    final Color RED      = new Color(220,  53,  69);
    final Color GRAY_BTN = new Color(108, 117, 125);

    // ================================================================
    //  CONSTRUCTOR
    // ================================================================
    public InnerGUI() {
        loadData();

        setTitle("Property Management System");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        mainPanel.add(buildLoginPanel(),    "login");
        mainPanel.add(buildRegisterPanel(), "register");
        mainPanel.add(buildAdminPanel(),    "admin");
        mainPanel.add(buildUserPanel(),     "user");

        add(mainPanel);

        startReminderChecker();

    }

    /** Centralised data-load so we can call it from anywhere. */
    @SuppressWarnings("unchecked")
    private void loadData() {
        customList       = mangfile.loadFromFile(mangfile.FileType.CUSTOMER);
        propertiesList   = mangfile.loadFromFile(mangfile.FileType.PROPERTY);
        appointmentsList = mangfile.loadFromFile(mangfile.FileType.APPOINTMENT);


    }


    // ================================================================
    //  STYLING HELPERS
    // ================================================================

    /** Styles any AbstractButton (JButton). */
    private void style(JButton btn, Color c) {
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /* Styles JTextField and JPasswordField via their shared supertype. */
    private void styleField(JComponent f) {
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private JButton btn(String text, String cmd, Color c) {
        JButton b = new JButton(text);
        b.setActionCommand(cmd);
        b.addActionListener(this);
        style(b, c);
        return b;
    }

    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    // ================================================================
    //  LOGIN PANEL
    // ================================================================
    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Property Appointment System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(PRIMARY);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(PANEL_BG);
        form.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        emailField = new JTextField(20);
        passField  = new JPasswordField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passField .setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        styleField(emailField);
        styleField(passField);

        form.add(boldLabel("Username / Email:")); form.add(emailField);
        form.add(Box.createVerticalStrut(15));
        form.add(boldLabel("Password:"));         form.add(passField);
        form.add(Box.createVerticalStrut(20));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttons.setBackground(PANEL_BG);
        buttons.add(btn("Create Account", "GO_REGISTER", BLUE));
        buttons.add(btn("Login",          "LOGIN",       PRIMARY));

        panel.add(title,   BorderLayout.NORTH);
        panel.add(form,    BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // ================================================================
    //  REGISTER PANEL
    // ================================================================
    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(PRIMARY);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(PANEL_BG);
        form.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        nameField     = new JTextField();
        emailRegField = new JTextField();
        passRegField  = new JPasswordField();

       
        for (JComponent f : new JComponent[]{nameField, emailRegField, passRegField}) {
            f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            styleField(f);
        }

        form.add(new JLabel("Full Name:"));  form.add(nameField);     form.add(Box.createVerticalStrut(15));
        form.add(new JLabel("Email:"));      form.add(emailRegField); form.add(Box.createVerticalStrut(15));
        form.add(new JLabel("Password:"));   form.add(passRegField);  form.add(Box.createVerticalStrut(15));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttons.setBackground(PANEL_BG);
        buttons.add(btn("Back to Login", "BACK",   GRAY_BTN));
        buttons.add(btn("Register",      "SUBMIT",  PRIMARY));

        panel.add(title,   BorderLayout.NORTH);
        panel.add(form,    BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // ================================================================
    //  ADMIN PANEL
    // ================================================================
    private JPanel buildAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("Administrator Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        header.add(btn("Logout", "LOGOUT", RED), BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        dashboardPanel = buildDashboard();
        tabs.addTab("Dashboard",    dashboardPanel);
        tabs.addTab("Users",        buildUsersTab());
        tabs.addTab("Properties",   buildPropertiesTab());
        tabs.addTab("Appointments", buildAppointmentsTab());

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabs,   BorderLayout.CENTER);
        return panel;
    }

    // ================================================================
    //  DASHBOARD
    // ================================================================
    private JPanel buildDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton refreshBtn = new JButton("Refresh Dashboard");
        style(refreshBtn, BLUE);
        refreshBtn.addActionListener(e -> refreshDashboard());
        topBar.add(refreshBtn);

       
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setName("dashboardContent");
        updateDashboardContent(content);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setBackground(BG);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void updateDashboardContent(JPanel content) {
        content.removeAll();
        loadData();

        int completed = 0, cancelled = 0, confirmed = 0, available = 0;
        for (appointment a : appointmentsList) {
            if (a.getStatus() == appointment.AppointmentStatus.COMPLETED) completed++;
            if (a.getStatus() == appointment.AppointmentStatus.CANCELLED)  cancelled++;
              if (a.getStatus() == appointment.AppointmentStatus.CONFIRMED) confirmed++;
              if (a.getStatus() == appointment.AppointmentStatus.AVAILABLE) available++;
        }

        GridBagConstraints g = new GridBagConstraints();
        g.fill    = GridBagConstraints.BOTH;
        g.insets  = new Insets(10, 10, 10, 10);
        g.weightx = 0.33;

        // Row 0 
        g.gridy = 0;
        g.gridx = 0; content.add(statCard("Total Users",        String.valueOf(customList.size()),       new Color(52, 152, 219)), g);
        g.gridx = 1; content.add(statCard("Total Properties",   String.valueOf(propertiesList.size()),   new Color(46, 204, 113)), g);
        g.gridx = 2; content.add(statCard("Total Appointments", String.valueOf(appointmentsList.size()), new Color(155, 89, 182)), g);

        // Row 1 
        
         g.gridy = 1;
         g.gridx = 0; content.add(statCard("Available", String.valueOf(available), new Color(100, 250, 150)), g);
         g.gridx = 1; content.add(statCard("Confirmed", String.valueOf(confirmed), new Color(240, 204, 50)), g);
         g.gridx = 2; content.add(statCard("Completed", String.valueOf(completed), new Color(52, 152, 219)), g);
        
        // Row 2 

         g.gridy = 2;
         g.gridx = 1; content.add(statCard("Cancelled",  String.valueOf(cancelled),  RED), g);

        // Row 3
        g.gridy = 3; g.gridx = 0; g.gridwidth = 3; g.weighty = 0.5;
        content.add(buildRecentTable(), g);

        content.revalidate();
        content.repaint();
    }

   
    private void refreshDashboard() {
        if (dashboardPanel == null) return;
        checkAndUpdateAppointmentStatuses();
        Component center = ((BorderLayout) dashboardPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (!(center instanceof JScrollPane)) return;
        Component view = ((JScrollPane) center).getViewport().getView();
        if (view instanceof JPanel) {
            updateDashboardContent((JPanel) view);
            msg("Dashboard updated!");
        }
    }

    private JPanel statCard(String title, String value, Color color) {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel t = new JLabel(title);
        t.setForeground(Color.GRAY);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", Font.BOLD, 28));
        v.setForeground(color);
        c.add(t, BorderLayout.NORTH);
        c.add(v, BorderLayout.CENTER);
        return c;
    }

    private JPanel buildRecentTable() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel lbl = new JLabel("Recent Appointments");
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(lbl, BorderLayout.NORTH);

        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Property", "Owner", "Date","Available seats", "Status"}, 0);
        int count = 0;
        for (int i = appointmentsList.size() - 1; i >= 0 && count < 5; i--) {
            appointment a = appointmentsList.get(i);
            int slotsLeft = a.getProperty().getMaxViewingCapacity() - a.getBookingCount();
            m.addRow(new Object[]{
                a.getAppointmentId(),
                a.getProperty().getName(),
                a.getProperty().getOwner() != null ? a.getProperty().getOwner().getName() : "N/A",
                a.getAppointmentTimeString(),
                slotsLeft,
                a.getStatus()
            });
            count++;
        }
        JTable t = new JTable(m);
        t.setRowHeight(30);
        t.setEnabled(false);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    // ================================================================
    //  ADD USER DIALOG
    // ================================================================
    private void showAddUserDialog() {
        JDialog dlg = new JDialog(this, "Add New User", true);
        dlg.setSize(400, 240);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField     tfName  = new JTextField(18);
        JTextField     tfEmail = new JTextField(18);
        JPasswordField tfPass  = new JPasswordField(18);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Name:"),     g); g.gridx = 1; form.add(tfName,  g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Email:"),    g); g.gridx = 1; form.add(tfEmail, g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Password:"), g); g.gridx = 1; form.add(tfPass,  g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save   = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            String name  = tfName.getText().trim();
            String email = tfEmail.getText().trim();
            String pass  = new String(tfPass.getPassword());
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                msg("Please fill all fields!"); return;
            }
            if (pass.length() < 4) {
                msg("Password must be at least 4 characters!"); return;
            }
            for (user u : customList) {
                if (u.getName().equalsIgnoreCase(name)) {
                    msg("This name is already registered!"); return;
                }
                if (u.getEmail().equalsIgnoreCase(email)) {
                    msg("This email is already registered!"); return;
                }
            }
            String uid = "USR" + String.format("%03d", customList.size() + 1);
            customList.add(new user(uid, name, email, pass));
            mangfile.saveToFile(mangfile.FileType.CUSTOMER, customList);
            msg("User added successfully!");
            refreshDashboard();
            dlg.dispose();
        });
        cancel.addActionListener(e -> dlg.dispose());

        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

   // ================================================================
//  ADD APPOINTMENT DIALOG
// ================================================================
private void showAddAppointmentDialog() {
    loadData();
    if (propertiesList.isEmpty()) {
        msg("You need at least one property first."); return;
    }

    JDialog dlg = new JDialog(this, "Add New Appointment", true);
    dlg.setSize(455, 325);
    dlg.setLocationRelativeTo(this);
    dlg.setLayout(new BorderLayout());

    JPanel form = new JPanel(new GridBagLayout());
    form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    GridBagConstraints g = new GridBagConstraints();
    g.fill   = GridBagConstraints.HORIZONTAL;
    g.insets = new Insets(6, 6, 6, 6);

    String[] propNames = propertiesList.stream().map(property::getName).toArray(String[]::new);
    user[] owners = propertiesList.stream().map(property::getOwner).toArray(user[]::new);

    JComboBox<String> propCombo = new JComboBox<>(propNames);

    
    JLabel ownerLabel = new JLabel(owners[0] != null ? owners[0].getName() : "N/A");
    propCombo.addActionListener(ev -> {
        int idx = propCombo.getSelectedIndex();
        ownerLabel.setText(owners[idx] != null ? owners[idx].getName() : "N/A");
    });

    JTextField tfDate    = new JTextField(20);
    JTextField tfTime    = new JTextField(20);
    JTextField tfEndTime = new JTextField(20);
    tfDate.setToolTipText("Format: yyyy-MM-dd");
    tfTime.setToolTipText("Format: HH:mm");
    tfEndTime.setToolTipText("Format: mm");

    g.gridx = 0; g.gridy = 0; form.add(new JLabel("Property:"),                      g); g.gridx = 1; form.add(propCombo,  g);
    g.gridx = 0; g.gridy = 1; form.add(new JLabel("Owner:"),                          g); g.gridx = 1; form.add(ownerLabel, g);
    g.gridx = 0; g.gridy = 2; form.add(new JLabel("Date (yyyy-MM-dd):"),              g); g.gridx = 1; form.add(tfDate,     g);
    g.gridx = 0; g.gridy = 3; form.add(new JLabel("Time (HH:mm):"),                   g); g.gridx = 1; form.add(tfTime,     g);
    g.gridx = 0; g.gridy = 4; form.add(new JLabel("EndTime max 45min(mm):"), g); g.gridx = 1; form.add(tfEndTime,  g);

    JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JButton save   = new JButton("Save");
    JButton cancel = new JButton("Cancel");
    style(save, PRIMARY); style(cancel, GRAY_BTN);

    save.addActionListener(e -> {
        String dateStr    = tfDate.getText().trim();
        String timeStr    = tfTime.getText().trim();
        String endTimeStr = tfEndTime.getText().trim();
        if (dateStr.isEmpty() || timeStr.isEmpty() || endTimeStr.isEmpty()) {
            msg("Please fill all fields!"); return;
        }else if(Integer.parseInt(endTimeStr)>45){
            msg("End time must be 45 minutes or less!"); return;
        }
        

        String   appId   = "APT" + String.format("%03d", appointmentsList.size() + 1);
        property selProp = propertiesList.get(propCombo.getSelectedIndex());
        user     selUser = selProp.getOwner();  

        time t = new time();
        try {
            String[] dateParts = dateStr.split("-");
            String[] timeParts = timeStr.split(":");
            int year  = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day   = Integer.parseInt(dateParts[2]);
            int hour  = Integer.parseInt(timeParts[0]);
            int min   = Integer.parseInt(timeParts[1]);
            t.setdate(hour, min, day, month, year);
            t.setenddate(Integer.parseInt(endTimeStr));
        } catch (Exception ex) {
            msg("Invalid format. Use yyyy-MM-dd and HH:mm and mm"); return;
        }

        appointment newApp = new appointment(appId, selProp, t);
        appointmentsList.add(newApp);
        mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
        msg("Appointment added successfully!");
        refreshDashboard();
        dlg.dispose();
    });
    cancel.addActionListener(e -> dlg.dispose());

    btns.add(save); btns.add(cancel);
    dlg.add(form, BorderLayout.CENTER);
    dlg.add(btns, BorderLayout.SOUTH);
    dlg.setVisible(true);
}

    // ================================================================
    //  ADMIN — EDIT PROPERTY DIALOG
    // ================================================================
    private void showEditPropertyDialog() {
        int row = propertiesTable.getSelectedRow();
        if (row < 0) { msg("Please select a property first."); return; }
        String id = (String) propertiesModel.getValueAt(row, 0);
        property prop = propertiesList.stream().filter(p -> p.getPropertyId().equals(id)).findFirst().orElse(null);
        if (prop == null) { msg("Property not found!"); return; }

        JDialog dlg = new JDialog(this, "Edit Property", true);
        dlg.setSize(400, 370);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField tfName  = new JTextField(prop.getName(), 18);
        JTextField tfAddr  = new JTextField(prop.getAddress(), 18);
        JTextField tfPrice = new JTextField(String.valueOf(prop.getPrice()), 18);
        JTextField tfCap   = new JTextField(String.valueOf(prop.getMaxViewingCapacity()), 18);

        String[]    labels = {"Name:", "Address:", "Price:", "Capacity:"};
        JTextField[] fields = {tfName, tfAddr, tfPrice, tfCap};
        for (int i = 0; i < fields.length; i++) {
            g.gridx = 0; g.gridy = i; form.add(new JLabel(labels[i]), g);
            g.gridx = 1;              form.add(fields[i], g);
        }

        String[] ownerNames = customList.stream().map(user::getName).toArray(String[]::new);
        JComboBox<String> ownerCombo = new JComboBox<>(ownerNames);
        if (prop.getOwner() != null) {
            for (int i = 0; i < customList.size(); i++)
                if (customList.get(i).getId().equals(prop.getOwner().getId())) { ownerCombo.setSelectedIndex(i); break; }
        }
        g.gridx = 0; g.gridy = 4; form.add(new JLabel("Owner:"), g);
        g.gridx = 1;              form.add(ownerCombo, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty() || tfAddr.getText().trim().isEmpty()) {
                msg("Name and Address are required!"); return;
            }
            try {
                double price = Double.parseDouble(tfPrice.getText().trim());
                int    cap   = Integer.parseInt(tfCap.getText().trim());
                prop.setName(tfName.getText().trim());
                prop.setAddress(tfAddr.getText().trim());
                prop.setPrice(price);
                prop.setMaxViewingCapacity(cap);
                prop.setOwner(customList.get(ownerCombo.getSelectedIndex()));
                mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
                refreshProperties();
                msg("Property updated successfully!");
                dlg.dispose();
            } catch (NumberFormatException ex) {
                msg("Price must be a number and Capacity must be a whole number.");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  ADMIN — EDIT APPOINTMENT DIALOG
    // ================================================================
    private void showEditAppointmentDialog() {
        int row = appointmentsTable.getSelectedRow();
        if (row < 0) { msg("Please select an appointment first."); return; }
        String id = (String) appointmentsModel.getValueAt(row, 0);
        appointment appt = appointmentsList.stream().filter(a -> a.getAppointmentId().equals(id)).findFirst().orElse(null);
        if (appt == null) { msg("Appointment not found!"); return; }

        JDialog dlg = new JDialog(this, "Edit Appointment", true);
        dlg.setSize(455, 300);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        time curTime = appt.getAppointmentTime();
        String curDate = curTime.getdatetime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String curHM   = curTime.getdatetime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

        JTextField tfDate    = new JTextField(curDate, 20);
        JTextField tfTime    = new JTextField(curHM, 20);
        JTextField tfEndTime = new JTextField("30", 20);
        tfDate.setToolTipText("Format: yyyy-MM-dd");
        tfTime.setToolTipText("Format: HH:mm");
        tfEndTime.setToolTipText("Max 45 minutes");

        String[] statusNames = {"AVAILABLE", "CONFIRMED", "CANCELLED", "COMPLETED"};
        JComboBox<String> statusCombo = new JComboBox<>(statusNames);
        statusCombo.setSelectedItem(appt.getStatus().toString());

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Date (yyyy-MM-dd):"),      g); g.gridx = 1; form.add(tfDate,      g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Time (HH:mm):"),           g); g.gridx = 1; form.add(tfTime,      g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Duration max 45min(mm):"), g); g.gridx = 1; form.add(tfEndTime,   g);
        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Status:"),                 g); g.gridx = 1; form.add(statusCombo, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            String dateStr = tfDate.getText().trim();
            String timeStr = tfTime.getText().trim();
            String endStr  = tfEndTime.getText().trim();
            if (dateStr.isEmpty() || timeStr.isEmpty() || endStr.isEmpty()) {
                msg("Please fill all fields!"); return;
            }
            try {
                int endMin = Integer.parseInt(endStr);
                if (endMin > 45) { msg("Duration must be 45 minutes or less!"); return; }
                String[] dp = dateStr.split("-"); String[] tp = timeStr.split(":");
                time t = new time();
                t.setdate(Integer.parseInt(tp[0]), Integer.parseInt(tp[1]),
                          Integer.parseInt(dp[2]), Integer.parseInt(dp[1]), Integer.parseInt(dp[0]));
                t.setenddate(endMin);
                appt.setAppointmentTime(t);
                String sel = (String) statusCombo.getSelectedItem();
                appt.setStatus(appointment.AppointmentStatus.valueOf(sel));
                mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
                refreshAppointments();
                msg("Appointment updated successfully!");
                dlg.dispose();
            } catch (Exception ex) {
                msg("Invalid format. Use yyyy-MM-dd and HH:mm");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  USER — EDIT MY PROPERTY DIALOG
    // ================================================================
    private void showUserEditPropertyDialog() {
        if (currentUser == null) return;
        int row = myPropsTable.getSelectedRow();
        if (row < 0) { msg("Please select a property first."); return; }
        String id = (String) myPropsModel.getValueAt(row, 0);
        property prop = propertiesList.stream()
            .filter(p -> p.getPropertyId().equals(id) && p.getOwner() != null && p.getOwner().getId().equals(currentUser.getId()))
            .findFirst().orElse(null);
        if (prop == null) { msg("Property not found or not yours!"); return; }

        JDialog dlg = new JDialog(this, "Edit My Property", true);
        dlg.setSize(400, 300);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField tfName  = new JTextField(prop.getName(), 18);
        JTextField tfAddr  = new JTextField(prop.getAddress(), 18);
        JTextField tfPrice = new JTextField(String.valueOf(prop.getPrice()), 18);
        JTextField tfCap   = new JTextField(String.valueOf(prop.getMaxViewingCapacity()), 18);

        String[] labels = {"Name:", "Address:", "Price:", "Capacity:"};
        JTextField[] fields = {tfName, tfAddr, tfPrice, tfCap};
        for (int i = 0; i < fields.length; i++) {
            g.gridx = 0; g.gridy = i; form.add(new JLabel(labels[i]), g);
            g.gridx = 1;              form.add(fields[i], g);
        }

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty() || tfAddr.getText().trim().isEmpty()) {
                msg("Name and Address are required!"); return;
            }
            try {
                double price = Double.parseDouble(tfPrice.getText().trim());
                int    cap   = Integer.parseInt(tfCap.getText().trim());
                prop.setName(tfName.getText().trim());
                prop.setAddress(tfAddr.getText().trim());
                prop.setPrice(price);
                prop.setMaxViewingCapacity(cap);
                mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
                refreshMyProps();
                msg("Property updated successfully!");
                dlg.dispose();
            } catch (NumberFormatException ex) {
                msg("Price must be a number and Capacity must be a whole number.");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  USER — EDIT MY APPOINTMENT DIALOG
    // ================================================================
    private void showUserEditAppointmentDialog() {
        if (currentUser == null) return;
        int row = myApptTable.getSelectedRow();
        if (row < 0) { msg("Please select an appointment first."); return; }
        String id = (String) myApptModel.getValueAt(row, 0);
        appointment appt = appointmentsList.stream()
            .filter(a -> a.getAppointmentId().equals(id)
                && a.getProperty().getOwner() != null
                && a.getProperty().getOwner().getId().equals(currentUser.getId()))
            .findFirst().orElse(null);
        if (appt == null) { msg("Appointment not found or not yours!"); return; }
        if (appt.getStatus() == appointment.AppointmentStatus.CANCELLED ||
            appt.getStatus() == appointment.AppointmentStatus.COMPLETED) {
            msg("Cannot edit a cancelled or completed appointment."); return;
        }

        JDialog dlg = new JDialog(this, "Edit My Appointment", true);
        dlg.setSize(420, 260);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        time curTime = appt.getAppointmentTime();
        String curDate = curTime.getdatetime().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String curHM   = curTime.getdatetime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

        JTextField tfDate    = new JTextField(curDate, 20);
        JTextField tfTime    = new JTextField(curHM, 20);
        JTextField tfEndTime = new JTextField("30", 20);
        tfDate.setToolTipText("Format: dd-MM-yyyy");
        tfTime.setToolTipText("Format: HH:mm");
        tfEndTime.setToolTipText("Max 45 minutes");

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Date (dd-MM-yyyy):"),      g); g.gridx = 1; form.add(tfDate,    g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Time (HH:mm):"),           g); g.gridx = 1; form.add(tfTime,    g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Duration max 45min(mm):"), g); g.gridx = 1; form.add(tfEndTime, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            String dateStr = tfDate.getText().trim();
            String timeStr = tfTime.getText().trim();
            String endStr  = tfEndTime.getText().trim();
            if (dateStr.isEmpty() || timeStr.isEmpty() || endStr.isEmpty()) {
                msg("Please fill all fields!"); return;
            }
            try {
                int endMin = Integer.parseInt(endStr);
                if (endMin > 45) { msg("Duration must be 45 minutes or less!"); return; }
                String[] dp = dateStr.split("-"); String[] tp = timeStr.split(":");
                time t = new time();
                t.setdate(Integer.parseInt(tp[0]), Integer.parseInt(tp[1]),
                          Integer.parseInt(dp[2]), Integer.parseInt(dp[1]), Integer.parseInt(dp[0]));
                t.setenddate(endMin);
                appt.setAppointmentTime(t);
                mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
                refreshMyAppts();
                refreshAvailableAppts();
                msg("Appointment updated successfully!");
                dlg.dispose();
            } catch (Exception ex) {
                msg("Invalid format. Use dd-MM-yyyy and HH:mm");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  ADD PROPERTY DIALOG
    // ================================================================
    
    private void showAddPropertyDialog() {
        loadData();
                if (customList.isEmpty() ) {
            msg("You need at least one user first."); return;
        }
        JDialog dlg = new JDialog(this, "Add Property", true);
        dlg.setSize(400, 370);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField tfName  = new JTextField(18);
        JTextField tfAddr  = new JTextField(18);
        JTextField tfPrice = new JTextField(18);
        JTextField tfCap   = new JTextField(18);

        String[]    labels = {"Name:", "Address:", "Price:", "Capacity:"};
        JTextField[] fields = {tfName, tfAddr, tfPrice, tfCap};

        for (int i = 0; i < fields.length; i++) {
            g.gridx = 0; g.gridy = i; form.add(new JLabel(labels[i]), g);
            g.gridx = 1;              form.add(fields[i], g);
        }

   
        String[] ownerNames = customList.stream().map(user::getName).toArray(String[]::new);
        JComboBox<String> ownerCombo = new JComboBox<>(ownerNames);
        g.gridx = 0; g.gridy = fields.length; form.add(new JLabel("Owner:"), g);
        g.gridx = 1;                           form.add(ownerCombo, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save   = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty() || tfAddr.getText().trim().isEmpty()) {
                msg("Name and Address are required!"); return;
            }
            if (customList.isEmpty()) {
                msg("You need at least one user to assign as owner!"); return;
            }
            try {
                double price   = Double.parseDouble(tfPrice.getText().trim());
                int    cap     = Integer.parseInt(tfCap.getText().trim());
                user   owner   = customList.get(ownerCombo.getSelectedIndex());
                String newId   = "P" + String.format("%03d", propertiesList.size() + 1);
                propertiesList.add(new property(
                    newId,
                    tfName.getText().trim(),
                    tfAddr.getText().trim(),              
                    price, cap, owner
                ));
                mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
                refreshProperties();
                msg("Property added!");
                dlg.dispose();
            } catch (NumberFormatException ex) {
                msg("Price must be a number and Capacity must be a whole number.");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());

        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  USERS TAB
    // ================================================================
    private JPanel buildUsersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterUsers(searchField.getText()); }
        });
        top.add(searchField);

        JButton addUserBtn = new JButton("Add User");
        style(addUserBtn, PRIMARY);
        addUserBtn.addActionListener(e -> { showAddUserDialog(); refreshUsers(); });
        top.add(addUserBtn);

        JButton delBtn = new JButton("Delete Selected");
        style(delBtn, RED);
        delBtn.addActionListener(e -> deleteUser());
        top.add(delBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, BLUE);
        refBtn.addActionListener(e -> refreshUsers());
        top.add(refBtn);

        usersModel = new DefaultTableModel(new String[]{"ID", "Name", "Email"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
          usersTable = new JTable(usersModel);
        usersTable.setRowHeight(30);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshUsers();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        return panel;
    }

    private void refreshUsers() {
        usersModel.setRowCount(0);
        customList = mangfile.loadFromFile(mangfile.FileType.CUSTOMER);
        for (user u : customList)
            usersModel.addRow(new Object[]{
                u.getId(), u.getName(), u.getEmail()
            });
    }

    private void filterUsers(String kw) {
        usersModel.setRowCount(0);
        String lower = kw.toLowerCase();
        for (user u : customList)
            if (kw.isEmpty()
                    || u.getName().toLowerCase().contains(lower)
                    || u.getEmail().toLowerCase().contains(lower))
                usersModel.addRow(new Object[]{
                    u.getId(), u.getName(), u.getEmail()
                });
    }

    private void deleteUser() {
        int row = usersTable.getSelectedRow();
        if (row < 0) { msg("Please select a user first."); return; }
        String id = (String) usersModel.getValueAt(row, 0);
        if (confirm("Delete this user?")) {
            customList.removeIf(u -> u.getId().equals(id));
            mangfile.saveToFile(mangfile.FileType.CUSTOMER, customList);
            refreshUsers();
            msg("User deleted.");
        }
    }

    // ================================================================
    //  PROPERTIES TAB
    // ================================================================
    private JPanel buildPropertiesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        propSearchField = new JTextField(20);
        propSearchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterProperties(propSearchField.getText()); }
        });
        top.add(propSearchField);

        JButton addBtn = new JButton("Add Property");
        style(addBtn, PRIMARY);
        addBtn.addActionListener(e -> showAddPropertyDialog());
        top.add(addBtn);

        JButton editPropBtn = new JButton("Edit Selected");
        style(editPropBtn, new Color(255, 140, 0));
        editPropBtn.addActionListener(e -> showEditPropertyDialog());
        top.add(editPropBtn);

        JButton delBtn = new JButton("Delete Selected");
        style(delBtn, RED);
        delBtn.addActionListener(e -> deleteProperty());
        top.add(delBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, BLUE);
        refBtn.addActionListener(e -> refreshProperties());
        top.add(refBtn);

        propertiesModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Address", "Price", "Capacity", "Owner"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        propertiesTable = new JTable(propertiesModel);
        propertiesTable.setRowHeight(30);
        propertiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshProperties();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(propertiesTable), BorderLayout.CENTER);
        return panel;
    }

    private void refreshProperties() {
        propertiesModel.setRowCount(0);
        propertiesList = mangfile.loadFromFile(mangfile.FileType.PROPERTY);
        for (property p : propertiesList)
            propertiesModel.addRow(new Object[]{
                p.getPropertyId(), p.getName(), p.getAddress(),
                "$" + String.format("%,.0f", p.getPrice()),
                p.getMaxViewingCapacity(),
                p.getOwner() != null ? p.getOwner().getName() : "N/A"
            });
    }

    private void filterProperties(String kw) {
        propertiesModel.setRowCount(0);
        String lower = kw.toLowerCase();
        for (property p : propertiesList)
            if (kw.isEmpty()
                    || p.getName().toLowerCase().contains(lower)
                    || p.getAddress().toLowerCase().contains(lower)
                    || (p.getOwner() != null && p.getOwner().getName().toLowerCase().contains(lower)))
                propertiesModel.addRow(new Object[]{
                    p.getPropertyId(), p.getName(), p.getAddress(),
                    "$" + String.format("%,.0f", p.getPrice()),
                    p.getMaxViewingCapacity(),
                    p.getOwner() != null ? p.getOwner().getName() : "N/A"
                });
    }

    private void deleteProperty() {
        int row = propertiesTable.getSelectedRow();
        if (row < 0) { msg("Please select a property first."); return; }
        String id = (String) propertiesModel.getValueAt(row, 0);
        if (confirm("Delete this property?")) {
            propertiesList.removeIf(p -> p.getPropertyId().equals(id));
            mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
            refreshProperties();
            msg("Property deleted.");
        }
    }

    // ================================================================
    //  APPOINTMENTS TAB
    // ================================================================
    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        apptSearchField = new JTextField(15);
        apptSearchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterAppointments(apptSearchField.getText()); }
        });
        
        top.add(apptSearchField);
        top.add(new JLabel("Filter:"));
        statusFilter = new JComboBox<>(new String[]{"ALL", "CONFIRMED", "CANCELLED", "COMPLETED"});
        statusFilter.addActionListener(e -> filterAppointments(apptSearchField.getText()));
        top.add(statusFilter);

        JButton addBtn = new JButton("Add Appointment");
        style(addBtn, PRIMARY);
        addBtn.addActionListener(e -> showAddAppointmentDialog());
        top.add(addBtn);

        JButton editApptBtn = new JButton("Edit Selected");
        style(editApptBtn, new Color(255, 140, 0));
        editApptBtn.addActionListener(e -> showEditAppointmentDialog());
        top.add(editApptBtn);

        JButton delBtn = new JButton("Delete Selected");
        style(delBtn, RED);
        delBtn.addActionListener(e -> deleteAppointment());
        top.add(delBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, BLUE);
        refBtn.addActionListener(e -> { refreshAppointments(); apptSearchField.setText(""); });
        top.add(refBtn);

        appointmentsModel = new DefaultTableModel(
            new String[]{"ID", "Bookings", "Property", "Date & Time", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        appointmentsTable = new JTable(appointmentsModel);
        appointmentsTable.setRowHeight(30);
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshAppointments();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
        return panel;
    }

    private void refreshAppointments() {
        appointmentsModel.setRowCount(0);
        appointmentsList = mangfile.loadFromFile(mangfile.FileType.APPOINTMENT);
        String filter = (String) statusFilter.getSelectedItem();
        for (appointment a : appointmentsList) {
            if (!"ALL".equals(filter) && !a.getStatus().toString().equals(filter)) continue;
            appointmentsModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getBookingCount() + "/" + a.getProperty().getMaxViewingCapacity() + " booked",
                a.getProperty().getName(),
                a.getAppointmentTimeString(),
                a.getStatus()
            });
        }
         checkAndUpdateAppointmentStatuses();
    }

    private void filterAppointments(String kw) {
        appointmentsModel.setRowCount(0);
        String lower  = kw.toLowerCase();
        String filter = (String) statusFilter.getSelectedItem();
        for (appointment a : appointmentsList) {
            if (!"ALL".equals(filter) && !a.getStatus().toString().equals(filter)) continue;
            if (!kw.isEmpty()
                    && !a.getProperty().getName().toLowerCase().contains(lower)
                    && !a.getAppointmentId().toLowerCase().contains(lower)) continue;
            appointmentsModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getBookingCount() + "/" + a.getProperty().getMaxViewingCapacity() + " booked",
                a.getProperty().getName(),
                a.getAppointmentTimeString(),
                a.getStatus()
            });
        }
    }

    private void deleteAppointment() {
        int row = appointmentsTable.getSelectedRow();
        if (row < 0) { msg("Please select an appointment first."); return; }
        String id = (String) appointmentsModel.getValueAt(row, 0);
        if (confirm("Delete this appointment?")) {
            appointmentsList.removeIf(a -> a.getAppointmentId().equals(id));
            mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
            refreshAppointments();
            msg("Appointment deleted.");
        }
    }

    // ================================================================
    //  USER PANEL
    // ================================================================
    private JPanel buildUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);

       
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        header.add(welcomeLabel, BorderLayout.CENTER);
        JButton logoutBtn = new JButton("Logout");
        style(logoutBtn, RED);
        logoutBtn.addActionListener(e -> {
            if (confirm("Logout?")) {
                currentUser = null;
                cardLayout.show(mainPanel, "login");
            }
        });
        header.add(logoutBtn, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Available Appointments", buildAvailableApptTab());
        tabs.addTab("My Booked Appointments", buildMybookedTab());
        tabs.addTab("My Appointments",        buildMyApptTab());
        tabs.addTab("My Properties",          buildMyPropsTab());

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabs,   BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAvailableApptTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        availableApptSearchField = new JTextField(15);
        availableApptSearchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterAvailableAppts(availableApptSearchField.getText()); }
        });
        top.add(availableApptSearchField);
        JButton bookBtn = new JButton("Book Selected");
        style(bookBtn, PRIMARY);
        bookBtn.addActionListener(e -> bookAppointment());
        top.add(bookBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, GRAY_BTN);
        refBtn.addActionListener(e -> { refreshAvailableAppts(); availableApptSearchField.setText(""); });
        top.add(refBtn);

        availableApptModel = new DefaultTableModel(
            new String[]{"ID", "Property", "Owner", "Date & Time", "Available seats"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        availableApptTable = new JTable(availableApptModel);
        availableApptTable.setRowHeight(30);
        availableApptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshAvailableAppts();

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(availableApptTable), BorderLayout.CENTER);
        return panel;
    }


    private JPanel buildMyApptTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        myApptSearchField = new JTextField(15);
        myApptSearchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterMyAppts(myApptSearchField.getText()); }
        });
        top.add(myApptSearchField);

        JButton addApptBtn = new JButton("Add Appointment on My Property");
        style(addApptBtn, BLUE);
        addApptBtn.addActionListener(e -> showUserAddAppointmentDialog());
        top.add(addApptBtn);

        JButton editMyApptBtn = new JButton("Edit Selected");
        style(editMyApptBtn, new Color(255, 140, 0));
        editMyApptBtn.addActionListener(e -> showUserEditAppointmentDialog());
        top.add(editMyApptBtn);
            
    JButton cancelBtn = new JButton("Cancel Appointment");
     style(cancelBtn, RED);

      cancelBtn.addActionListener(e -> cancelMyAppointment());

       top.add(cancelBtn);

        JButton delBtn = new JButton("Delete Selected");
        style(delBtn, RED);
        delBtn.addActionListener(e -> deleteMyAppointment());
        top.add(delBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, GRAY_BTN);
        refBtn.addActionListener(e -> { refreshMyAppts(); myApptSearchField.setText(""); });
        top.add(refBtn);

        myApptModel = new DefaultTableModel(
            new String[]{"ID", "Property", "Date & Time", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        myApptTable = new JTable(myApptModel);
        myApptTable.setRowHeight(30);
        myApptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(myApptTable), BorderLayout.CENTER);
        return panel;
    }


    private JPanel buildMyPropsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        myPropSearchField = new JTextField(20);
        myPropSearchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterMyProps(myPropSearchField.getText()); }
        });
        top.add(myPropSearchField);

        JButton addPropBtn = new JButton("Add Property");
        style(addPropBtn, PRIMARY);
        addPropBtn.addActionListener(e -> showUserAddPropertyDialog());
        top.add(addPropBtn);

        JButton editMyPropBtn = new JButton("Edit Selected");
        style(editMyPropBtn, new Color(255, 140, 0));
        editMyPropBtn.addActionListener(e -> showUserEditPropertyDialog());
        top.add(editMyPropBtn);

        JButton delBtn = new JButton("Delete Selected");
        style(delBtn, RED);
        delBtn.addActionListener(e -> deleteMyProperty());
        top.add(delBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, GRAY_BTN);
        refBtn.addActionListener(e -> { refreshMyProps(); myPropSearchField.setText(""); });
        top.add(refBtn);

        myPropsModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Address", "Price", "Capacity"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        myPropsTable = new JTable(myPropsModel);
        myPropsTable.setRowHeight(30);
        myPropsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(myPropsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMybookedTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search:"));
        myBookedSearchField = new JTextField(15);
        myBookedSearchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterMyBooked(myBookedSearchField.getText()); }
        });
        top.add(myBookedSearchField);

        JButton cancelBtn = new JButton("Cancel Booking");
        style(cancelBtn, RED);
        cancelBtn.addActionListener(e -> cancelMyBooking());
        top.add(cancelBtn);

        JButton refBtn = new JButton("Refresh");
        style(refBtn, GRAY_BTN);
        refBtn.addActionListener(e -> { refreshMyBooked(); myBookedSearchField.setText(""); });
        top.add(refBtn);

        myBookedModel = new DefaultTableModel(
            new String[]{"ID", "Property", "Date & Time", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        myBookedTable = new JTable(myBookedModel);
        myBookedTable.setRowHeight(30);
        myBookedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(myBookedTable), BorderLayout.CENTER);
        return panel;
    }
 
    // ================================================================
    //  USER PANEL — REFRESH METHODS
    // ================================================================
    private void refreshAvailableAppts() {
        availableApptModel.setRowCount(0);
        loadData();
        checkAndUpdateAppointmentStatuses();
        for (appointment a : appointmentsList) {
            if (a.getStatus() != appointment.AppointmentStatus.AVAILABLE) continue;
            if (currentUser != null && a.isBookedBy(currentUser)) continue;
            int slotsLeft = a.getProperty().getMaxViewingCapacity() - a.getBookingCount();
            availableApptModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getProperty().getName(),
                a.getProperty().getOwner() != null ? a.getProperty().getOwner().getName() : "N/A",
                a.getAppointmentTimeString(),
                slotsLeft
            });
        }
    }

    private void refreshMyAppts() {
        myApptModel.setRowCount(0);
        if (currentUser == null) return;
        loadData();
      checkAndUpdateAppointmentStatuses();
        for (appointment a : appointmentsList) {
            if (a.getProperty().getOwner() != null
                    && a.getProperty().getOwner().getId().equals(currentUser.getId())) {
                myApptModel.addRow(new Object[]{
                    a.getAppointmentId(),
                    a.getProperty().getName(),
                    a.getAppointmentTimeString(),
                    a.getStatus()
                });
            }
        }
    }

    private void refreshMyBooked() {
        myBookedModel.setRowCount(0);
        if (currentUser == null) return;
        loadData();
         checkAndUpdateAppointmentStatuses();
        for (appointment a : appointmentsList) {
            if (!a.isBookedBy(currentUser)) continue;
            myBookedModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getProperty().getName(),
                a.getAppointmentTimeString(),
                appointment.AppointmentStatus.CONFIRMED
            });
        }
    }

    private void refreshMyProps() {
        myPropsModel.setRowCount(0);
        if (currentUser == null) return;
        loadData();
        for (property p : propertiesList) {
            if (p.getOwner() != null && p.getOwner().getId().equals(currentUser.getId())) {
                myPropsModel.addRow(new Object[]{
                    p.getPropertyId(), p.getName(), p.getAddress(),
                    "$" + String.format("%,.0f", p.getPrice()),
                    p.getMaxViewingCapacity()
                });
            }
        }
    }

    private int countBookings(String apptId) {
        for (appointment a : appointmentsList)
            if (a.getAppointmentId().equals(apptId)) return a.getBookingCount();
        return 0;
    }

    // ================================================================
    //  USER PANEL — ACTIONS
    // ================================================================

    private void bookAppointment() {
        int row = availableApptTable.getSelectedRow();
        if (row < 0) { msg("Please select an appointment to book."); return; }
        String id = (String) availableApptModel.getValueAt(row, 0);

        for (appointment a : appointmentsList) {
            if (!a.getAppointmentId().equals(id)) continue;
            if (a.isBookedBy(currentUser)) {
                msg("You already booked this appointment!"); return;
            }
            if (a.getStatus() != appointment.AppointmentStatus.AVAILABLE) {
                msg("This appointment is no longer available."); return;
            }
            int cap      = a.getProperty().getMaxViewingCapacity();
            int bookings = a.getBookingCount();
            if (bookings >= cap) {
                msg("This appointment is fully booked!"); return;
            }
            if (confirm("Book this appointment?")) {
                a.addBooking(currentUser);
                if (a.getBookingCount() >= cap) a.confirm();
                mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
                msg("Appointment booked successfully!");
                refreshAvailableAppts();
                refreshMyBooked();
            }
            return;
        }
    }

    private void cancelMyBooking() {
        int row = myBookedTable.getSelectedRow();
        if (row < 0) { msg("Please select an appointment to cancel."); return; }
        String id = (String) myBookedModel.getValueAt(row, 0);

        for (appointment a : appointmentsList) {
            if (!a.getAppointmentId().equals(id)) continue;
            if (a.getStatus() == appointment.AppointmentStatus.COMPLETED
                    || a.getStatus() == appointment.AppointmentStatus.CANCELLED) {
                msg("Cannot cancel a completed or already cancelled appointment."); return;
            }
            if (confirm("Cancel this booking? It will become available again.")) {
                a.removeBooking(currentUser);
                if (a.getBookingCount() == 0) a.setStatus(appointment.AppointmentStatus.AVAILABLE);
                mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
                msg("Booking cancelled. Appointment is now available.");
                refreshMyBooked();
                refreshAvailableAppts();
            }
            return;
        }
    }

    private void checkAndUpdateAppointmentStatuses() {
        boolean changed = false;
        for (appointment a : appointmentsList) {
            if (a.getStatus() == appointment.AppointmentStatus.CANCELLED
                    || a.getStatus() == appointment.AppointmentStatus.COMPLETED) continue;

          
            if (a.getStatus() == appointment.AppointmentStatus.CONFIRMED && a.isExpired()) {
                a.complete();
                changed = true;
            }

              if (a.getStatus() == appointment.AppointmentStatus.AVAILABLE && a.isExpired() && a.getBookingCount() <= 0) {
                a.cancel();
                changed = true;
                continue;
            }
         
          else  if (a.getStatus() == appointment.AppointmentStatus.AVAILABLE && a.isExpired()) {
                a.complete();
                changed = true;
                continue;
            }

            int cap = a.getProperty().getMaxViewingCapacity();
            if (a.getStatus() == appointment.AppointmentStatus.AVAILABLE
                    && countBookings(a.getAppointmentId()) >= cap) {
                a.confirm();
                changed = true;
            }
        }
        if (changed) mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
    }

    private void startStatusChecker() {
        Thread checker = new Thread(() -> {
            while (currentUser != null) {
                try { Thread.sleep(60000); } catch (InterruptedException ex) { break; }
                SwingUtilities.invokeLater(() -> {
                    if (currentUser == null) return;
                    loadData();
                    checkAndUpdateAppointmentStatuses();
                    if (availableApptModel != null) refreshAvailableAppts();
                    if (myApptModel       != null) refreshMyAppts();
                    if (myBookedModel     != null) refreshMyBooked();
                });
            }
        });
        checker.setDaemon(true);
        checker.start();
    }

//=============================================================
//Sending alerts and messages to users↓↓↓↓↓↓↓↓↓
//=============================================================

private HashSet<String> notifiedAppointments = new HashSet<>();

private void startReminderChecker() {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    scheduler.scheduleAtFixedRate(() -> {

        LocalDateTime now = LocalDateTime.now();

        for (appointment appt : appointmentsList) {

            if (appt.getStatus() == appointment.AppointmentStatus.CANCELLED ||
                appt.getStatus() == appointment.AppointmentStatus.COMPLETED)
                continue;

            LocalDateTime apptTime = appt.getAppointmentTime().getdatetime();
            long minutes = Duration.between(now, apptTime).toMinutes();

            if (minutes >= 0 && minutes <= 5) {

               
                for (user u : appt.getBookedBy()) {

                    String notificationKey = appt.getAppointmentId() + "-" + u.getEmail();

                    if (notifiedAppointments.contains(notificationKey))
                        continue;

                    notifiedAppointments.add(notificationKey);

                    SwingUtilities.invokeLater(() -> {
                        Toolkit.getDefaultToolkit().beep();

                        JOptionPane.showMessageDialog(null, 
                            " Reminder!\nYour appointment for :"
                                + appt.getProperty().getName()
                                + "\nStarts in less than 5 minutes! :"
                                + appt.getAppointmentTime()
                                + "\nFor user: " + u.getEmail(),
                            "Appointment Reminder",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    });
                }
            }
        }

    }, 0, 30, TimeUnit.SECONDS);
}

//================================================================
//Sending alerts and messages to users↑↑↑↑↑↑↑↑↑
//================================================================

    // ================================================================
    //  USER PANEL — FILTER METHODS
    // ================================================================
    private void filterAvailableAppts(String kw) {
        availableApptModel.setRowCount(0);
        String lower = kw.toLowerCase();
        for (appointment a : appointmentsList) {
            if (a.getStatus() != appointment.AppointmentStatus.AVAILABLE) continue;
            if (currentUser != null && a.isBookedBy(currentUser)) continue;
            if (!kw.isEmpty()
                    && !a.getProperty().getName().toLowerCase().contains(lower)
                    && !a.getAppointmentId().toLowerCase().contains(lower)) continue;
            int slotsLeft = a.getProperty().getMaxViewingCapacity() - a.getBookingCount();
            availableApptModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getProperty().getName(),
                a.getProperty().getOwner() != null ? a.getProperty().getOwner().getName() : "N/A",
                a.getAppointmentTimeString(),
                slotsLeft
            });
        }
    }

    private void filterMyAppts(String kw) {
        myApptModel.setRowCount(0);
        if (currentUser == null) return;
        String lower = kw.toLowerCase();
        for (appointment a : appointmentsList) {
            if (a.getProperty().getOwner() == null
                    || !a.getProperty().getOwner().getId().equals(currentUser.getId())) continue;
            if (!kw.isEmpty()
                    && !a.getProperty().getName().toLowerCase().contains(lower)
                    && !a.getAppointmentId().toLowerCase().contains(lower)) continue;
            myApptModel.addRow(new Object[]{
                a.getAppointmentId(), a.getProperty().getName(), a.getAppointmentTimeString(), a.getStatus()
            });
        }
    }

    private void filterMyBooked(String kw) {
        myBookedModel.setRowCount(0);
        if (currentUser == null) return;
        String lower = kw.toLowerCase();
        for (appointment a : appointmentsList) {
            if (!a.isBookedBy(currentUser)) continue;
            if (!kw.isEmpty()
                    && !a.getProperty().getName().toLowerCase().contains(lower)
                    && !a.getAppointmentId().toLowerCase().contains(lower)) continue;
            myBookedModel.addRow(new Object[]{
                a.getAppointmentId(), a.getProperty().getName(), a.getAppointmentTimeString(), a.getStatus()
            });
        }
    }

    private void filterMyProps(String kw) {
        myPropsModel.setRowCount(0);
        if (currentUser == null) return;
        String lower = kw.toLowerCase();
        for (property p : propertiesList) {
            if (p.getOwner() == null || !p.getOwner().getId().equals(currentUser.getId())) continue;
            if (!kw.isEmpty()
                    && !p.getName().toLowerCase().contains(lower)
                    && !p.getAddress().toLowerCase().contains(lower)) continue;
            myPropsModel.addRow(new Object[]{
                p.getPropertyId(), p.getName(), p.getAddress(),
                "$" + String.format("%,.0f", p.getPrice()), p.getMaxViewingCapacity()
            });
        }
    }

   
private void cancelMyAppointment() {
    int row = myApptTable.getSelectedRow();

    if (row < 0) {
        msg("Please select an appointment first.");
        return;
    }

    String id = (String) myApptModel.getValueAt(row, 0);

    for (appointment a : appointmentsList) {
        if (a.getAppointmentId().equals(id)) {

            if (a.getStatus() == appointment.AppointmentStatus.CANCELLED) {
                msg("This appointment is already cancelled.");
                return;
            }

            if (a.getStatus() == appointment.AppointmentStatus.COMPLETED) {
                msg("You cannot cancel a completed appointment.");
                return;
            }

            a.setStatus(appointment.AppointmentStatus.CANCELLED);

            mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);

            msg("Appointment cancelled successfully!");
            refreshMyAppts();
            refreshAvailableAppts();   
            return;
        }
    }
}

    private void deleteMyAppointment() {
        int row = myApptTable.getSelectedRow();
        if (row < 0) { msg("Please select an appointment first."); return; }
        String id = (String) myApptModel.getValueAt(row, 0);

        for (appointment a : appointmentsList) {
            if (!a.getAppointmentId().equals(id)) continue;
            if (a.getStatus() == appointment.AppointmentStatus.CONFIRMED) {
                msg("Cannot delete a confirmed appointment. Cancel it first."); return;
            }
            break;
        }
        if (confirm("Delete this appointment?")) {
            appointmentsList.removeIf(a -> a.getAppointmentId().equals(id));
            mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
            refreshMyAppts();
            refreshAvailableAppts();
            msg("Appointment deleted.");
        }
    }

  
    private void deleteMyProperty() {
        int row = myPropsTable.getSelectedRow();
        if (row < 0) { msg("Please select a property first."); return; }
        String id = (String) myPropsModel.getValueAt(row, 0);
        if (confirm("Delete this property?")) {
            propertiesList.removeIf(p -> p.getPropertyId().equals(id));
            mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
            refreshMyProps();
            msg("Property deleted.");
        }
    }

    // ================================================================
    //  USER PANEL — ADD APPOINTMENT DIALOG
    // ================================================================
    private void showUserAddAppointmentDialog() {
        if (currentUser == null) return;
        loadData();

        ArrayList<property> myProps = new ArrayList<>();
        for (property p : propertiesList)
            if (p.getOwner() != null && p.getOwner().getId().equals(currentUser.getId()))
                myProps.add(p);

        if (myProps.isEmpty()) {
            msg("You don't own any properties. Add a property first!"); return;
        }

        JDialog dlg = new JDialog(this, "Add Appointment on My Property", true);
        dlg.setSize(450, 300);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        String[] propNames = myProps.stream().map(property::getName).toArray(String[]::new);
        JComboBox<String> propCombo = new JComboBox<>(propNames);
        JTextField tfDate    = new JTextField(18);
        JTextField tfTime    = new JTextField(18);
        JTextField tfEndTime = new JTextField(18);
        tfDate.setToolTipText("Format: yyyy-MM-dd");
        tfTime.setToolTipText("Format: HH:mm");
        tfEndTime.setToolTipText("Minutes (max 45)");

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Property:"),          g); g.gridx = 1; form.add(propCombo, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Date (yyyy-MM-dd):"), g); g.gridx = 1; form.add(tfDate,    g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Time (HH:mm):"),      g); g.gridx = 1; form.add(tfTime,    g);
        g.gridx = 0; g.gridy = 3; form.add(new JLabel("EndTime max 45min(mm):"), g); g.gridx = 1; form.add(tfEndTime, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            String dateStr = tfDate.getText().trim();
            String timeStr = tfTime.getText().trim();
            String endStr  = tfEndTime.getText().trim();
            if (dateStr.isEmpty() || timeStr.isEmpty() || endStr.isEmpty()) {
                msg("Please fill all fields!"); return;
            }else if(Integer.parseInt(endStr)>45){
                msg("End time must be 45 minutes or less!"); return;
            }
            try {
                property selProp = myProps.get(propCombo.getSelectedIndex());
                String appId = "APT" + String.format("%03d", appointmentsList.size() + 1);
                String[] dp = dateStr.split("-"); String[] tp = timeStr.split(":");
                time t = new time();
                t.setdate(Integer.parseInt(tp[0]), Integer.parseInt(tp[1]),
                          Integer.parseInt(dp[2]), Integer.parseInt(dp[1]), Integer.parseInt(dp[0]));
                t.setenddate(Integer.parseInt(endStr));
                appointment newApp = new appointment(appId, selProp, t);
                newApp.setStatus(appointment.AppointmentStatus.AVAILABLE);
                appointmentsList.add(newApp);
                mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appointmentsList);
                msg("Appointment added and is now available for booking!");
                refreshAvailableAppts();
                dlg.dispose();
            } catch (Exception ex) {
                msg("Invalid format. Use yyyy-MM-dd and HH:mm");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  USER PANEL — ADD PROPERTY DIALOG
    // ================================================================
    private void showUserAddPropertyDialog() {
        if (currentUser == null) return;

        JDialog dlg = new JDialog(this, "Add My Property", true);
        dlg.setSize(400, 320);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JTextField tfName  = new JTextField(18);
        JTextField tfAddr  = new JTextField(18);
        JTextField tfPrice = new JTextField(18);
        JTextField tfCap   = new JTextField(18);

        String[] labels = {"Name:", "Address:","Price:", "Capacity:"};
        JTextField[] fields = {tfName, tfAddr,tfPrice, tfCap};
        for (int i = 0; i < fields.length; i++) {
            g.gridx = 0; g.gridy = i; form.add(new JLabel(labels[i]), g);
            g.gridx = 1;              form.add(fields[i], g);
        }

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton save = new JButton("Save"); JButton cancel = new JButton("Cancel");
        style(save, PRIMARY); style(cancel, GRAY_BTN);

        save.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty() || tfAddr.getText().trim().isEmpty()) {
                msg("Name and Address are required!"); return;
            }
            try {
                double price = Double.parseDouble(tfPrice.getText().trim());
                int    cap   = Integer.parseInt(tfCap.getText().trim());
                String newId = "P" + String.format("%03d", propertiesList.size() + 1);
                propertiesList.add(new property(newId,
                    tfName.getText().trim(), tfAddr.getText().trim(), price, cap, currentUser));
                mangfile.saveToFile(mangfile.FileType.PROPERTY, propertiesList);
                msg("Property added successfully!");
                refreshMyProps();
                dlg.dispose();
            } catch (NumberFormatException ex) {
                msg("Price must be a number and Capacity must be a whole number.");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        btns.add(save); btns.add(cancel);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ================================================================
    //  UTILITY
    // ================================================================
    private void msg(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private boolean confirm(String text) {
        return JOptionPane.showConfirmDialog(this, text, "Confirm",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // ================================================================
    //  ACTION LISTENER
    // ================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {

            case "LOGIN":
                loadData();
                String email = emailField.getText().trim();
                String pass  = new String(passField.getPassword());
                if (email.isEmpty() || pass.isEmpty()) { msg("Fill all fields."); break; }

                if (email.equals("admin") && pass.equals("1234")) {
                    cardLayout.show(mainPanel, "admin");
                    emailField.setText("");
                    passField.setText("");         
                    break;
                }
                boolean found = false;
                for (user u : customList)
                    if ((u.getName().equalsIgnoreCase(email) || u.getEmail().equalsIgnoreCase(email)) && u.getPassword().equals(pass)) {
                        currentUser = u;
                        welcomeLabel.setText("Welcome, " + u.getName() + "!");
                        emailField.setText("");
                        passField.setText("");
                        refreshAvailableAppts();
                        refreshMyAppts();
                        refreshMyBooked();
                        refreshMyProps();
                        startStatusChecker();
                        cardLayout.show(mainPanel, "user");
                        found = true; break;
                    }
                if (!found) msg("Invalid credentials!");
                break;

            case "GO_REGISTER":
                cardLayout.show(mainPanel, "register");
                break;

            case "SUBMIT":
                String name      = nameField.getText().trim();
                String regEmail  = emailRegField.getText().trim();
                String regPass   = new String(passRegField.getPassword());
                if (name.isEmpty() || regEmail.isEmpty() || regPass.isEmpty()) {
                    msg("Fill all fields."); break;
                }
                if (regPass.length() < 4) {
                    msg("Password must be at least 4 characters!"); break;
                }
                
                loadData();
                for (user u : customList) {
                    if (u.getName().equalsIgnoreCase(name)) {
                        msg("This name is already registered!"); return;
                    }
                    if (u.getEmail().equalsIgnoreCase(regEmail)) {
                        msg("This email is already registered!"); return;
                    }
                }
                String uid = "USR" + String.format("%03d", customList.size() + 1);
                customList.add(new user(uid, name, regEmail, regPass));
                mangfile.saveToFile(mangfile.FileType.CUSTOMER, customList);
                msg("Registration successful!");
                nameField.setText("");
                emailRegField.setText("");
                passRegField.setText("");
                cardLayout.show(mainPanel, "login");
                break;

            case "BACK":
                cardLayout.show(mainPanel, "login");
                break;

            case "LOGOUT":
                if (confirm("Logout?")) cardLayout.show(mainPanel, "login");
                break;
        }
    }
}