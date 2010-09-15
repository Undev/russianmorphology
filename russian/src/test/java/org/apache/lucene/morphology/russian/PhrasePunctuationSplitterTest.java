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
import org.apache.lucene.morphology.PhrasePunctuationSplitter;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class PhrasePunctuationSplitterTest {

    @Test
    public void shouldCorrectlyParseWordsWithPunctuation() throws Exception {
        PhrasePunctuationSplitter splitter = new PhrasePunctuationSplitter();
        Map<String,String[]> expectations = new HashMap<String,String[]>();
        expectations.put("twitter.com", new String[]{"twitter.com", "twitter", "com"});
        expectations.put("B.V.", new String[]{"B.V.", "B", "V"});
        expectations.put("CM-Doctor", new String[]{"CM-Doctor", "CM", "Doctor"});
        expectations.put("games-2010.", new String[]{"games-2010.", "games", "2010"});
        expectations.put("Doctor", new String[]{});
        expectations.put("Tu-154", new String[]{"Tu-154", "Tu", "154"});
        expectations.put("Tu-154", new String[]{"Tu-154", "Tu", "154"});
        expectations.put("Прет-а-Порте", new String[]{"Прет-а-Порте", "Прет", "а", "Порте"});
        expectations.put("Металл-Трейд-М", new String[]{"Металл-Трейд-М", "Металл", "Трейд", "М"});

        for (String k : expectations.keySet()) {
            List<String> results = splitter.split(k);
            System.out.println("phrase = " + k);
            System.out.println("results = " + results);
            System.out.println("expectations = " + Arrays.asList(expectations.get(k)));

            Assert.assertArrayEquals(expectations.get(k), (String[])results.toArray(new String[]{}));
        }
    }
}