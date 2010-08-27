package org.apache.lucene.morphology;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author spariev
 * Splits word on punctuation marks (. , - , &, etc).
 * Example
 * twitter.com -> [twitter.com twitter com]
 * Б.В. -> [ "Б.В.", "Б", "В" ]
 * СМ-Доктор -> [ "СМ-Доктор" "СМ" "Доктор" ]
 * 222-33-22 -> [222-33-22, 222, 33, 222]
 */
public class PhrasePunctuationSplitter {
    private static final List<String> EMPTY_STRING_LIST = new ArrayList<String>();
    private static char[] PUNCTUATION_MARKS = new char[]{'.', ',', '_', '/', '-', '@', '&', '\'' };
    static {
         Arrays.sort(PUNCTUATION_MARKS);
    }

    public static List<String> split(String phrase) {
        if (firstIndexOfPunctuationMark(phrase) < 0) {
            return EMPTY_STRING_LIST;
        }
        List<String> results = new ArrayList<String>();
        String currentPhrase = phrase ;
        results.add(currentPhrase);
        int firstMarkPos = -1;
        while((firstMarkPos = firstIndexOfPunctuationMark(currentPhrase)) >= 0) {
            String subStr = currentPhrase.substring(0, firstMarkPos);
            currentPhrase = currentPhrase.substring(firstMarkPos + 1, currentPhrase.length());
            results.add(subStr);
        }
        results.add(currentPhrase);
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

    public static int firstIndexOfPunctuationMark(String phrase) {
        char[] phraseChars = phrase.toCharArray();
        for (int i = 0; i < phraseChars.length; i++) {
            char phraseChar = phraseChars[i];
            if (Arrays.binarySearch(PUNCTUATION_MARKS, phraseChar) >= 0) return i;
        }
        return -1;
    }
}
