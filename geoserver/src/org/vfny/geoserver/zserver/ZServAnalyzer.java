/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;


/**
 * A lucene Analyzer that filters LetterTokenizer with LowerCaseFilter.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: ZServAnalyzer.java,v 1.3.6.1 2003/12/30 23:00:41 dmzwiers Exp $
 */
public final class ZServAnalyzer extends Analyzer {
    /**
     * The tokenStream class to process the tokens.
     *
     * @param fieldName Needed for override?
     * @param reader The reader of tokens to normalize.
     *
     * @return The token stream with this tokenizer.
     */
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        return new NumberLowerCaseTokenizer(reader);
    }

    private class NumberLowerCaseTokenizer extends CharTokenizer {
        /**
         * Construct a new LetterTokenizer.
         *
         * @param in The reader to get characters from.
         */
        public NumberLowerCaseTokenizer(Reader in) {
            super(in);
        }

        /**
         * Puts chars to lower case.
         *
         * @param c The char to lower.
         *
         * @return The lowercase version of c.
         */
        protected char normalize(char c) {
            return Character.toLowerCase(c);
        }

        /**
         * Collects only characters which satisfy {@link
         * java.lang.Character#isLetter(char)} or  {@link
         * java.lang.Character#isDigit(char)}
         *
         * @param c The character to test.
         *
         * @return <tt>true</tt> if c is a letter or digit.
         */
        protected boolean isTokenChar(char c) {
            return (Character.isLetter(c) || Character.isDigit(c));
        }
    }
}
