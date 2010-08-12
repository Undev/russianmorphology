package org.apache.lucene.morphology;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * @author spariev
 * Splits word with punctuation marks (. , - , &, etc) inside on there marks, removing suffixs.
 * Example
 * twitter.com -> [ twitter.com twitter ]
 * Б.В. -> [ "Б.В.", "Б.В", "Б" ]
 * СМ-Доктор -> [ "СМ-Доктор" "СМ" ]
 * 222-33-22 -> [222-33-22, 222-33, 222]
 */
public class PhrasePunctuationSplitter {
    private static final List<String> EMPTY_STRING_LIST = new ArrayList<String>();
    private static char[] PUNCTUATION_MARKS = new char[]{'.', ',', '_', '/', '-', '@', '&', '\'' };
    static {
         Arrays.sort(PUNCTUATION_MARKS);
    }
    private static final Pattern PUNCT_REGEXP = Pattern.compile("[\\p{Punct}]");

    public List<String> split(String phrase) {
        if (!containsPunctuation(phrase)) {
            return EMPTY_STRING_LIST;
        }
        List<String> results = new ArrayList<String>();
//        String[] splittedPhrase = (PUNCT_REGEXP.split(phrase));
        String currentPhrase = phrase ;
        results.add(currentPhrase);
        while(containsPunctuation(currentPhrase)) {
            for (int i = 0; i < PUNCTUATION_MARKS.length; i++) {
                int pmIndex = currentPhrase.lastIndexOf(PUNCTUATION_MARKS[i]);
                if(pmIndex >= 0){
                    currentPhrase = currentPhrase.substring(0, pmIndex);
                    results.add(currentPhrase);
                    break;
                }
            }
        }
//        Collections.reverse(results);
        return results;
    }

    public boolean containsPunctuation(String phrase) {
        return PUNCT_REGEXP.matcher(phrase).find();
    }
}
