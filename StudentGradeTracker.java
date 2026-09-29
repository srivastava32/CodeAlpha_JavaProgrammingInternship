import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Student {
    String name;
    String rollNo;
    double marks;

    Student(String name, String rollNo, double marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks = marks;
    }

    String getGrade() {
        if (marks >= 90) return "A";
        else if (marks >= 80) return "B";
        else if (marks >= 70) return "C";
        else if (marks >= 60) return "D";
        else return "F";
    }
}

public class StudentGradeTracker extends JFrame {

    ArrayList<Student> students = new ArrayList<>();

    JTextField nameField, rollField, marksField;
    DefaultTableModel model;
    JLabel resultLabel;

    StudentGradeTracker() {
        setTitle("Student Grade Tracker");
        setSize(800, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("STUDENT GRADE TRACKER",
                JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        nameField = new JTextField();
        rollField = new JTextField();
        marksField = new JTextField();

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Marks (0-100):"));
        inputPanel.add(marksField);

        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Selected");

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.WEST);

        model = new DefaultTableModel(
                new String[]{"Name", "Roll No", "Marks", "Grade"}, 0);

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        resultLabel = new JLabel("Enter student details.",
                JLabel.CENTER);

        JButton statsButton = new JButton("Calculate Statistics");

        bottomPanel.add(statsButton);
        bottomPanel.add(resultLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row != -1) {
                students.remove(row);
                model.removeRow(row);
                resultLabel.setText("Student deleted.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Select a student first.");
            }
        });

        statsButton.addActionListener(e -> calculateStats());

        setVisible(true);
    }

    void addStudent() {
        try {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();
            double marks = Double.parseDouble(marksField.getText());

            if (name.isEmpty() || roll.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter all details.");
                return;
            }

            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this,
                        "Marks must be between 0 and 100.");
                return;
            }

            for (Student s : students) {
                if (s.rollNo.equalsIgnoreCase(roll)) {
                    JOptionPane.showMessageDialog(this,
                            "Roll number already exists.");
                    return;
                }
            }

            Student s = new Student(name, roll, marks);
            students.add(s);

            model.addRow(new Object[]{
                    s.name, s.rollNo, s.marks, s.getGrade()
            });

            nameField.setText("");
            rollField.setText("");
            marksField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter valid numeric marks.");
        }
    }

    void calculateStats() {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No student records available.");
            return;
        }

        double total = 0;
        double highest = students.get(0).marks;
        double lowest = students.get(0).marks;

        for (Student s : students) {
            total += s.marks;
            highest = Math.max(highest, s.marks);
            lowest = Math.min(lowest, s.marks);
        }

        double average = total / students.size();

        resultLabel.setText(String.format(
                "Average: %.2f | Highest: %.2f | Lowest: %.2f",
                average, highest, lowest));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeTracker::new);
    }
}
