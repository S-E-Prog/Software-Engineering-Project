package domain;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class GUI implements Serializable {
   
    public static void main(String[] args) {

 InnerGUI gui = new InnerGUI();
gui.setVisible(true);
    }

}

class  InnerGUI extends JFrame implements Serializable {


    private JPanel mainPanel;
    private CardLayout cardLayout;
       user newUser ;
          ArrayList<user> customList= new ArrayList<>();
          
    
    

    public InnerGUI() {
       customList = mangfile.loadFromFile(mangfile.FileType.CUSTOMER);
        setTitle("Login System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        createLoginPanel();
        createRegisterPanel();
        createAdminPanel();
        add(mainPanel);
       
    }

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(241, 245, 249));

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

        JLabel messageLabel = new JLabel("");

        centerPanel.add(new JLabel("User Name:"));
        centerPanel.add(emailField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passField);
        centerPanel.add(messageLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(200, 255, 200));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        loginButton.setBackground(new Color(0, 150, 0));
        loginButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0, 120, 215));
        registerButton.setForeground(Color.WHITE);

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        loginPanel.add(title, BorderLayout.NORTH);
        loginPanel.add(centerPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);

   
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            if (email.equals("admin") && password.equals("1234")) {
                cardLayout.show(mainPanel, "admin");
                emailField.setText("");
                passField.setText("");
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Credentials");
            }
        });

        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        mainPanel.add(loginPanel, "login");
    }

    private void createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout());
        JLabel regTitle = new JLabel("Register", SwingConstants.CENTER);
        regTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        JPanel regCenterPanel = new JPanel();
        regCenterPanel.setLayout(new BoxLayout(regCenterPanel, BoxLayout.Y_AXIS));
        regCenterPanel.setBackground(new Color(200, 255, 200));
        regCenterPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JTextField nameField = new JTextField();
        JTextField regEmailField = new JTextField();
        JPasswordField regPassField = new JPasswordField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        regEmailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        regPassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JRadioButton sellerBtn = new JRadioButton("Seller");
        JRadioButton clientBtn = new JRadioButton("Client", true);
        sellerBtn.setBackground(new Color(200, 255, 200));
        clientBtn.setBackground(new Color(200, 255, 200));
        ButtonGroup group = new ButtonGroup();
        group.add(sellerBtn); group.add(clientBtn);

        regCenterPanel.add(new JLabel("Name:"));
        regCenterPanel.add(nameField);
        regCenterPanel.add(new JLabel("Email:"));
        regCenterPanel.add(regEmailField);
        regCenterPanel.add(new JLabel("Password:"));
        regCenterPanel.add(regPassField);
        regCenterPanel.add(sellerBtn);
        regCenterPanel.add(clientBtn);

        JButton regSubmitButton = new JButton("Submit");
        regSubmitButton.setBackground(new Color(0, 150, 0));
        regSubmitButton.setForeground(Color.WHITE);
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(215, 10, 10));
        backButton.setForeground(Color.WHITE);
        JPanel regButtonPanel = new JPanel(new FlowLayout());
        regButtonPanel.add(backButton);
        regButtonPanel.add(regSubmitButton);
        regButtonPanel.setBackground(new Color(200, 255, 200));
        registerPanel.add(regTitle, BorderLayout.NORTH);
        registerPanel.add(regCenterPanel, BorderLayout.CENTER);
        registerPanel.add(regButtonPanel, BorderLayout.SOUTH);

        regSubmitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = regEmailField.getText();
            String password = new String(regPassField.getPassword());
            boolean isSeller = sellerBtn.isSelected();
            user newUser = new user("1", name, email, password, isSeller);

            customList.add(newUser);
            mangfile.saveToFile(mangfile.FileType.CUSTOMER, customList);
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            cardLayout.show(mainPanel, "login");
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        mainPanel.add(registerPanel, "register");
    }

    private void createAdminPanel() {
        JPanel adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBackground(Color.lightGray);
        JLabel welcomeLabel = new JLabel("Welcome, Admin!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
       JPanel centerListPanel = new JPanel();
       JPanel userPanel = new JPanel();
  
 
    userPanel.removeAll();
    
  
    userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

  
    for (user u : customList) {
 
        JPanel singleUserItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        singleUserItem.setBorder(BorderFactory.createEtchedBorder()); // إضافة إطار بسيط
        
        JLabel userInfo = new JLabel(u.toString());
        
   
        if (u.isSeller()) {
            userInfo.setForeground(Color.BLUE);
        } else {
            userInfo.setForeground(Color.BLACK);
        }

        singleUserItem.add(userInfo);
        userPanel.add(singleUserItem);
    }
    
    userPanel.revalidate();
    userPanel.repaint();


        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel  appointmentPanel = new JPanel();

           tabbedPane.addTab("Users", userPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        
           adminPanel.add(tabbedPane, BorderLayout.CENTER);
          



        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(215, 10, 10));
        logoutBtn.setForeground(Color.WHITE);
        JPanel buttoPanel = new JPanel(new FlowLayout());
        buttoPanel.setBackground(new Color(150, 150, 255));
        buttoPanel.add(logoutBtn);
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        adminPanel.add(welcomeLabel, BorderLayout.NORTH);

        adminPanel.add(buttoPanel, BorderLayout.SOUTH);

        mainPanel.add(adminPanel, "admin");
    }



}