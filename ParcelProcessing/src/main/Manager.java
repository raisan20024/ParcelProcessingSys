package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class Manager {
    private QueueOfCustomers clientQueue;
    private ParcelMap parcelMap;
    private Worker worker;
    private Log log;
    private JFrame frame;
    private JTextArea parcelTextArea;
    private JTextArea customerTextArea;
    private JTextArea currentParcelTextArea;
    private JButton processButton;
    private JLabel statusBar;
    public Manager() {
        this.clientQueue = new QueueOfCustomers();
        this.parcelMap = new ParcelMap();
        this.worker = new Worker(parcelMap);
        this.log = Log.getInstance();
        createGUI();
    }
    public void initializeParcels(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String packageID = parts[0];
                    int daysInDepot = Integer.parseInt(parts[1]);
                    double weight = Double.parseDouble(parts[2]);
                    double length = Double.parseDouble(parts[3]);
                    double width = Double.parseDouble(parts[4]);
                    double height = Double.parseDouble(parts[5]);

                    Parcel parcel = new Parcel(packageID, daysInDepot, weight, length, width, height);
                    parcelMap.addParcel(parcel);
                }
            }
            updateParcelTextArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeCustomers(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int queueNumber = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String packageID = parts[2];

                    Customer customer = new Customer(queueNumber, name, packageID);
                    clientQueue.addCustomer(customer);
                }
            }
            updateCustomerTextArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startProcessing() {
        if (!clientQueue.isEmpty()) {
            Customer customer = clientQueue.removeCustomer();
            worker.processCustomer(customer);
            updateCustomerTextArea();
            updateParcelTextArea();
            currentParcelTextArea.setText("Currently Processing: \n" + customer.toString());
            updateStatusBar("Processing customer: " + customer.getName());
        } else {
            currentParcelTextArea.setText("No more customers to process.");
            updateStatusBar("No more customers to process.");
        }
        log.writeLogToFile("log.txt");
    }
    
    
    private void createGUI() {
        frame = new JFrame("Parcel Depot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 800);
        frame.setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(54, 57, 63)); 
        JLabel titleLabel = new JLabel("Parcel Depot System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(45, 45, 45));

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.WHITE);

        JMenuItem loadPackages = new JMenuItem("Load Parcels");
        loadPackages.setIcon(new ImageIcon("path_to_icon")); 
        loadPackages.addActionListener(e -> initializeParcels("parcels.txt"));

        JMenuItem loadClients = new JMenuItem("Load Clients");
        loadClients.setIcon(new ImageIcon("path_to_icon"));
        loadClients.addActionListener(e -> initializeCustomers("clients.txt"));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setIcon(new ImageIcon("path_to_icon"));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(loadPackages);
        fileMenu.add(loadClients);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.WHITE);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Parcel Depot System v1.0", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);

        parcelTextArea = new JTextArea();
        parcelTextArea.setEditable(false);
        parcelTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        parcelTextArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane packageScrollPane = new JScrollPane(parcelTextArea);
        parcelTextArea.setPreferredSize(new Dimension(150, 150));
        packageScrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));

        customerTextArea = new JTextArea();
        customerTextArea.setEditable(false);
        customerTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        customerTextArea.setMargin(new Insets(10, 10, 10, 10));
        parcelTextArea.setPreferredSize(new Dimension(150, 150));
        JScrollPane customerScrollPane = new JScrollPane(customerTextArea);
        customerScrollPane.setBorder(BorderFactory.createTitledBorder("Clients"));

        currentParcelTextArea = new JTextArea();
        currentParcelTextArea.setEditable(false);
        currentParcelTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        currentParcelTextArea.setMargin(new Insets(10, 10, 10, 10));
        parcelTextArea.setPreferredSize(new Dimension(150, 150));
        JScrollPane currentPackageScrollPane = new JScrollPane(currentParcelTextArea);
        currentPackageScrollPane.setBorder(BorderFactory.createTitledBorder("Current Parcel"));

        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, customerScrollPane, packageScrollPane);
        splitPane1.setDividerLocation(500);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, currentPackageScrollPane);
        splitPane2.setDividerLocation(900);

        processButton = new JButton("Process Next Client");
        processButton.setToolTipText("Click to process the next client in the queue");
        processButton.setFont(new Font("Arial", Font.BOLD, 16));
        processButton.setBackground(new Color(76, 175, 80));  // Green Color
        processButton.setForeground(Color.WHITE);
        processButton.setFocusPainted(false);
        processButton.addActionListener(e -> startProcessing());

        JPanel packageFormPanel = new JPanel(new GridBagLayout());
        packageFormPanel.setBorder(BorderFactory.createTitledBorder("Add Parcel"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 1, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField packageIDField = new JTextField(20);
        JTextField daysInDepotField = new JTextField(20);
        JTextField weightField = new JTextField(20);
        JTextField lengthField = new JTextField(20);
        JTextField widthField = new JTextField(20);
        JTextField heightField = new JTextField(20);

        JButton addPackageButton = new JButton("Add Parcel");
        addPackageButton.setFont(new Font("Arial", Font.BOLD, 14));
        addPackageButton.setBackground(new Color(76, 175, 80));
        addPackageButton.setForeground(Color.WHITE);
        addPackageButton.setFocusPainted(false);
        addPackageButton.addActionListener(e -> {
            try {
                String packageID = packageIDField.getText();
                int daysInDepot = Integer.parseInt(daysInDepotField.getText());
                double weight = Double.parseDouble(weightField.getText());
                double length = Double.parseDouble(lengthField.getText());
                double width = Double.parseDouble(widthField.getText());
                double height = Double.parseDouble(heightField.getText());

                Parcel parcel = new Parcel(packageID, daysInDepot, weight, length, width, height);
                parcelMap.addParcel(parcel);
                updateParcelTextArea();
                packageIDField.setText("");
                daysInDepotField.setText("");
                weightField.setText("");
                lengthField.setText("");
                widthField.setText("");
                heightField.setText("");
            } catch (NumberFormatException ex) {
                updateStatusBar("Error: Please enter valid numeric values.");
            }
        });

        addFieldToPanel(packageFormPanel, gbc, "Parcel ID:", packageIDField, 0);
        addFieldToPanel(packageFormPanel, gbc, "Days in Depot:", daysInDepotField, 1);
        addFieldToPanel(packageFormPanel, gbc, "Weight:", weightField, 2);
        addFieldToPanel(packageFormPanel, gbc, "Length:", lengthField, 3);
        addFieldToPanel(packageFormPanel, gbc, "Width:", widthField, 4);
        addFieldToPanel(packageFormPanel, gbc, "Height:", heightField, 5);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        packageFormPanel.add(addPackageButton, gbc);

        JPanel customerFormPanel = new JPanel(new GridBagLayout());
        customerFormPanel.setBorder(BorderFactory.createTitledBorder("Add Client"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 0, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        

        JTextField queueNumberField = new JTextField(20);
        JTextField customerNameField = new JTextField(20);
        JTextField packageIDForCustomerField = new JTextField(20);

        JButton addCustomerButton = new JButton("Add Client");
        addCustomerButton.setFont(new Font("Arial", Font.BOLD, 14));
        addCustomerButton.setBackground(new Color(76, 175, 80));
        addCustomerButton.setForeground(Color.WHITE);
        addCustomerButton.setFocusPainted(false);
        addCustomerButton.addActionListener(e -> {
            try {
                int queueNumber = Integer.parseInt(queueNumberField.getText());
                String name = customerNameField.getText();
                String packageID = packageIDForCustomerField.getText();

                Customer customer = new Customer(queueNumber, name, packageID);
                clientQueue.addCustomer(customer);
                updateCustomerTextArea();
                queueNumberField.setText("");
                customerNameField.setText("");
                packageIDForCustomerField.setText("");
            } catch (NumberFormatException ex) {
                updateStatusBar("Error: Please enter valid numeric values.");
            }
        });

        addFieldToPanel(customerFormPanel, gbc2, "Queue Number:", queueNumberField, 0);
        addFieldToPanel(customerFormPanel, gbc2, "Client Name:", customerNameField, 1);
        addFieldToPanel(customerFormPanel, gbc2, "Parcel ID for Client:", packageIDForCustomerField, 2);
        gbc2.gridx = 0;
        gbc2.gridy = 3;
        gbc2.gridwidth = 2;
        customerFormPanel.add(addCustomerButton, gbc2);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.add(processButton, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(packageFormPanel, BorderLayout.NORTH);
        mainPanel.add(customerFormPanel, BorderLayout.CENTER);
        mainPanel.add(splitPane2, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addFieldToPanel(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField, int gridY) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }





    private void updateParcelTextArea() {
        parcelTextArea.setText(parcelMap.toString());
    }

    private void updateCustomerTextArea() {
        customerTextArea.setText(clientQueue.toString());
    }

    private void updateStatusBar(String message) {
        statusBar.setText("Status: " + message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Manager());
    }
}