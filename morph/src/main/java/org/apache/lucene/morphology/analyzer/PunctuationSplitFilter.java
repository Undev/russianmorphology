package org.apache.lucene.morphology.analyzer;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.morphology.PhrasePunctuationSplitter;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;


public class PunctuationSplitFilter extends TokenFilter {
    public static final String TOKEN_TYPE_SYNONYM = "SYNONYM";

    private TermAttribute termAtt;
    private AttributeSource save;
    private PhrasePunctuationSplitter punctuationSplitter = new PhrasePunctuationSplitter();
    private Stack<State> phraseFormsStack = new Stack<State>();

    public PunctuationSplitFilter(TokenStream tokenStream) {
        super(tokenStream);
        termAtt = addAttribute(TermAttribute.class);
        save = tokenStream.cloneAttributes();
        System.out.println("save = " + save);
    }

    public boolean incrementToken() throws IOException {
        if(phraseFormsStack.size() > 0) {
            State syn = phraseFormsStack.pop();
            restoreState(syn);
            return true;
        }

        if(!input.incrementToken()) return false;

        if (!punctuationSplitter.containsPunctuation(termAtt.term())){
            return true;
        }

        addPhaseFormsToStack();
        //comment this out if original term is needed
        State syn = phraseFormsStack.pop();
        restoreState(syn);
        return true;
    }

    private void addPhaseFormsToStack() throws IOException {
        List<String> forms = punctuationSplitter.split(termAtt.term());
        Collections.reverse(forms);
        if(forms.size() == 0) return;
        State current = captureState();
        for(int i = 0; i < forms.size(); i++){
            save.restoreState(current);
            PunctuationSplitFilter.setTerm(save, forms.get(i));
//            PunctuationSplitFilter.setPositionIncrement(save, 0);
            phraseFormsStack.push(save.captureState());
        }
    }

    /**
     * Creates and returns a token for the given synonym of the current input
     * token; Override for custom (stateless or stateful) behavior, if desired.
     *
     * @param synonym       a synonym for the current token's term
     * @param current       the current token from the underlying child stream
     * @param reusableToken the token to reuse
     * @return a new token, or null to indicate that the given synonym should be
     *         ignored
     */
    protected Token createToken(String synonym, Token current, final Token reusableToken) {
        reusableToken.reinit(current, synonym);
        reusableToken.setTermBuffer(synonym);
        reusableToken.setPositionIncrement(0);
        return reusableToken;
    }

    public static void setTerm(AttributeSource source, String term) {
        TermAttribute attr = (TermAttribute) source.addAttribute(TermAttribute.class);
        attr.setTermBuffer(term);
    }

//    public static void setPositionIncrement(AttributeSource source, int posIncr) {
//        PositionIncrementAttribute attr = (PositionIncrementAttribute) source.addAttribute(PositionIncrementAttribute.class);
//        attr.setPositionIncrement(posIncr);
//    }

}