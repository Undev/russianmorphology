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
package org.apache.lucene.morphology.russian;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;


public class RussianAnalyzerTest {

    @Test
    public void shouldGiveCorrectWords() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-analayzer-answer.txt");
        BufferedReader breader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String[] strings = breader.readLine().replaceAll(" +", " ").trim().split(" ");
        HashSet<String> answer = new HashSet<String>(Arrays.asList(strings));
        stream.close();

        RussianPunctuationAnalyzer morphologyAnalyzer = new RussianPunctuationAnalyzer();
        stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-analayzer-data.txt");

        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        TokenStream in = morphologyAnalyzer.tokenStream(null, reader);
        TermAttribute term = in.addAttribute(TermAttribute.class);
        HashSet<String> result = new HashSet<String>();
        List<String> r2 = new ArrayList<String>();
        for (; ;) {
            if (!in.incrementToken()) {
                break;
            }
            result.add(term.term());
            r2.add(term.term());
            //
        }
        stream.close();

        for(int i = 0; i < strings.length; i++){
            System.out.print("  " + strings[i]);
        }
        System.out.println("");
        System.out.println("  vs  ");
        for(int i = 0; i < r2.size(); i++){
            System.out.print("  " + r2.get(i));
        }
        System.out.println("");
        assertThat(result, equalTo(answer));
    }

    @Test
    public void shouldCorrectlyParseWordsWithPunctuation() throws Exception {
        RussianPunctuationAnalyzer morphologyAnalyzer = new RussianPunctuationAnalyzer();
//        StringBufferInputStream stream = new StringBufferInputStream("");
        InputStream stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-analayzer-answer-2.txt");
        BufferedReader breader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String[] strings = breader.readLine().replaceAll(" +", " ").trim().split(" ");
        HashSet<String> answer = new HashSet<String>(Arrays.asList(strings));
        stream.close();

        stream = this.getClass().getResourceAsStream("/org/apache/lucene/morphology/russian/russian-analayzer-data-2.txt");
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        TokenStream in = morphologyAnalyzer.tokenStream(null, reader);
        TermAttribute term = in.addAttribute(TermAttribute.class);
        HashSet<String> result = new HashSet<String>();
        List<String> r2 = new ArrayList<String>();
        for (; ;) {
            if (!in.incrementToken()) {
                break;
            }
            result.add(term.term());
            r2.add(term.term());
            //
        }
        stream.close();

        for(int i = 0; i < strings.length; i++){
            System.out.print("  " + strings[i]);
        }
        System.out.println("  vs  ");
        for(int i = 0; i < r2.size(); i++){
            System.out.print("  " + r2.get(i));
        }
        System.out.println("");
        assertThat(result, equalTo(answer));
    }
}

