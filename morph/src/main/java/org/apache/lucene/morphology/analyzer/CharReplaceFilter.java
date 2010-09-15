package org.apache.lucene.morphology.analyzer;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import java.io.IOException;
import java.util.Map;

/**
 * Filter which replaces chars in term, given replacementPairs map.
 * If trimBuffer c-tor param is set, it also removes leading/trailing whitespaces
 */
public final class CharReplaceFilter extends TokenFilter {
  private Map<Character, Character> replacementPairs;
  private boolean trimBuffer;

  public CharReplaceFilter(TokenStream in,
                           Map<Character, Character> replacementPairs,
                           boolean trimBuffer){
    super(in);
    termAtt = addAttribute(TermAttribute.class);
    this.replacementPairs = replacementPairs;
    this.trimBuffer = trimBuffer;
  }

  private TermAttribute termAtt;

  @Override
  public final boolean incrementToken() throws IOException {
    if (input.incrementToken()) {

      final char[] buffer = termAtt.termBuffer();
      final int length = termAtt.termLength();
      boolean replaceOccured = false;
      for(int i=0;i<length;i++){
          Character ch = buffer[i];
          if (replacementPairs.containsKey(ch)){
              buffer[i] = replacementPairs.get(ch);
              replaceOccured = true;
          }
      }
      if(trimBuffer && replaceOccured){
          termAtt.setTermBuffer(new String(buffer, 0, termAtt.termLength()).trim());
      }
      return true;
    } else
      return false;
  }
}