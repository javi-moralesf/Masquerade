package com.moralesf.masquerade.java.Key;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by javi_moralesf on 10/04/15.
 */
public class KeyGen implements KeyGenInterface {

    private Random random = null;
    private String key;
    private int length;

    public KeyGen(int length){
        this.length = length;
        this.random = new Random();
        key = generateDifficulty(length);
    }

    private String generateDifficulty(int difficultyLevel) {
        String new_key = "";
        for(int i = 0; i < difficultyLevel; i++){
            String new_char = generateRandomAlphabet();
            new_key = new StringBuilder(new_key)
                    .append(new_char)
                    .toString();
        }
        return new_key;
    }

    public String generateRandomAlphabet() {
        char character = (char) (97 + this.random.nextInt(26));
        String alphabet = String.valueOf(character);
        if(this.random.nextInt() == 1){
            alphabet = alphabet.toUpperCase();
        }
        return alphabet;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public void setFalsePositives(ArrayList<String> falsePositives) {
        while(falsePositives.contains(this.key)){
            key = generateDifficulty(length);
        }
    }
}
