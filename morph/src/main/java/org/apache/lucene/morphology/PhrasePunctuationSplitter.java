package org.apache.lucene.morphology;

import java.util.*;
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

    public List<String> split(String phrase) {
        if (lastIndexOfPunctuationMark(phrase) < 0) {
            return EMPTY_STRING_LIST;
        }
        List<String> results = new ArrayList<String>();
        String currentPhrase = phrase ;
        results.add(currentPhrase);
        int lastMarkPos = -1;
        while((lastMarkPos = lastIndexOfPunctuationMark(currentPhrase)) >= 0) {
             currentPhrase = currentPhrase.substring(0, lastMarkPos);
             results.add(currentPhrase);
        }
        //remove empty terms
        Iterator<String> iter = results.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
            if (s == null || s.trim().length() == 0){
                iter.remove();
            }
        }
        return results;
    }

    public int lastIndexOfPunctuationMark(String phrase) {
        char[] phraseChars = phrase.toCharArray();
        for (int i = phraseChars.length -1; i >= 0 ; i--) {
            char phraseChar = phraseChars[i];
            if (Arrays.binarySearch(PUNCTUATION_MARKS, phraseChar) >= 0) return i;
        }
        return -1;
    }
}
