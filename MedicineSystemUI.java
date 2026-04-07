package Medical;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MedicineSystemUI extends JFrame {

    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd = false;
        String medicineName;
        int stock;

        public TrieNode() {}
    }

    static class MedicineTrie {
        TrieNode root = new TrieNode();

        public void insert(String name, int stock) {
            TrieNode curr = root;
            for (char ch : name.toLowerCase().toCharArray()) {
                int idx = ch - 'a';
                if (curr.children[idx] == null) curr.children[idx] = new TrieNode();
                curr = curr.children[idx];
            }
            curr.isEnd = true;
            curr.medicineName = name;
            curr.stock = stock;
        }

        public TrieNode search(String name) {
            TrieNode curr = root;
            for (char ch : name.toLowerCase().toCharArray()) {
                int idx = ch - 'a';
                if (curr.children[idx] == null) return null;
                curr = curr.children[idx];
            }
            return curr.isEnd ? curr : null;
        }
    }

    static class CollectionLog {
        String studentId;
        String dept;
        String medicine;
        int quantity;
        String deliveryLocation;
        int deliveryTime;

        public CollectionLog(String studentId, String dept, String medicine, int quantity,
                             String deliveryLocation, int deliveryTime) {
            this.studentId = studentId;
            this.dept = dept;
            this.medicine = medicine;
            this.quantity = quantity;
            this.deliveryLocation = deliveryLocation;
            this.deliveryTime = deliveryTime;
        }

        public String toString() {
            return "Student ID: " + studentId + ", Dept: " + dept + ", Collected: " + medicine + " x" + quantity +
                    ", Delivered to: " + deliveryLocation + " (ETA: " + deliveryTime + " mins)";
        }
    }

    static class Medicine {
        String name;
        int quantity;
        int expiryDays;
        int cost;
        int demandScore;

        public Medicine(String name, int quantity, int expiryDays, int cost, int demandScore) {
            this.name = name;
            this.quantity = quantity;
            this.expiryDays = expiryDays;
            this.cost = cost;
            this.demandScore = demandScore;
        }

        public String toString() {
            return name + " | Qty: " + quantity + " | Expiry: " + expiryDays + " days | Demand: " + demandScore;
        }
    }

    static class ExpiryHeap {
        PriorityQueue<Medicine> heap = new PriorityQueue<>(Comparator.comparingInt(m -> m.expiryDays));

        public void addMedicine(Medicine m) {
            heap.add(m);
        }
    }

    static class LocationGraph {
        static class Edge {
            int to;
            int weight;
            Edge(int to, int weight) {
                this.to = to;
                this.weight = weight;
            }
        }

        Map<String, Integer> locIndex = new HashMap<>();
        List<List<Edge>> adj = new ArrayList<>();

        public LocationGraph(List<String> locations) {
            for (int i = 0; i < locations.size(); i++) {
                locIndex.put(locations.get(i), i);
                adj.add(new ArrayList<>());
            }
        }

        public void addEdge(String from, String to, int weight) {
            int u = locIndex.get(from);
            int v = locIndex.get(to);
            adj.get(u).add(new Edge(v, weight));
            adj.get(v).add(new Edge(u, weight));
        }

        public int dijkstra(String start, String end) {
            int n = adj.size();
            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE);
            boolean[] visited = new boolean[n];

            int s = locIndex.get(start);
            int t = locIndex.get(end);
            dist[s] = 0;

            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            pq.add(new int[]{s, 0});

            while (!pq.isEmpty()) {
                int[] cur = pq.poll();
                int u = cur[0];
                if (visited[u]) continue;
                visited[u] = true;
                if (u == t) return dist[t];

                for (Edge e : adj.get(u)) {
                    if (!visited[e.to] && dist[u] + e.weight < dist[e.to]) {
                        dist[e.to] = dist[u] + e.weight;
                        pq.add(new int[]{e.to, dist[e.to]});
                    }
                }
            }
            return -1;
        }

        public Set<String> getLocations() {
            return locIndex.keySet();
        }
    }

    private JFrame homePage;
    private MedicineTrie trie = new MedicineTrie();
    private List<CollectionLog> logs = new ArrayList<>();
    private ExpiryHeap expiryHeap = new ExpiryHeap();
    private Medicine[] allMedicines;

    private JTextField searchField = new JTextField(15);
    private JTextArea outputArea = new JTextArea(7, 25);
    private JTextField collectMedField = new JTextField(10);
    private JTextField collectQtyField = new JTextField(5);
    private JTextField expiryDaysField = new JTextField(5);
    private JComboBox<String> deliveryLocationBox;

    private JTextField singleMedNameField = new JTextField(10);
    private JTextField singleMedBudgetField = new JTextField(5);
    private JButton maxQuantityBtn = new JButton("Calculate Max Quantity");

    private String loggedInUser;
    private String loggedInDept;

    private LocationGraph locationGraph;
    private final String FIXED_START = "Mirpur 12";

    public MedicineSystemUI(JFrame homePage, String loggedInUser, String loggedInDept) {
        this.homePage = homePage;
        this.loggedInUser = loggedInUser;
        this.loggedInDept = loggedInDept;

        setTitle("\uD83D\uDC8A Smart Medicine Inventory System");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initLocations();
        initMedicines();
        initUI();
    }

    private void initLocations() {
        List<String> locations = Arrays.asList("Mirpur 12", "Banani", "Gulshan", "Dhanmondi", "Uttara", "Motijheel");
        locationGraph = new LocationGraph(locations);
        locationGraph.addEdge("Mirpur 12", "Banani", 20);
        locationGraph.addEdge("Mirpur 12", "Gulshan", 25);
        locationGraph.addEdge("Mirpur 12", "Dhanmondi", 15);
        locationGraph.addEdge("Mirpur 12", "Uttara", 30);
        locationGraph.addEdge("Mirpur 12", "Motijheel", 35);
        locationGraph.addEdge("Banani", "Gulshan", 10);
        locationGraph.addEdge("Gulshan", "Dhanmondi", 15);
        locationGraph.addEdge("Dhanmondi", "Motijheel", 20);
        locationGraph.addEdge("Uttara", "Motijheel", 40);
    }

    private void initMedicines() {
        allMedicines = new Medicine[] {
                new Medicine("Paracetamol", 50, 15, 10, 9),
                new Medicine("Cetrizine", 30, 5, 7, 6),
                new Medicine("Napa", 20, 3, 5, 8),
                new Medicine("Aspirin", 40, 10, 8, 7),
                new Medicine("Antacid", 35, 20, 6, 5)
        };

        for (Medicine m : allMedicines) {
            expiryHeap.addMedicine(m);
            trie.insert(m.name, m.quantity);
        }
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(225, 255, 225));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("\uD83D\uDD0D Search Medicine:"));
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);

        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel collectPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        collectPanel.setBackground(new Color(225, 255, 225));
        collectPanel.add(new JLabel("Medicine Name:"));
        collectPanel.add(collectMedField);
        collectPanel.add(new JLabel("Quantity:"));
        collectPanel.add(collectQtyField);
        collectPanel.add(new JLabel("Delivery Location:"));

        String[] locationsArr = locationGraph.getLocations().stream().filter(loc -> !loc.equals(FIXED_START)).sorted().toArray(String[]::new);
        deliveryLocationBox = new JComboBox<>(locationsArr);
        collectPanel.add(deliveryLocationBox);

        JButton collectBtn = new JButton("Collect & Deliver");
        collectPanel.add(new JLabel(""));
        collectPanel.add(collectBtn);

        JPanel expiryPanel = new JPanel();
        expiryPanel.setBackground(new Color(225, 255, 225));
        expiryPanel.add(new JLabel("Enter Expiry Days:"));
        expiryPanel.add(expiryDaysField);
        JButton expiryFilterBtn = new JButton("Filter Expiry");
        expiryPanel.add(expiryFilterBtn);

        JPanel actionPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        actionPanel.setBackground(new Color(225, 255, 225));
        JButton showLogsBtn = new JButton("Show Collection Log");
        JButton showExpiryBtn = new JButton("Show All by Expiry");
        JButton issueNextBtn = new JButton("Issue Next Expiring");
        JButton backBtn = new JButton("Back to Home");

        JButton[] buttons = {showLogsBtn, showExpiryBtn, issueNextBtn, backBtn};
        for (JButton btn : buttons) actionPanel.add(btn);

        JPanel maxQuantityPanel = new JPanel();
        maxQuantityPanel.setBackground(new Color(225, 255, 225));
        maxQuantityPanel.add(new JLabel("Medicine Name:"));
        maxQuantityPanel.add(singleMedNameField);
        maxQuantityPanel.add(new JLabel("Budget (Tk):"));
        maxQuantityPanel.add(singleMedBudgetField);
        maxQuantityPanel.add(maxQuantityBtn);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(collectPanel, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.EAST);
        panel.add(expiryPanel, BorderLayout.SOUTH);
        panel.add(maxQuantityPanel, BorderLayout.PAGE_END);

        add(panel);

        searchBtn.addActionListener(e -> searchMedicine());
        collectBtn.addActionListener(e -> collectMedicine());
        showLogsBtn.addActionListener(e -> showLogs());
        showExpiryBtn.addActionListener(e -> showAllByExpiry());
        issueNextBtn.addActionListener(e -> issueNextExpiring());
        expiryFilterBtn.addActionListener(e -> showExpiryWithinDays());
        backBtn.addActionListener(e -> {
            this.dispose();
            homePage.setVisible(true);
        });
        maxQuantityBtn.addActionListener(e -> {
            String medName = singleMedNameField.getText().trim();
            int budget;
            try {
                budget = Integer.parseInt(singleMedBudgetField.getText().trim());
            } catch (NumberFormatException ex) {
                outputArea.setText("❌ Invalid budget.");
                return;
            }
            showMaxQuantityForSingleMedicine(medName, budget);
        });
    }

    private void searchMedicine() {
        String query = searchField.getText().trim();
        TrieNode result = trie.search(query);
        outputArea.setText(result != null ? "Found: " + result.medicineName + "\nStock: " + result.stock : "Medicine not found.");
    }

    private void collectMedicine() {
        String name = collectMedField.getText().trim();
        int qty;
        try {
            qty = Integer.parseInt(collectQtyField.getText().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("❌ Invalid quantity.");
            return;
        }
        TrieNode node = trie.search(name);
        if (node != null && node.stock >= qty) {
            String deliveryLoc = (String) deliveryLocationBox.getSelectedItem();
            int deliveryTime = locationGraph.dijkstra(FIXED_START, deliveryLoc);
            node.stock -= qty;
            logs.add(new CollectionLog(loggedInUser, loggedInDept, name, qty, deliveryLoc, deliveryTime));
            outputArea.setText("✅ " + qty + " unit(s) of " + name + " collected and delivered to " + deliveryLoc + " (ETA: " + deliveryTime + " mins).\nRemaining Stock: " + node.stock);
        } else {
            outputArea.setText("❌ Not enough stock or medicine not found.");
        }
    }

    private void showLogs() {
        StringBuilder sb = new StringBuilder("📜 Collection Logs:\n");
        for (CollectionLog log : logs) sb.append(log).append("\n");
        outputArea.setText(sb.toString());
    }

    private void showAllByExpiry() {
        StringBuilder sb = new StringBuilder("🗂️ Medicines by Expiry:\n");
        for (Medicine m : expiryHeap.heap) sb.append(m).append("\n");
        outputArea.setText(sb.toString());
    }

    private void issueNextExpiring() {
        if (!expiryHeap.heap.isEmpty()) {
            Medicine m = expiryHeap.heap.poll();
            outputArea.setText("Issued: " + m);
        } else outputArea.setText("No medicines available.");
    }

    private void showExpiryWithinDays() {
        try {
            int days = Integer.parseInt(expiryDaysField.getText().trim());
            StringBuilder sb = new StringBuilder("⏳ Medicines expiring within " + days + " days:\n");
            for (Medicine m : expiryHeap.heap) {
                if (m.expiryDays <= days) sb.append(m).append("\n");
            }
            outputArea.setText(sb.toString());
        } catch (NumberFormatException e) {
            outputArea.setText("❌ Please enter valid expiry days.");
        }
    }

    private void showMaxQuantityForSingleMedicine(String medicineName, int budget) {
        TrieNode node = trie.search(medicineName);
        if (node == null) {
            outputArea.setText("Medicine not found: " + medicineName);
            return;
        }
        int pricePerUnit = 0;
        for (Medicine m : allMedicines) {
            if (m.name.equalsIgnoreCase(medicineName)) {
                pricePerUnit = m.cost;
                break;
            }
        }
        if (pricePerUnit == 0) {
            outputArea.setText("Price info not found for " + medicineName);
            return;
        }
        int maxQuantity = budget / pricePerUnit;
        outputArea.setText("With a budget of " + budget + " tk, you can buy maximum " + maxQuantity + " units of " + medicineName + " (Price per unit: " + pricePerUnit + " tk).");
    }
}
