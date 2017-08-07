/*
 * This class uses techniques to break a Vigenere Cipher without knowing the key used in encryption. 
 * We are using the logic of finding the most common letter of occurance and then finding the key.
 * This program can be used to break a Vigenere Cipher of different languages like English, Dutch, Italian, French, Danish, German, Portuguese, Italian, Spanish
 */
import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    
    // mapping of language names to language dictionary
    private HashMap<String, HashSet<String>> dicts;
    // mapping of most common characters to language
    private HashMap<String, Character> commonChars;
    /*
     * Constructor to load the dictionary of words for different languges
     * @see readDictionary
     */
    public VigenereBreaker() {
        dicts = new HashMap<String, HashSet<String>>();
        commonChars = new HashMap<String, Character>();
        
        // reading all dictionaries from the source file
        readDictionary("English");
        readDictionary("Dutch");
        readDictionary("Italian");
        readDictionary("French");
        readDictionary("Danish");       
        readDictionary("German");
        readDictionary("Portuguese");
        readDictionary("Italian");
        readDictionary("Spanish");
        
        // finding most common characters in different languages
        for (String langName : dicts.keySet()) {
            commonChars.put(langName, mostCommonCharIn(dicts.get(langName)));
        }
        
    }
    
    /*
     * This function reads the dictionary of words of different languages
     * @pram the Language name
     */
    
    private void readDictionary(String lang) {
        String path = "dictionaries/"+lang;
        dicts.put(lang, readDictionary(new FileResource(path)));
        
    }
    
    /*
     * This function slices the string in the number of keys used to encrypt the message. If 4 keys were used to encrypt a string, the encrypted message will be sliced in 4 parts.
     * @param It takes a string message, the slice number and total number of slices used.
     * @returns the sliced up string
     */
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sb = new StringBuilder();
        for(int i = whichSlice; i<message.length(); i+=totalSlices){
            sb.append(message.charAt(i));
        }    
        return sb.toString();
    }
    
    /*
     * This function find the keys used in encryption of Vigenere Cipher
     * @param It takes the encrypted message, the key length and the most common word in the language.
     * @returns the array of keys used in encryption
     */
    
    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker();
        for (int i=0; i<klength;i++) {
            String slice = sliceString(encrypted, i, klength);
            key[i] = cc.getKey(slice);
        }
        return key;
    }
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> set = new HashSet<String>();
        for (String word : fr.words()) set.add(word.toLowerCase());
        return set;
    }
    
    /*
     * Coutning the number of words in the encrypted message, which is an actual word in the dictionary for the languge selected.
     * Example: If the langugae is English, "jhdssad" is not a word, but "Hello" is. This words will be compared with the langugae specified.
     */
    
    public int countWords(String message, HashSet<String> dict) {
        int count = 0;
        message = message.toLowerCase();        
        String[] words = message.split("\\W");
        for (String word : words) if (dict.contains(word)) count++;
        return count;        
    }
    
    /*
     * This function tries different key lengths and finds the most number of decrypted words found. 
     * When the array of keys are known, it calls the VigenereCipher method to decrypt the message using the array of keys. 
     */
    
    public String breakForLanguage(String encrypted, String langName) {
        
        HashMap<int[], Integer> keys = new HashMap<int[], Integer>();
        HashSet<String> dict = dicts.get(langName);
        char commonChar = commonChars.get(langName);
        
        for (int i=1;i<100;i++) {
            int[] key = tryKeyLength(encrypted, i, commonChar);
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int cnt = countWords(decrypted, dict);
            keys.put(key, cnt);
            
        }
        
        int maxCoutn = 0;
        int[] foundKey = null;
        
        for (int[] key : keys.keySet()) {
            if (maxCoutn < keys.get(key)) {
                maxCoutn = keys.get(key);
                foundKey = key;
            }
        }
        
        System.out.println("Language:");
        System.out.println(langName);
        
        System.out.println("Key length:");
        System.out.println(foundKey.length);
        
        
        VigenereCipher vc = new VigenereCipher(foundKey);
        System.out.println("Decrypted word count:");
        System.out.println(countWords(vc.decrypt(encrypted), dict));
        System.out.println("----------------------------------------------------------");
        return vc.decrypt(encrypted);
    }
    
    /*
     * This function returns the most common character in any language used.
     */
    public char mostCommonCharIn(HashSet<String> dict) {
        HashMap<Character, Integer> charCounts = new HashMap<Character, Integer>();
        
        // count the characters counts in dictionary
        for (String word : dict) {
            for (char c : word.toLowerCase().toCharArray()) {
                if (!charCounts.containsKey(c)) charCounts.put(c, 1);
                else charCounts.put(c, charCounts.get(c)+1);
            }
        }
        int maxFreq = 0;
        char mostCommon = 'a';
        
        for (char c : charCounts.keySet()) {
            
            if (charCounts.get(c) > maxFreq) {
                maxFreq = charCounts.get(c);
                mostCommon = c;
            }
            
        }
    
        return mostCommon;
        
    }
    
    public String breakForAllLanguages(String enctrypted) {
        for (String langName : dicts.keySet()) {            
            breakForLanguage(enctrypted,langName);          
        }
        return "";
    }

    public void breakVigenere () {
        FileResource fr = new FileResource();
        String story = fr.asString();
        //String dec = breakForAllLanguages(story);
        String dec = breakForLanguage(story,"German");
        System.out.println(dec);
        
     
    }
    
    
    }
