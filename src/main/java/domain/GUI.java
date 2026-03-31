package domain;

import javax.swing.*;
import java.awt.*;

public class GUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        JPanel mainPanel = new JPanel(new CardLayout());
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(241, 245, 249));

        // --------------------login-----------------------------------
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(200, 255, 200));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();

        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(200, 255, 200));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.setBackground(new Color(0, 150, 0));
        loginButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0, 120, 215));
        registerButton.setForeground(Color.WHITE);

        JLabel messageLabel = new JLabel("");

        centerPanel.add(new JLabel("USER Name:"));
        centerPanel.add(emailField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passField);

        buttonPanel.setBackground(new Color(200, 255, 200));
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        centerPanel.add(messageLabel);
        loginPanel.add(title, BorderLayout.NORTH);
        loginPanel.add(centerPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);
        // ---------------------------register------------------------------------------------------
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(new Color(241, 245, 249));
        JLabel regTitle = new JLabel("Register", SwingConstants.CENTER);
        regTitle.setFont(new Font("Arial", Font.BOLD, 20));
        regTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        registerPanel.add(regTitle, BorderLayout.NORTH);
        JPanel regCenterPanel = new JPanel();
        regCenterPanel.setLayout(new BoxLayout(regCenterPanel, BoxLayout.Y_AXIS));
        regCenterPanel.setBackground(new Color(200, 255, 200));
        regCenterPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        JTextField nameField = new JTextField();
        JTextField regEmailField = new JTextField();
        JPasswordField regPassField = new JPasswordField();
        regEmailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        regPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JRadioButton SellerButton = new JRadioButton("Seller");
        JRadioButton ClientButton = new JRadioButton("Client");
        ButtonGroup group = new ButtonGroup();
        SellerButton.setBackground(new Color(200, 255, 200));
        ClientButton.setBackground(new Color(200, 255, 200));
        group.add(ClientButton);
        group.add(SellerButton);
        group.setSelected(ClientButton.getModel(), true);
        regCenterPanel.add(new JLabel("USER Name:"));
        regCenterPanel.add(nameField);
        regCenterPanel.add(new JLabel("Email:"));
        regCenterPanel.add(regEmailField);
        regCenterPanel.add(new JLabel("Password:"));
        regCenterPanel.add(regPassField);
        regCenterPanel.add(SellerButton);
        regCenterPanel.add(ClientButton);
        JButton regSubmitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        regSubmitButton.setBackground(new Color(0, 150, 0));
        regSubmitButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(150, 20, 0));
        backButton.setForeground(Color.WHITE);
        JPanel regButtonPanel = new JPanel();
        regButtonPanel.setBackground(new Color(200, 255, 200));
        regButtonPanel.add(backButton);
        regButtonPanel.add(regSubmitButton);
        registerPanel.add(regCenterPanel, BorderLayout.CENTER);
        registerPanel.add(regButtonPanel, BorderLayout.SOUTH);

        // ---------------------------------------UserUI-----------------------------------------------------
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(241, 245, 249));
        JLabel userTitle = new JLabel("Welcome  ", SwingConstants.CENTER);
        userTitle.setFont(new Font("Arial", Font.BOLD, 20));
        userTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userPanel.add(userTitle, BorderLayout.NORTH);
        JPanel userCenterPanel = new JPanel();

        // ----------------------------------------------------------------------------------------------
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(userPanel, "user");

        frame.add(mainPanel);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());

            if (email.equals("admin") && password.equals("1234")) {
                messageLabel.setForeground(new Color(0, 128, 0));
                messageLabel.setText("Login Successful");
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "user");
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Email or Password");
            }
        });
        registerButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (mainPanel.getLayout());
            cl.show(mainPanel, "register");
        });
        regSubmitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = regEmailField.getText();
            String password = new String(regPassField.getPassword());
            String role = SellerButton.isSelected() ? "Seller" : "Client";

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Registration Successful for " + role);
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                cl.show(mainPanel, "login");
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (mainPanel.getLayout());
            cl.show(mainPanel, "login");
        });

        frame.setVisible(true);
    }
}