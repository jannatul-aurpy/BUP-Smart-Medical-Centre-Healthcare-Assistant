package Medical;

import java.util.LinkedList;

public class StudentHashTable {
    private static final int SIZE = 100;
    private LinkedList<Student>[] table;

    public StudentHashTable() {
        table = new LinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) table[i] = new LinkedList<>();
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    public void insert(Student student) {
        int index = hash(student.id);
        table[index].add(student);
    }

    public Student get(String id, String password) {
        int index = hash(id);
        for (Student s : table[index]) {
            if (s.id.equals(id) && s.hashedPassword.equals(Student.hashPassword(password))) {
                return s;
            }
        }
        return null;
    }

    public boolean exists(String id) {
        int index = hash(id);
        for (Student s : table[index]) {
            if (s.id.equals(id)) return true;
        }
        return false;
    }
}

