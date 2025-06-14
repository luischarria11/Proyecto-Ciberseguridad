package crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class CryptoManager {
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    private static final int HASH_LENGTH = 32; // SHA-256

    private static SecretKey deriveKey(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public static void encryptFile(String inputFile, String password) throws Exception {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);

        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[IV_LENGTH];
        sr.nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] fileData = Files.readAllBytes(Path.of(inputFile));
        byte[] encryptedData = cipher.doFinal(fileData);

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(fileData);

        String outputFile = inputFile + ".enc";
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(salt);
            fos.write(iv);
            fos.write(encryptedData);
            fos.write(hash);
        }

        System.out.println("Archivo cifrado exitosamente: " + outputFile);
    }

    public static void decryptFile(String inputFile, String password) throws Exception {
        byte[] allBytes = Files.readAllBytes(Path.of(inputFile));

        byte[] salt = Arrays.copyOfRange(allBytes, 0, SALT_LENGTH);
        byte[] iv = Arrays.copyOfRange(allBytes, SALT_LENGTH, SALT_LENGTH + IV_LENGTH);
        byte[] hash = Arrays.copyOfRange(allBytes, allBytes.length - HASH_LENGTH, allBytes.length);
        byte[] encryptedData = Arrays.copyOfRange(allBytes, SALT_LENGTH + IV_LENGTH, allBytes.length - HASH_LENGTH);

        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] decryptedData;
        try {
            decryptedData = cipher.doFinal(encryptedData);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new Exception("Contraseña incorrecta o datos corruptos.");
        }

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] recalculatedHash = sha256.digest(decryptedData);

        if (!MessageDigest.isEqual(hash, recalculatedHash)) {
            throw new Exception("El hash no coincide. Archivo modificado o contraseña incorrecta.");
        }

        String outputFile = inputFile.replace(".enc", ".dec");
        Files.write(Path.of(outputFile), decryptedData);
        System.out.println("Archivo descifrado exitosamente: " + outputFile);
    }
}