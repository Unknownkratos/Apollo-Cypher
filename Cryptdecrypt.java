import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cryptdecrypt {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("encrypt or decrypt:");
        String operation = scan.nextLine();
        String filePath = "C:\\Users\\Administrator\\OneDrive\\Desktop\\Encryption-Decryption\\test.txt"; // Set the file path here

        try {
            String fileContent = readFromFile(filePath);
            if (operation.equals("encrypt")) {
                String key = generateKey();
                String encryptedContent = encrypt(fileContent, key);
                writeToFile(filePath, encryptedContent);
                System.out.println("File encrypted successfully. Key: " + key);
            } else if (operation.equals("decrypt")) {
                System.out.println("Enter the key:");
                String key = scan.nextLine();
                if (key.length() != 0) {
                    String decryptedContent = decrypt(fileContent, key);
                    writeToFile(filePath, decryptedContent);
                    System.out.println("File decrypted successfully.");
                } else {
                    System.out.println("Invalid key. Please provide a valid key for decryption.");
                }
            } else {
                System.out.println("Invalid operation. Please enter 'encrypt' or 'decrypt'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(String filePath) throws Exception {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        return new String(fileBytes);
    }

    private static void writeToFile(String filePath, String content) throws Exception {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    private static String encrypt(String data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String decrypt(String data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // 256-bit key
            SecretKey secretKey = keyGen.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            // Define custom character set for Base64 encoding
            char[] customAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            Base64.Encoder encoder = Base64.getEncoder().withoutPadding();

            // Apply custom encoding
            byte[] customEncodedBytes = encoder.encode(keyBytes);
            char[] customEncodedChars = new char[customEncodedBytes.length];
            for (int i = 0; i < customEncodedBytes.length; i++) {
                customEncodedChars[i] = customAlphabet[customEncodedBytes[i] & 0x3F];
            }
            return new String(customEncodedChars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
