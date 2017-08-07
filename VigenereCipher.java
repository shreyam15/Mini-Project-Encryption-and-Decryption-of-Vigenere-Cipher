/*
 * This code encrypts and decrytpts a String using Vigenere Cipher technique.
 * author@ Shreyam Duttagupta
 * date@ 1st July 2017
 */
import edu.duke.*;
import java.util.*;

public class VigenereCipher {
    CaesarCipher[] ciphers;
    /*
     * Constructor to build a new Ceaser Cipher.
     * @param array of key used to encrypt and decrypt
     */
    public VigenereCipher(int[] key) {
        ciphers = new CaesarCipher[key.length];
        for (int i = 0; i < key.length; i++) {
            ciphers[i] = new CaesarCipher(key[i]);
        }
    }
    
    /*
     * method to encrypt the String of message using Ceasar Cipher class.
     * @param String input from a file or text.
     * @returns the enctypted message
     */
    public String encrypt(String input) {
        StringBuilder answer = new StringBuilder();
        int i = 0;
        for (char c : input.toCharArray()) {
            int cipherIndex = i % ciphers.length;
            CaesarCipher thisCipher = ciphers[cipherIndex];
            answer.append(thisCipher.encryptLetter(c));
            i++;
        }
        return answer.toString();
    }
    
    /*
     * method to decrypt the String of message using Ceaser Cipher class.
     * @param String of message from a file or text.
     * @returns the decrypted message
     */
    public String decrypt(String input) {
        StringBuilder answer = new StringBuilder();
        int i = 0;
        for (char c : input.toCharArray()) {
            int cipherIndex = i % ciphers.length;
            CaesarCipher thisCipher = ciphers[cipherIndex];
            answer.append(thisCipher.decryptLetter(c));
            i++;
        }
        return answer.toString();
    }
    
    public String toString() {
        return Arrays.toString(ciphers);
    }
    
}
