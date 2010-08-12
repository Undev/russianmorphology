package org.apache.lucene.morphology.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.LetterDecoderEncoder;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class PunctuationAnalyzer extends Analyzer{
    private LuceneMorphology luceneMorph;

    public PunctuationAnalyzer(LuceneMorphology luceneMorph) {
        this.luceneMorph = luceneMorph;
    }

    public PunctuationAnalyzer(String pathToMorph, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(pathToMorph, letterDecoderEncoder);
    }

    public PunctuationAnalyzer(InputStream inputStream, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(inputStream, letterDecoderEncoder);
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
//        TokenStream result = new StandardTokenizer(Version.LUCENE_30, reader);
//        result = new StandardFilter(result);
//        result = new LowerCaseFilter(result);
//        result = new PunctuationSplitFilter(result);
//        return new MorphologyFilter(result, luceneMorph);

        TokenStream result = new WhitespaceTokenizer(reader);
        result = new LowerCaseFilter(result);
        result = new PunctuationSplitFilter(result); //
        return new MorphologyFilter(result, luceneMorph);
    }
}
