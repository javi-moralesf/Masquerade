package com.moralesf.masquerade.java.Key;

import java.util.Random;

/**
 * Created by javi_moralesf on 10/04/15.
 */
public class KeyGen implements KeyGenInterface {

    private Random random = null;
    private String key;

    public KeyGen(){
        this.random = new Random();
        key = Long.toString(System.currentTimeMillis());
        key = key.substring(2, 10);
        key = generateDifficulty(key, 2);
    }

    private String generateDifficulty(String key, int difficultyLevel) {
        for(int i = 0; i < difficultyLevel; i++){
            String new_character = generateRandomAlphabet();
            int new_position = this.random.nextInt(key.length() - 1);
            key = new StringBuilder(key).insert(new_position, new_character).toString();
        }
        return key;
    }

    public String generateRandomAlphabet() {
        String alphabet = null;

        char character = (char) (97 + this.random.nextInt(26));
        alphabet = String.valueOf(character);
        if(this.random.nextInt() == 1){
            alphabet = alphabet.toUpperCase();
        }
        return alphabet;
    }

    @Override
    public String getKey() {
        return this.key;
    }

}
