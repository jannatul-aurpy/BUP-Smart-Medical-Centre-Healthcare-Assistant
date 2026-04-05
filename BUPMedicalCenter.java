/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bupmedicalcenter1;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;

public class BUPMedicalCenter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}

// ==================== GLOBAL TABLE ====================
class GlobalTable {
    public static StudentHashTable table = new StudentHashTable();
}

// ==================== STUDENT CLASS ====================
class Student {
    public String id, name, dept, hashedPassword;
    
    public Student(String id, String name, String dept, String password) {
        this.id = id; this.name = name; this.dept = dept;
        this.hashedPassword = hashPassword(password);
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}

// ==================== STUDENT HASH TABLE ====================
class StudentHashTable {
    private static final int SIZE = 100;
    private final LinkedList<Student>[] table = new LinkedList[SIZE];
    
    public StudentHashTable() {
        for (int i = 0; i < SIZE; i++) table[i] = new LinkedList<>();
    }
    
    private int hash(String key) {
        return Math.abs(key.hashCode()) % SIZE;
    }
    
    public void insert(Student s) {
        table[hash(s.id)].add(s);
    }
    
    public Student get(String id, String pw) {
        for (Student s : table[hash(id)]) {
            if (s.id.equals(id) && s.hashedPassword.equals(Student.hashPassword(pw)))
                return s;
        }
        return null;
    }
    
    public boolean exists(String id) {
        for (Student s : table[hash(id)]) {
            if (s.id.equals(id)) return true;
        }
        return false;
    }
}

// ==================== WELCOME SCREEN ====================
class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        setTitle("BUP Smart Medical Centre");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 32, 63));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome", SwingConstants.CENTER);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcome.setForeground(Color.WHITE);
        welcome.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel icon = new JLabel("🏥", SwingConstants.CENTER);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 64));

        JLabel title = new JLabel("BUP Smart Medical Centre & Healthcare Assistant", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        for (JButton button : new JButton[]{loginButton, registerButton}) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(0, 32, 63));
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(150, 45));
        }
        
        loginButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> {
            new RegistrationFrame().setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(registerButton);

        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(welcome);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(icon);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
    }
}

// ==================== LOGIN FRAME ====================
class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login - Smart Medical Centre");
        setSize(500, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Demo users
        GlobalTable.table.insert(new Student("23029","Misha","CSE","pass029"));
        GlobalTable.table.insert(new Student("23123","Shanjimom","EEE","abc456"));

        JPanel p = new JPanel(new GridLayout(7,1,10,10));
        p.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("← Back");

        p.add(new JLabel("Student ID:"));
        p.add(idField);
        p.add(new JLabel("Password:"));
        p.add(pwField);
        p.add(new JLabel());
        p.add(loginButton);
        p.add(backButton);
        add(p);

        loginButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String pw = new String(pwField.getPassword());
            Student st = GlobalTable.table.get(id, pw);
            if (st != null) {
                dispose();
                new MainMenuFrame(st).setVisible(true);
            } else {
                int opt = JOptionPane.showConfirmDialog(this,
                        "Invalid credentials. Do you want to register?",
                        "Login Failed",
                        JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    new RegistrationFrame().setVisible(true);
                    dispose();
                }
            }
        });

        backButton.addActionListener(e -> {
            new WelcomeScreen().setVisible(true);
            dispose();
        });
    }
}

// ==================== REGISTRATION FRAME ====================
class RegistrationFrame extends JFrame {
    public RegistrationFrame() {
        setTitle("Register - Smart Medical Centre");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(6,2,10,10));
        p.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("← Back");
        
        p.add(new JLabel("Student ID:")); p.add(idField);
        p.add(new JLabel("Name:")); p.add(nameField);
        p.add(new JLabel("Department:")); p.add(deptField);
        p.add(new JLabel("Password:")); p.add(pwField);
        p.add(registerButton); p.add(backButton);
        add(p);

        registerButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String pw = new String(pwField.getPassword());
            if (id.isBlank() || name.isBlank() || dept.isBlank() || pw.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            if (GlobalTable.table.exists(id)) {
                JOptionPane.showMessageDialog(this, "User ID already exists.");
            } else {
                GlobalTable.table.insert(new Student(id, name, dept, pw));
                JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        
        backButton.addActionListener(e -> {
            new WelcomeScreen().setVisible(true);
            dispose();
        });
    }
}

// ==================== MAIN MENU FRAME ====================
class MainMenuFrame extends JFrame {
    public MainMenuFrame(Student st) {
        setTitle("Main Menu — Welcome " + st.name);
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(3,1,10,10));
        p.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));

        JButton docBtn = new JButton("🩺 Doctor Appointment");
        JButton medBtn = new JButton("💊 Medicine System");
        JButton exitBtn = new JButton("Exit Application");

        p.add(docBtn);
        p.add(medBtn);
        p.add(exitBtn);
        add(p);

        docBtn.addActionListener(e -> {
            new SchedulerFrame(st).setVisible(true);
            dispose();
        });
        
        medBtn.addActionListener(e -> {
            new SmartMedicineSystem(st).setVisible(true);
            dispose();
        });
        
        exitBtn.addActionListener(e -> System.exit(0));
    }
}

// ==================== DOCTOR CLASS ====================
class Doctor {
    String name, department;
    Queue<Patient> queue = new LinkedList<>();
    Set<String> seen = new HashSet<>();
    
    Doctor(String name, String dept) { 
        this.name = name; 
        this.department = dept; 
    }
    
    void addPatient(Patient p) { 
        if(seen.add(p.name)) queue.add(p); 
    }
    
    Patient seePatient() { 
        return queue.poll(); 
    }
    
    int getQueueSize() { 
        return queue.size(); 
    }
    
    Set<String> getSeenPatients() { 
        return seen; 
    }
    
    public String toString() { 
        return name+" ("+department+") [Q="+getQueueSize()+"]"; 
    }
}

// ==================== PATIENT CLASS ====================
class Patient {
    String name; 
    int severity;
    
    Patient(String n, int s){ 
        name = n; 
        severity = s; 
    }
    
    public String toString(){ 
        return name+" (Severity "+severity+")"; 
    }
}

// ==================== DOCTOR MIN HEAP ====================
class DoctorMinHeap {
    List<Doctor> heap = new ArrayList<>();
    
    void addDoctor(Doctor d) { 
        heap.add(d); 
        heapifyUp(heap.size()-1); 
    }
    
    Doctor addPatientToDepartmentDoctorAndReturn(String name, int sev, String dept) {
        List<Doctor> cand = new ArrayList<>();
        for (Doctor d : heap) if (d.department.equals(dept)) cand.add(d);
        if (cand.isEmpty()) return null;
        Doctor t = Collections.min(cand, Comparator.comparingInt(Doctor::getQueueSize));
        t.addPatient(new Patient(name, sev));
        heapifyDown(heap.indexOf(t));
        return t;
    }
    
    void printDoctorQueues(JTextArea a) {
        for(Doctor d: heap){
            a.append(d+"\n");
            for(Patient p: d.queue)
                a.append("   - "+p+"\n");
        }
    }
    
    void roundRobinVisit(JTextArea a) {
        int max = heap.stream().mapToInt(Doctor::getQueueSize).max().orElse(0);
        for(int i=0; i<max; i++){
            for(Doctor d: heap){
                Patient p = d.seePatient();
                if (p != null) a.append(d.name+" sees "+p+"\n");
            }
        }
    }
    
    void showPatientHistory(JTextArea a) {
        for (Doctor d : heap) a.append(d.name + "'s Seen: " + d.getSeenPatients() + "\n");
    }
    
    Set<String> getDoctorsVisitedByPatient(String id) {
        Set<String> out = new HashSet<>();
        for (Doctor d: heap){
            for (String n: d.getSeenPatients()) if (n.contains(id)) out.add(d.name+" ("+d.department+")");
        }
        return out;
    }
    
    private void heapifyUp(int i) {
        while (i>0){
            int p=(i-1)/2;
            if (heap.get(i).getQueueSize()<heap.get(p).getQueueSize()) {
                Collections.swap(heap,i,p); i=p;
            } else break;
        }
    }
    
    private void heapifyDown(int i) {
        int n=heap.size();
        while(true){
            int l=2*i+1, r=2*i+2, smallest=i;
            if(l<n && heap.get(l).getQueueSize()<heap.get(smallest).getQueueSize()) smallest=l;
            if(r<n && heap.get(r).getQueueSize()<heap.get(smallest).getQueueSize()) smallest=r;
            if (smallest!=i){ Collections.swap(heap, i, smallest); i=smallest; }
            else break;
        }
    }
}

// ==================== SCHEDULER FRAME ====================
class SchedulerFrame extends JFrame {
    private DoctorMinHeap scheduler = new DoctorMinHeap();
    private JTextArea outputArea = new JTextArea(10,30);
    private JTextField idField = new JTextField();
    private JComboBox<String> deptBox;

    public SchedulerFrame(Student st) {
        setTitle("Doctor Appointment — " + st.name);
        setSize(800,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0,0,80));
        
        String[] depts = {
            "Cardiology","Neurology","Dermatology","Orthopedics",
            "Gastroenterology","Pediatrics","Ophthalmology","ENT","Urology","Medicine Specialist"
        };
        
        scheduler.addDoctor(new Doctor("Dr. Rahim","Cardiology"));
        scheduler.addDoctor(new Doctor("Dr. Karim","Neurology"));
        scheduler.addDoctor(new Doctor("Dr. Nabila","Dermatology"));
        scheduler.addDoctor(new Doctor("Dr. Arman","Orthopedics"));
        scheduler.addDoctor(new Doctor("Dr. Fatema","Gastroenterology"));
        scheduler.addDoctor(new Doctor("Dr. Hasan","Pediatrics"));
        scheduler.addDoctor(new Doctor("Dr. Laila","Ophthalmology"));
        scheduler.addDoctor(new Doctor("Dr. Jamil","ENT"));
        scheduler.addDoctor(new Doctor("Dr. Farid","Urology"));
        scheduler.addDoctor(new Doctor("Dr. Zaman","Medicine Specialist"));
        
        deptBox = new JComboBox<>(depts);

        JPanel topPanel = new JPanel(new GridLayout(10,2,6,6));
        topPanel.setBackground(new Color(0,0,80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton addSymptomBtn = new JButton("1. Add by Symptoms");
        JButton emergencyBtn = new JButton("2. Emergency Case");
        JButton historyBtn = new JButton("3. View History");
        JButton visitBtn = new JButton("4. Start Visit");
        JButton showBtn = new JButton("5. Show Queues");
        JButton manualAddBtn = new JButton("➕ Add Appointment");
        JButton medHistoryBtn = new JButton("6. View Medical History");
        JButton prescriptionBtn = new JButton("7. View Prescriptions");

        for (JButton b : Arrays.asList(addSymptomBtn, emergencyBtn, historyBtn, visitBtn, showBtn, manualAddBtn, medHistoryBtn, prescriptionBtn)) {
            b.setBackground(new Color(0,102,204));
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        topPanel.add(new JLabel("Student ID:"){ { setForeground(Color.WHITE); } });
        topPanel.add(idField);
        topPanel.add(new JLabel("Dept (manual):"){ { setForeground(Color.WHITE); } });
        topPanel.add(deptBox);
        topPanel.add(manualAddBtn);
        topPanel.add(addSymptomBtn);
        topPanel.add(emergencyBtn);
        topPanel.add(historyBtn);
        topPanel.add(visitBtn);
        topPanel.add(showBtn);
        topPanel.add(medHistoryBtn);
        topPanel.add(prescriptionBtn);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        JButton exitBtn = new JButton("← Exit to Menu");
        exitBtn.setBackground(Color.DARK_GRAY);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitBtn.addActionListener(e -> {
            new MainMenuFrame(st).setVisible(true);
            dispose();
        });
        
        JPanel southPanel = new JPanel();
        southPanel.setBackground(new Color(0,0,80));
        southPanel.add(exitBtn);
        add(southPanel, BorderLayout.SOUTH);
        
        addSymptomBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this,"Enter Student ID:");
            if (id == null || id.isBlank()) return;
            String udept = JOptionPane.showInputDialog(this,"Enter University Dept:");
            if (udept==null||udept.isBlank()) return;
            String symp = JOptionPane.showInputDialog(this,"Describe your symptom:");
            if (symp==null||symp.isBlank()) return;

            String hospitalDept = suggestDepartment(symp.toLowerCase());
            int severity = getSeverityFromSymptoms(symp.toLowerCase());
            String sname = id.trim()+" ("+udept.trim()+")";
            Doctor assigned = scheduler.addPatientToDepartmentDoctorAndReturn(sname, severity, hospitalDept);
            deptBox.setSelectedItem(hospitalDept);
            outputArea.append("[✔] "+sname+" → "+assigned.name+" ("+hospitalDept+", Severity: "+severity+")\n\n");
        });
        
        emergencyBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this,"Enter Student ID for Emergency:");
            if (id == null || id.isBlank()) return;
            String udept = JOptionPane.showInputDialog(this,"Enter University Dept:");
            if (udept==null||udept.isBlank()) return;

            String[] ems = { "Severe Chest Pain", "Unconsciousness", "Severe Fracture", "Breathing Difficulty", "Severe Head Injury" };
            String sel = (String) JOptionPane.showInputDialog(this,"Select Emergency Symptom:", "Emergency", JOptionPane.QUESTION_MESSAGE, null, ems, ems[0]);
            if (sel == null) return;

            String sname = "🚨 "+id.trim()+" ("+udept.trim()+")";
            String hospitalDept = suggestDepartment(sel.toLowerCase());
            Doctor assigned = scheduler.addPatientToDepartmentDoctorAndReturn(sname, 10, hospitalDept);
            deptBox.setSelectedItem(hospitalDept);
            outputArea.append("[🚨] Emergency: "+sname+" → "+assigned.name+" ("+hospitalDept+")\n\n");
        });

        manualAddBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isBlank()) {
                JOptionPane.showMessageDialog(this,"Enter Student ID in top field.");
                return;
            }
            String udept = JOptionPane.showInputDialog(this,"Enter University Dept:");
            if (udept==null||udept.isBlank()) return;

            String hospitalDept = (String)deptBox.getSelectedItem();
            String sname = id+" ("+udept+")";
            Doctor assigned = scheduler.addPatientToDepartmentDoctorAndReturn(sname, 5, hospitalDept);
            outputArea.append("[➕] "+sname+" → "+assigned.name+" ("+hospitalDept+", Severity: 5)\n\n");
        });

        visitBtn.addActionListener(e -> {
            outputArea.append("\n--- Round Robin Visits ---\n");
            scheduler.roundRobinVisit(outputArea);
        });

        showBtn.addActionListener(e -> {
            outputArea.setText("--- Doctor Queues ---\n");
            scheduler.printDoctorQueues(outputArea);
        });

        historyBtn.addActionListener(e -> {
            outputArea.setText("--- Overall Patient History ---\n");
            scheduler.showPatientHistory(outputArea);
        });

        medHistoryBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this,"Enter Student ID:");
            if (id == null || id.isBlank()) return;
            Set<String> visited = scheduler.getDoctorsVisitedByPatient(id.trim());
            outputArea.setText("--- Medical History for "+id+" ---\n");
            if (visited.isEmpty()) {
                outputArea.append("No records found.\n");
            } else {
                for (String d : visited) outputArea.append("Visited: " + d + "\n");
            }
        });

        prescriptionBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this,"Enter Student ID for prescriptions:");
            if (id == null || id.isBlank()) return;
            outputArea.setText("--- Prescription History for "+id+" ---\n");
            outputArea.append("Medicine 1: Paracetamol 500mg\nMedicine 2: Omeprazole 20mg\nNote: Drink water, take rest.\n");
        });
    }

    private int getSeverityFromSymptoms(String symptoms) {
        Map<String,Integer> map = Map.of(
            "severe chest pain",10,
            "unconsciousness",10,
            "severe fracture",10,
            "breathing difficulty",10,
            "severe head injury",10
        );
        return map.entrySet().stream()
                  .filter(e -> symptoms.contains(e.getKey()))
                  .map(Map.Entry::getValue)
                  .max(Integer::compareTo)
                  .orElse(5);
    }

    private String suggestDepartment(String symptoms) {
        if (symptoms.contains("chest")) return "Cardiology";
        if (symptoms.contains("head")) return "Neurology";
        if (symptoms.contains("fracture")) return "Orthopedics";
        if (symptoms.contains("breathing")) return "Medicine Specialist";
        if (symptoms.contains("unconscious")) return "Neurology";
        return "Medicine Specialist";
    }
}

// ==================== SMART MEDICINE SYSTEM ====================
class SmartMedicineSystem extends JFrame {
    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd;
        String medicineName;
        int stock;
    }
    
    static class Medicine {
        String name;
        int quantity, expiryDays, cost, demandScore;
        Medicine(String n, int q, int e, int c, int d){
            name = n; quantity = q; expiryDays = e; cost = c; demandScore = d;
        }
        public String toString(){
            return name+" | Qty:"+quantity+" | Exp:"+expiryDays+"d | Cost:"+cost+" | Demand:"+demandScore;
        }
    }
    
    TrieNode root = new TrieNode();
    PriorityQueue<TrieNode> lowStockHeap = new PriorityQueue<>(Comparator.comparingInt(n->n.stock));
    List<String> logs = new ArrayList<>();
    PriorityQueue<Medicine> expiryHeap = new PriorityQueue<>(Comparator.comparingInt(m->m.expiryDays));
    Medicine[] allMeds;
    JTextField searchField = new JTextField(15);
    JTextArea outputArea = new JTextArea(15,40);
    JTextField collectIdField = new JTextField(10);
    JTextField collectDeptField = new JTextField(10);
    JTextField collectMedField = new JTextField(10);
    JTextField expiryDaysField = new JTextField(5);
    JTextField restockBudgetField = new JTextField(5);

    public SmartMedicineSystem(Student st) {
        setTitle("Medicine System — " + st.name);
        setSize(700,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMedicines();
        initUI();
        
        JButton exitBtn = new JButton("← Exit to Menu");
        exitBtn.setBackground(Color.DARK_GRAY);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitBtn.addActionListener(e -> {
            new MainMenuFrame(st).setVisible(true);
            dispose();
        });
        JPanel south = new JPanel();
        south.add(exitBtn);
        add(south, BorderLayout.SOUTH);
    }

    private void initMedicines() {
        allMeds = new Medicine[]{
            new Medicine("Paracetamol", 50, 15, 10, 9),
            new Medicine("Cetrizine", 30, 5, 7, 6),
            new Medicine("Napa", 20, 3, 5, 8),
            new Medicine("Aspirin", 40, 10, 8, 7),
            new Medicine("Antacid", 35, 20, 6, 5)
        };
        for (Medicine m: allMeds) {
            insertTrie(m.name, m.quantity);
            expiryHeap.add(m);
            TrieNode node = searchTrie(m.name);
            if (node != null && node.stock <= 10) lowStockHeap.add(node);
        }
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(new Color(10,25,65));
        
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(15,40,90));
        searchPanel.add(new JLabel("Search Medicine:"){ { setForeground(Color.WHITE); } });
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(30,60,110));
        searchBtn.setForeground(Color.WHITE);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        outputArea.setEditable(false);
        outputArea.setBackground(new Color(20,30,70));
        outputArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        JPanel collectPanel = new JPanel(new GridLayout(5,2,5,5));
        collectPanel.setBackground(new Color(15,35,80));
        TitledBorder border = BorderFactory.createTitledBorder("Collect Medicine");
        border.setTitleColor(Color.WHITE);
        collectPanel.setBorder(border);

        collectPanel.add(new JLabel("Student ID:"){ { setForeground(Color.WHITE); } });
        collectPanel.add(collectIdField);
        collectPanel.add(new JLabel("Department:"){ { setForeground(Color.WHITE); } });
        collectPanel.add(collectDeptField);
        collectPanel.add(new JLabel("Medicine Name:"){ { setForeground(Color.WHITE); } });
        collectPanel.add(collectMedField);
        JButton collectBtn = new JButton("Collect");
        collectBtn.setBackground(new Color(30,60,110));
        collectBtn.setForeground(Color.WHITE);
        collectPanel.add(new JLabel());
        collectPanel.add(collectBtn);
        
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(15,40,90));
        JButton lowStockBtn = new JButton("Show Low Stock"),
                showLogsBtn = new JButton("Show Collection Log"),
                showExpiryBtn = new JButton("Show All by Expiry"),
                alertExpiryBtn = new JButton("Alert Expiring Soon"),
                issueNextBtn = new JButton("Issue Next Expiring"),
                restockBtn = new JButton("Restock");
        
        actionPanel.add(lowStockBtn);
        actionPanel.add(showLogsBtn);
        actionPanel.add(showExpiryBtn);
        actionPanel.add(new JLabel("Expiry ≤"){ { setForeground(Color.WHITE);} });
        actionPanel.add(expiryDaysField);
        actionPanel.add(alertExpiryBtn);
        actionPanel.add(issueNextBtn);
        actionPanel.add(new JLabel("Budget"){ { setForeground(Color.WHITE);} });
        actionPanel.add(restockBudgetField);
        actionPanel.add(restockBtn);

        JButton[] btns = {lowStockBtn, showLogsBtn, showExpiryBtn, alertExpiryBtn, issueNextBtn, restockBtn};
        for (JButton b: btns){ b.setBackground(new Color(30,60,110)); b.setForeground(Color.WHITE); }

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(collectPanel, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.SOUTH);
        add(panel);
        
        searchBtn.addActionListener(e -> searchMedicine());
        collectBtn.addActionListener(e -> collectMedicine());
        lowStockBtn.addActionListener(e -> showLowStock());
        showLogsBtn.addActionListener(e -> showLogs());
        showExpiryBtn.addActionListener(e -> showAllByExpiry());
        alertExpiryBtn.addActionListener(e -> alertExpiring());
        issueNextBtn.addActionListener(e -> issueNextExpiring());
        restockBtn.addActionListener(e -> restockMedicines());
    }
    
    private void insertTrie(String name, int stock) {
        TrieNode cur = root;
        for (char c: name.toLowerCase().toCharArray()){
            int idx = c - 'a';
            if (idx < 0 || idx >= 26) continue;
            if(cur.children[idx] == null) cur.children[idx] = new TrieNode();
            cur = cur.children[idx];
        }
        cur.isEnd = true; 
        cur.medicineName = name; 
        cur.stock = stock;
    }
    
    private TrieNode searchTrie(String name){
        TrieNode cur = root;
        for (char c: name.toLowerCase().toCharArray()){
            int idx = c - 'a';
            if (idx < 0 || idx >= 26 || cur.children[idx] == null) return null;
            cur = cur.children[idx];
        }
        return cur.isEnd ? cur : null;
    }

    private void searchMedicine(){
        String name = searchField.getText().trim();
        if (name.isBlank()){
            outputArea.setText("Enter medicine name to search.\n");
            return;
        }
        TrieNode r = searchTrie(name);
        if (r != null){
            outputArea.setText("✅ Found: "+r.medicineName+" | Stock: "+r.stock+"\n");
        } else {
            outputArea.setText("❌ Not found.\n");
        }
    }

    private void collectMedicine(){
        String id = collectIdField.getText().trim(),
               dept = collectDeptField.getText().trim(),
               med = collectMedField.getText().trim();
        if (id.isBlank() || dept.isBlank() || med.isBlank()){
            outputArea.setText("Fill all collection fields.\n");
            return;
        }
        TrieNode r = searchTrie(med);
        if (r != null && r.stock > 0){
            r.stock--;
            logs.add("ID:"+id+", Dept:"+dept+", Med:"+med);
            outputArea.setText("✅ Collected. Remaining stock: "+r.stock+"\n");
            if (r.stock <= 10 && !lowStockHeap.contains(r)) lowStockHeap.add(r);
        } else {
            outputArea.setText("❌ Not available.\n");
        }
    }

    private void showLowStock(){
        outputArea.setText("📉 Low Stock (≤10):\n");
        if (lowStockHeap.isEmpty()) {
            outputArea.append("All medicines above 10.\n");
        } else {
            PriorityQueue<TrieNode> tmp = new PriorityQueue<>(lowStockHeap);
            while (!tmp.isEmpty()){
                TrieNode n = tmp.poll();
                outputArea.append("- "+n.medicineName+" | Stock: "+n.stock+"\n");
            }
        }
    }

    private void showLogs(){
        outputArea.setText("📒 Collection Logs:\n");
        if (logs.isEmpty()) {
            outputArea.append("No records yet.\n");
        } else {
            logs.forEach(l -> outputArea.append("- "+l+"\n"));
        }
    }

    private void showAllByExpiry(){
        outputArea.setText("📋 All by Expiry:\n");
        if (expiryHeap.isEmpty()){
            outputArea.append("No medicines.\n");
            return;
        }
        for (Medicine m: expiryHeap) outputArea.append("- "+m+"\n");
    }

    private void alertExpiring(){
        String s = expiryDaysField.getText().trim();
        if (s.isBlank()){
            outputArea.setText("Enter days threshold.\n");
            return;
        }
        int days;
        try { days = Integer.parseInt(s); } catch(Exception e){
            outputArea.setText("Invalid number.\n"); return;
        }
        outputArea.setText("⚠️ Expiring in ≤ "+days+" & out of stock:\n");
        boolean found = false;
        for (Medicine m: allMeds){
            TrieNode r = searchTrie(m.name);
            int st = (r != null) ? r.stock : 0;
            if (m.expiryDays <= days && st <= 0){
                outputArea.append("- "+m.name+" (Exp:"+m.expiryDays+", Stock:"+st+")\n");
                found = true;
            }
        }
        if (!found) outputArea.append("No items match.\n");
    }

    private void issueNextExpiring(){
        if (!expiryHeap.isEmpty()){
            Medicine m = expiryHeap.poll();
            outputArea.setText("🩺 Issued: "+m.name+" (Earliest expiry: "+m.expiryDays+")\n");
        } else {
            outputArea.setText("No medicines left.\n");
        }
    }

    private void restockMedicines(){
        String b = restockBudgetField.getText().trim();
        if (b.isBlank()){
            outputArea.setText("Enter budget.\n"); return;
        }
        int budget;
        try { budget = Integer.parseInt(b); } catch(Exception e){
            outputArea.setText("Invalid budget.\n"); return;
        }
        List<Medicine> cand = new ArrayList<>();
        int n = allMeds.length;
        int[][] dp = new int[n+1][budget+1];
        for (int i=1; i<=n; i++){
            Medicine m = allMeds[i-1];
            for (int w=1; w<=budget; w++){
                if (m.cost <= w)
                    dp[i][w] = Math.max(dp[i-1][w], dp[i-1][w-m.cost] + m.demandScore);
                else
                    dp[i][w] = dp[i-1][w];
            }
        }
        int w = budget;
        for(int i=n; i>0 && w>0; i--){
            if (dp[i][w] != dp[i-1][w]){
                Medicine m = allMeds[i-1];
                cand.add(m);
                w -= m.cost;
            }
        }
        outputArea.setText("📦 Restock within budget " + budget + ":\n");
        if (cand.isEmpty()) {
            outputArea.append("No items selected.\n");
        } else {
            for (Medicine m : cand) {
                outputArea.append("- " + m.name + " (Cost:" + m.cost + ", Demand:" + m.demandScore + ")\n");
            }
        }
    }
}