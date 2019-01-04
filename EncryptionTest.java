import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class EncryptionTest {
    private static final String PUBLIC_KEY_FILE  = "/Users/michaelblind/.ssh/id_rsa.pub";
    private static final String PRIVATE_KEY_FILE = "/Users/michaelblind/.ssh/id_rsa";

    public static void main(String[] args) throws Exception {
        /*try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
            new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // Saving the Private key in a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
            new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        String message = "Success!";
        byte[] encrypted = encrypt(message);
        System.out.println(encrypted);
        String decrypted = decrypt(encrypted);
        System.out.println(decrypted);
    }

    static byte[] encrypt(String message) throws Exception{
        byte[] encrypted = null;
        PublicKey public_key = getPublicKey();
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            // Encrypt using public_key
            cipher.init(Cipher.ENCRYPT_MODE, public_key);
            encrypted = cipher.doFinal(message.getBytes());
        } catch (Exception e) {e.printStackTrace();}
        return encrypted;
    }

    static String decrypt(byte[] encrypted) throws Exception {
        byte[] decrypted = null;
        PrivateKey key = getPrivateKey();
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");
            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(encrypted);
        } catch (Exception ex) {ex.printStackTrace();}
        return new String(decrypted);
    }

    static PublicKey getPublicKey() throws Exception {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
        return (PublicKey) stream.readObject();
    }

    static PrivateKey getPrivateKey() throws Exception{
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
        return (PrivateKey) stream.readObject();
    }
}
