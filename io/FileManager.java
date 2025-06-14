package io;

import java.io.*;
import java.util.Base64;

public class FileManager {
    public static void writeEncryptedFile(String path, byte[] encrypted, byte[] salt, byte[] iv, String hash) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(Base64.getEncoder().encodeToString(salt));
            writer.newLine();
            writer.write(Base64.getEncoder().encodeToString(iv));
            writer.newLine();
            writer.write(hash);
            writer.newLine();
            writer.write(Base64.getEncoder().encodeToString(encrypted));
        }
    }

    public static EncryptedData readEncryptedFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            byte[] salt = Base64.getDecoder().decode(reader.readLine());
            byte[] iv = Base64.getDecoder().decode(reader.readLine());
            String hash = reader.readLine();
            StringBuilder encryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedData.append(line);
            }
            byte[] encrypted = Base64.getDecoder().decode(encryptedData.toString());
            return new EncryptedData(salt, iv, encrypted, hash);
        }
    }

    public static class EncryptedData {
        public final byte[] salt;
        public final byte[] iv;
        public final byte[] encrypted;
        public final String hash;

        public EncryptedData(byte[] salt, byte[] iv, byte[] encrypted, String hash) {
            this.salt = salt;
            this.iv = iv;
            this.encrypted = encrypted;
            this.hash = hash;
        }
    }
}
