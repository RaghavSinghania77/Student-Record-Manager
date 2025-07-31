package studentdetailform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class StudentDetailForm extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASS = "0000";

    private JPanel gridPanel;
    private GridBagConstraints gbc;
    private JCheckBox selectAllCheckbox;
    private Map<Integer, Integer> rowToStudentIdMap = new HashMap<>();

    public StudentDetailForm() {
        setTitle("Student Detail Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x1E2A38)); //dark blue-gray 

        JButton newButton = new JButton("New");
        newButton.setPreferredSize(new Dimension(100, 30));
        newButton.setBackground(new Color(0x007ACC)); // bright blue
        newButton.setForeground(Color.WHITE); // white 
        newButton.setFocusPainted(false);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(0x1E2A38)); // Dark blue-gray
        buttonPanel.add(newButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setBackground(Color.red); // Bright blue
        deleteButton.setForeground(Color.WHITE); // White
        deleteButton.setFocusPainted(false);
        buttonPanel.add(deleteButton); 

        JButton editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(100, 30));
        editButton.setBackground(new Color(0x00BFFF)); 
        editButton.setForeground(Color.WHITE); // White 
        editButton.setFocusPainted(false); 
        buttonPanel.add(editButton);

        add(buttonPanel, BorderLayout.NORTH);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Integer> rowsToDelete = new ArrayList<>();
                for (Component component : gridPanel.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            int rowIndex = getRowIndex(checkBox); 
                            int studentId = getStudentIdAtRow(rowIndex);
                            if (studentId != -1) {
                                rowsToDelete.add(studentId);
                            }
                        }
                    }
                }
                for (int studentId : rowsToDelete) {
                    deleteStudent(studentId);
                }
                refreshGrid();
            }
        });

        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(new Color(0xF0F4F8)); // Light gray-blue
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START; 
        gbc.weightx = 1.0; 
        gbc.weighty = 0.0; 

        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(0x007ACC))); // bright blue
        gridPanel.setPreferredSize(new Dimension(1535, 700));

        JScrollPane scrollPane = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0x1E2A38)); // Dark blue-gray
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        String[] columnNames = {"Select", "Student Name", "Address Line 1", "Address Line 2", "State", "City", "Gender", "E-mail", "Phone Number", "Is Active"};
        selectAllCheckbox = new JCheckBox();
        selectAllCheckbox.setBackground(new Color(0xF0F4F8)); // Light gray-blue
        selectAllCheckbox.setOpaque(true);
        selectAllCheckbox.setFocusPainted(false); 
        selectAllCheckbox.setBorder(BorderFactory.createLineBorder(new Color(0x007ACC))); // bright blue
        selectAllCheckbox.setForeground(Color.WHITE); // White 

        selectAllCheckbox.setSelected(true);
        selectAllCheckbox.setOpaque(false);

        selectAllCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean selected = selectAllCheckbox.isSelected();
                for (Component component : gridPanel.getComponents()) {
                    if (component instanceof JCheckBox && component != selectAllCheckbox) {
                        ((JCheckBox) component).setSelected(selected);
                    }
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gridPanel.add(selectAllCheckbox, gbc);

        for (int i = 1; i < columnNames.length; i++) {
            gbc.gridx = i;
            JLabel label = new JLabel(columnNames[i], SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(160, 40));
            label.setBackground(new Color(0x007ACC)); // Bright blue
            label.setForeground(Color.WHITE); // White 
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(new Color(0x003F6C))); // Darker blue
            gridPanel.add(label, gbc);
        }

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newStudentForm();
            }
        });

        addPrevious();

        setVisible(true);
    }

    private int getRowIndex(JCheckBox checkBox) {
        GridBagLayout layout = (GridBagLayout) gridPanel.getLayout();
        GridBagConstraints constraints = layout.getConstraints(checkBox);
        return constraints.gridy;
    }

    private void newStudentForm() {
        JDialog dialog = new JDialog(this, "New Student", true);
        dialog.setSize(500, 500);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(0xE0E6EF)); // Light blue
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel studentNameLabel = new JLabel("Student Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(studentNameLabel, gbc);

        JTextField studentNameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(studentNameField, gbc);

        JLabel addressLine1Label = new JLabel("Address Line 1:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(addressLine1Label, gbc);

        JTextField addressLine1Field = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(addressLine1Field, gbc);

        JLabel addressLine2Label = new JLabel("Address Line 2:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(addressLine2Label, gbc);

        JTextField addressLine2Field = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(addressLine2Field, gbc);

        JLabel stateLabel = new JLabel("State:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(stateLabel, gbc);

        JComboBox<String> stateField = new JComboBox<>(getStatesFromDatabase());
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(stateField, gbc);

        JLabel cityLabel = new JLabel("City:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(cityLabel, gbc);

        JComboBox<String> cityField = new JComboBox<>();
        stateField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedState = (String) stateField.getSelectedItem();
                cityField.setModel(new DefaultComboBoxModel<>(getCitiesFromDatabase(selectedState)));
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(cityField, gbc);

        JLabel genderLabel = new JLabel("Gender:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(genderLabel, gbc);

        JComboBox<String> genderField = new JComboBox<>(new String[]{"Female", "Male"});
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(genderField, gbc);

        JLabel emailLabel = new JLabel("E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(emailField, gbc);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(phoneNumberLabel, gbc);

        JTextField phoneNumberField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(phoneNumberField, gbc);

        JLabel isActiveLabel = new JLabel("Is Active:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(isActiveLabel, gbc);

        JComboBox<String> isActiveField = new JComboBox<>(new String[]{"No", "Yes"});
        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(isActiveField, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0x007ACC)); // Bright blue
        submitButton.setForeground(Color.WHITE); // White 
        submitButton.setFocusPainted(false); 
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = studentNameField.getText();
                String addressLine1 = addressLine1Field.getText();
                String addressLine2 = addressLine2Field.getText();
                String state = (String) stateField.getSelectedItem();
                String city = (String) cityField.getSelectedItem();
                int gender = genderField.getSelectedIndex();
                String email = emailField.getText();
                long phoneNumber = Long.parseLong(phoneNumberField.getText());
                int isActive = isActiveField.getSelectedIndex();

                addStudentToDatabase(studentName, addressLine1, addressLine2, state, city, gender, email, phoneNumber, isActive);

                gridPanel.removeAll();
                addHeaderRow();
                addPrevious();
                gridPanel.revalidate(); // Ensure layout is updated
                gridPanel.repaint();

                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void deleteStudent(int studentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM Student WHERE studentId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No student found with ID: " + studentId);
            } else {
                System.out.println("Deleted student with ID: " + studentId);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void addPrevious() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT s.studentId, s.studentName, s.AddressLine1, s.AddressLine2, st.StateName, c.CityName, s.Gender, s.email, s.phone_number, s.IsActive "
                    + "FROM Student s "
                    + "JOIN State st ON s.StateId = st.StateId "
                    + "JOIN City c ON s.CityId = c.CityId";
            ResultSet rs = stmt.executeQuery(sql);

            int rowCount = 1;
            while (rs.next()) {
                int studentId = rs.getInt("studentId");
                rowToStudentIdMap.put(rowCount, studentId); 

                gbc.gridy = rowCount;
                addCheckboxToGrid(rowCount, 0);
                addLabelToGrid(rs.getString("studentName"), rowCount, 1);
                addLabelToGrid(rs.getString("AddressLine1"), rowCount, 2);
                addLabelToGrid(rs.getString("AddressLine2"), rowCount, 3);
                addLabelToGrid(rs.getString("StateName"), rowCount, 4);
                addLabelToGrid(rs.getString("CityName"), rowCount, 5);
                addLabelToGrid(rs.getInt("Gender") == 1 ? "Male" : "Female", rowCount, 6);
                addLabelToGrid(rs.getString("email"), rowCount, 7);
                addLabelToGrid(String.valueOf(rs.getLong("phone_number")), rowCount, 8);
                addLabelToGrid(rs.getInt("IsActive") == 1 ? "Yes" : "No", rowCount, 9);
                rowCount++;
            }

            // Add dummy 
            gbc.gridx = 0;
            gbc.gridy = rowCount;
            gbc.weightx = 1;
            gbc.weighty = 1;
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            gridPanel.add(filler, gbc);

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private void addCheckboxToGrid(int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        JCheckBox checkbox = new JCheckBox();
        checkbox.setBackground(new Color(0xF0F4F8)); // Light gray-blue
        checkbox.setOpaque(true);

        checkbox.setPreferredSize(new Dimension(40, 40));
        gridPanel.add(checkbox, gbc);
    }

    private void addLabelToGrid(String text, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(160, 40));
        label.setBackground(new Color(0xE0E6EF)); // Light blue
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(new Color(0x007ACC))); // Bright blue
        gridPanel.add(label, gbc);
    }

    private String[] getStatesFromDatabase() {
        Connection conn = null;
        Statement stmt = null;
        java.util.List<String> states = new java.util.ArrayList<>();

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT StateName FROM State";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                states.add(rs.getString("StateName"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return states.toArray(new String[0]);
    }

    private String[] getCitiesFromDatabase(String state) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        java.util.List<String> cities = new java.util.ArrayList<>();

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT c.CityName FROM City c JOIN State s ON c.StateId = s.StateId WHERE s.StateName = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, state);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                cities.add(rs.getString("CityName"));
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return cities.toArray(new String[0]);
    }

    private void addHeaderRow() {
        String[] columnNames = {"Select", "Student Name", "Address Line 1", "Address Line 2", "State", "City", "Gender", "E-mail", "Phone Number", "Is Active"};
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gridPanel.add(selectAllCheckbox, gbc);

        for (int i = 1; i < columnNames.length; i++) {
            gbc.gridx = i;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.BOTH;
            JLabel label = new JLabel(columnNames[i], SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(160, 40)); 
            label.setBackground(new Color(0x007ACC)); // Bright blue
            label.setForeground(Color.WHITE); // White text
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(new Color(0x003F6C))); // Darker blue
            gridPanel.add(label, gbc);
        }
    }

    private void addStudentToDatabase(String studentName, String addressLine1, String addressLine2, String state, String city, int gender, String email, long phoneNumber, int isActive) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "INSERT INTO Student (studentName, AddressLine1, AddressLine2, StateId, CityId, Gender, email, phone_number, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentName);
            pstmt.setString(2, addressLine1);
            pstmt.setString(3, addressLine2);
            pstmt.setInt(4, getStateId(state, conn));
            pstmt.setInt(5, getCityId(city, state, conn));
            pstmt.setInt(6, gender);
            pstmt.setString(7, email);
            pstmt.setLong(8, phoneNumber);
            pstmt.setInt(9, isActive);

            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private int getStateId(String stateName, Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT StateId FROM State WHERE StateName = ?");
        pstmt.setString(1, stateName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("StateId");
        } else {
            pstmt = conn.prepareStatement("INSERT INTO State (StateName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, stateName);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve state ID.");
            }
        }
    }

    private int getCityId(String cityName, String stateName, Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT CityId FROM City WHERE CityName = ? AND StateId = (SELECT StateId FROM State WHERE StateName = ?)");
        pstmt.setString(1, cityName);
        pstmt.setString(2, stateName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("CityId");
        } else {
            pstmt = conn.prepareStatement("INSERT INTO City (CityName, StateId) VALUES (?, (SELECT StateId FROM State WHERE StateName = ?))", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, cityName);
            pstmt.setString(2, stateName);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve city ID.");
            }
        }
    }

    private void addStudentRow(ResultSet rs, int rowCount) throws SQLException {
        int studentId = rs.getInt("studentId"); 
        rowToStudentIdMap.put(rowCount, studentId); 

        gbc.gridy = rowCount;
        addCheckboxToGrid(rowCount, 0);
        addLabelToGrid(rs.getString("studentName"), rowCount, 1);
        addLabelToGrid(rs.getString("AddressLine1"), rowCount, 2);
        addLabelToGrid(rs.getString("AddressLine2"), rowCount, 3);
        addLabelToGrid(rs.getString("StateName"), rowCount, 4);
        addLabelToGrid(rs.getString("CityName"), rowCount, 5);
        addLabelToGrid(rs.getInt("Gender") == 1 ? "Male" : "Female", rowCount, 6);
        addLabelToGrid(rs.getString("email"), rowCount, 7);
        addLabelToGrid(String.valueOf(rs.getLong("phone_number")), rowCount, 8);
        addLabelToGrid(rs.getInt("IsActive") == 1 ? "Yes" : "No", rowCount, 9);
    }

    private int getStudentIdAtRow(int rowIndex) {
        return rowToStudentIdMap.getOrDefault(rowIndex, -1); // Return -1 if no ID 
    }

    private void refreshGrid() {
        gridPanel.removeAll();
        addHeaderRow(); 
        addPrevious(); 
        gridPanel.revalidate(); 
        gridPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentDetailForm();
            }
        });
    }
}
