package Medical;

import java.security.MessageDigest;

public class Student {
    public String id, name, dept, hashedPassword;

    public Student(String id, String name, String dept, String password) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.hashedPassword = hashPassword(password);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed");
        }
    }

}
