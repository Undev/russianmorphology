/**
 * Copyright 2009 Alexander Kuznetsov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.morphology.analyzer;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class MorphologyFilter extends TokenFilter {
    public static final String TOKEN_TYPE_SYNONYM = "SYNONYM";

    private LuceneMorphology luceneMorph;
    private TermAttribute termAtt;
    private AttributeSource save;

    public MorphologyFilter(TokenStream tokenStream, LuceneMorphology luceneMorph) {
        super(tokenStream);
        this.luceneMorph = luceneMorph;
        termAtt = addAttribute(TermAttribute.class);
        save = tokenStream.cloneAttributes();
    }


    private Stack<State> synonymStack = new Stack<State>();

    public boolean incrementToken() throws IOException {
        if(synonymStack.size() > 0) {
            State syn = synonymStack.pop();
            restoreState(syn);
            return true;
        }

        if(!input.incrementToken()) return false;

        if (!luceneMorph.checkString(termAtt.term())){
            return true;
        }
        addAliasesToStack();
        //comment this out if original term is needed
        State syn = synonymStack.pop();
        restoreState(syn);
        return true;
    }

    private void addAliasesToStack() throws IOException {
        List<String> forms = luceneMorph.getNormalForms(termAtt.term());
        if(forms.size() == 0) return;
        State current = captureState();
        for(int i = 0; i < forms.size(); i++){
            save.restoreState(current);
            MorphologyFilter.setTerm(save, forms.get(i));
            MorphologyFilter.setType(save, TOKEN_TYPE_SYNONYM);
            MorphologyFilter.setPositionIncrement(save, 0);
            synonymStack.push(save.captureState());
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

    public static void setType(AttributeSource source, String type) {
        TypeAttribute attr = (TypeAttribute) source.addAttribute(TypeAttribute.class);
        attr.setType(type);
    }

    public static void setPositionIncrement(AttributeSource source, int posIncr) {
        PositionIncrementAttribute attr = (PositionIncrementAttribute) source.addAttribute(PositionIncrementAttribute.class);
        attr.setPositionIncrement(posIncr);
    }

}
