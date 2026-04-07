package Medical;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DoctorAppointmentUI extends JFrame {
    private JFrame homePage;
    private Student student;

    private DoctorMinHeap scheduler = new DoctorMinHeap();
    private JTextArea outputArea = new JTextArea(10, 30);
    private JComboBox<String> deptBox;

    public DoctorAppointmentUI(JFrame homePage, Student student) {
        this.homePage = homePage;
        this.student = student;

        setTitle("Smart Doctor Appointment System");
        setSize(850, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 255, 240)); // Light green

        scheduler.addDoctor(new Doctor("Dr. Rahim", "Cardiology"));
        scheduler.addDoctor(new Doctor("Dr. Karim", "Neurology"));
        scheduler.addDoctor(new Doctor("Dr. Nabila", "Dermatology"));
        scheduler.addDoctor(new Doctor("Dr. Arman", "Orthopedics"));
        scheduler.addDoctor(new Doctor("Dr. Fatema", "Gastroenterology"));
        scheduler.addDoctor(new Doctor("Dr. Hasan", "Pediatrics"));
        scheduler.addDoctor(new Doctor("Dr. Laila", "Ophthalmology"));
        scheduler.addDoctor(new Doctor("Dr. Jamil", "ENT"));
        scheduler.addDoctor(new Doctor("Dr. Farid", "Urology"));
        scheduler.addDoctor(new Doctor("Dr. Zaman", "Medicine Specialist"));

        deptBox = new JComboBox<>(new String[]{
                "Cardiology", "Neurology", "Dermatology", "Orthopedics",
                "Gastroenterology", "Pediatrics", "Ophthalmology", "ENT", "Urology", "Medicine Specialist"
        });

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(new Color(200, 255, 210));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(label("Student ID: " + student.id));
        topPanel.add(label("Student Name: " + student.name + " (" + student.dept + ")"));
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        centerPanel.setBackground(new Color(220, 255, 230));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton addSymptomBtn = new JButton("1. Add by Symptoms");
        JButton emergencyBtn = new JButton("2. Emergency Case");
        JButton manualAddBtn = new JButton("3. Add Appointment (Manual)");
        JButton visitBtn = new JButton("4. Start Doctor Visits");
        JButton showBtn = new JButton("5. Show All Queues");
        JButton historyBtn = new JButton("6. View All Patient History");
        JButton checkMedicalHistoryBtn = new JButton("7. My Medical History");
        JButton checkPrescriptionBtn = new JButton("8. My Prescriptions");
        JButton backBtn = new JButton("⏪ Back to Home");

        customizeButton(addSymptomBtn);
        customizeButton(emergencyBtn);
        customizeButton(manualAddBtn);
        customizeButton(visitBtn);
        customizeButton(showBtn);
        customizeButton(historyBtn);
        customizeButton(checkMedicalHistoryBtn);
        customizeButton(checkPrescriptionBtn);
        customizeButton(backBtn);

        centerPanel.add(new JLabel("Select Department:"));
        centerPanel.add(deptBox);
        centerPanel.add(manualAddBtn);
        centerPanel.add(addSymptomBtn);
        centerPanel.add(emergencyBtn);
        centerPanel.add(visitBtn);
        centerPanel.add(showBtn);
        centerPanel.add(historyBtn);
        centerPanel.add(checkMedicalHistoryBtn);
        centerPanel.add(checkPrescriptionBtn);
        centerPanel.add(new JLabel(""));
        centerPanel.add(backBtn);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Output"));
        add(centerPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        addSymptomBtn.addActionListener(e -> {
            String symptom = JOptionPane.showInputDialog(this, "Describe your symptom:");
            if (symptom == null || symptom.trim().isEmpty()) return;

            String dept = suggestDepartment(symptom.toLowerCase());
            int severity = getSeverityFromSymptoms(symptom.toLowerCase());

            Doctor assigned = scheduler.addPatientToDepartmentDoctorAndReturn(
                    student.id + " (" + student.dept + ")", severity, dept);
            outputArea.append("[✔] " + student.name + " → " + assigned.name + " (" + dept + ", Severity: " + severity + ")\n\n");
        });

        emergencyBtn.addActionListener(e -> {
            String[] emergencySymptoms = {
                    "Severe Chest Pain", "Unconsciousness", "Severe Fracture",
                    "Breathing Difficulty", "Severe Head Injury"
            };
            String selectedSymptom = (String) JOptionPane.showInputDialog(
                    this, "Select Emergency Symptom:",
                    "Emergency", JOptionPane.QUESTION_MESSAGE,
                    null, emergencySymptoms, emergencySymptoms[0]);

            if (selectedSymptom == null) return;

            String dept = suggestDepartment(selectedSymptom.toLowerCase());
            Doctor assigned = scheduler.addPatientToDepartmentDoctorAndReturn(
                    "🚨 " + student.id + " (" + student.dept + ")", 10, dept);
            outputArea.append("[🚨 Emergency] " + student.name + " → " + assigned.name + " (" + dept + ")\n\n");
        });

        manualAddBtn.addActionListener(e -> {
            String dept = (String) deptBox.getSelectedItem();
            Doctor assigned = scheduler.addPatientToDepartmentDoctorAndReturn(
                    student.id + " (" + student.dept + ")", 5, dept);
            outputArea.append("[➕] " + student.name + " → " + assigned.name + " (" + dept + ", Severity: 5)\n\n");
        });

        visitBtn.addActionListener(e -> {
            outputArea.append("\n--- Doctor Visit Started ---\n");
            scheduler.roundRobinVisit(outputArea);
        });

        showBtn.addActionListener(e -> {
            outputArea.setText("--- Doctor Queues ---\n");
            scheduler.printDoctorQueues(outputArea);
        });

        historyBtn.addActionListener(e -> {
            outputArea.setText("--- All Patient History ---\n");
            scheduler.showPatientHistory(outputArea);
        });

        checkMedicalHistoryBtn.addActionListener(e -> {
            Set<String> visited = scheduler.getDoctorsVisitedByPatient(student.id);
            outputArea.setText("--- Medical History for " + student.name + " ---\n");
            if (visited.isEmpty()) {
                outputArea.append("No records found.\n");
            } else {
                for (String doc : visited) outputArea.append("Visited: " + doc + "\n");
            }
        });

        checkPrescriptionBtn.addActionListener(e -> {
            outputArea.setText("--- Prescriptions for " + student.name + " ---\n");
            outputArea.append("Medicine 1: Paracetamol 500mg\n");
            outputArea.append("Medicine 2: Omeprazole 20mg\n");
            outputArea.append("Note: Drink plenty of fluids. Take rest.\n");
        });

        backBtn.addActionListener(e -> {
            homePage.setVisible(true);
            this.dispose();
        });
    }
    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(0, 100, 0));
        return lbl;
    }

    private void customizeButton(JButton b) {
        b.setBackground(new Color(0, 153, 76));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    private int getSeverityFromSymptoms(String symptoms) {
        Map<String, Integer> map = Map.of(
                "severe chest pain", 10,
                "unconsciousness", 10,
                "severe fracture", 10,
                "breathing difficulty", 10,
                "severe head injury", 10
        );
        return map.entrySet().stream()
                .filter(e -> symptoms.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElse(5);
    }

    private String suggestDepartment(String symptoms) {
        if (symptoms.contains("chest")) return "Cardiology";
        if (symptoms.contains("head")) return "Neurology";
        if (symptoms.contains("fracture")) return "Orthopedics";
        if (symptoms.contains("breathing")) return "Medicine Specialist";
        if (symptoms.contains("unconscious")) return "Neurology";
        return "Medicine Specialist";
    }

    static class Patient {
        String name;
        int severity;

        public Patient(String name, int severity) {
            this.name = name;
            this.severity = severity;
        }

        public String toString() {
            return name + " (Severity " + severity + ")";
        }
    }

    static class Doctor {
        String name, department;
        Queue<Patient> queue = new LinkedList<>();
        Set<String> seenPatients = new HashSet<>();

        public Doctor(String name, String department) {
            this.name = name;
            this.department = department;
        }

        public void addPatient(Patient p) {
            if (!seenPatients.contains(p.name)) {
                queue.add(p);
                seenPatients.add(p.name);
            }
        }

        public Patient seePatient() {
            return queue.poll();
        }

        public int getQueueSize() {
            return queue.size();
        }

        public Set<String> getSeenPatients() {
            return seenPatients;
        }

        public String toString() {
            return name + " (" + department + ") [Queue: " + queue.size() + "]";
        }
    }

    static class DoctorMinHeap {
        private List<Doctor> heap = new ArrayList<>();

        public void addDoctor(Doctor doc) {
            heap.add(doc);
            heapifyUp(heap.size() - 1);
        }

        public Doctor addPatientToDepartmentDoctorAndReturn(String name, int severity, String dept) {
            List<Doctor> deptDoctors = new ArrayList<>();
            for (Doctor d : heap) {
                if (d.department.equals(dept)) deptDoctors.add(d);
            }
            if (deptDoctors.isEmpty()) return null;
            Doctor target = Collections.min(deptDoctors, Comparator.comparingInt(Doctor::getQueueSize));
            target.addPatient(new Patient(name, severity));
            heapifyDown(heap.indexOf(target));
            return target;
        }

        public void printDoctorQueues(JTextArea area) {
            for (Doctor d : heap) {
                area.append(d + "\n");
                for (Patient p : d.queue) {
                    area.append("   - " + p + "\n");
                }
            }
        }

        public void roundRobinVisit(JTextArea area) {
            int maxRounds = heap.stream().mapToInt(Doctor::getQueueSize).max().orElse(0);
            for (int i = 0; i < maxRounds; i++) {
                for (Doctor d : heap) {
                    if (!d.queue.isEmpty()) {
                        Patient p = d.seePatient();
                        area.append(d.name + " sees " + p + "\n");
                    }
                }
            }
        }

        public void showPatientHistory(JTextArea area) {
            for (Doctor d : heap) {
                area.append(d.name + "'s Seen Patients: " + d.getSeenPatients() + "\n");
            }
        }

        public Set<String> getDoctorsVisitedByPatient(String idOrName) {
            Set<String> visited = new HashSet<>();
            for (Doctor d : heap) {
                for (String p : d.getSeenPatients()) {
                    if (p.contains(idOrName)) visited.add(d.name + " (" + d.department + ")");
                }
            }
            return visited;
        }

        private void heapifyUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i).getQueueSize() < heap.get(parent).getQueueSize()) {
                    Collections.swap(heap, i, parent);
                    i = parent;
                } else break;
            }
        }

        private void heapifyDown(int i) {
            int size = heap.size();
            while (i < size) {
                int left = 2 * i + 1, right = 2 * i + 2, smallest = i;
                if (left < size && heap.get(left).getQueueSize() < heap.get(smallest).getQueueSize())
                    smallest = left;
                if (right < size && heap.get(right).getQueueSize() < heap.get(smallest).getQueueSize())
                    smallest = right;
                if (smallest != i) {
                    Collections.swap(heap, i, smallest);
                    i = smallest;
                } else break;
            }
        }
    }
}
