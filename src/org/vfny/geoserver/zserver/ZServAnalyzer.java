package org.vfny.geoserver.zserver;

import java.io.Reader;
import org.apache.lucene.analysis.*;

/** An Analyzer that filters LetterTokenizer with LowerCaseFilter. */

public final class ZServAnalyzer extends Analyzer {
  public final TokenStream tokenStream(String fieldName, Reader reader) {
    return new NumberLowerCaseTokenizer(reader);
  }

    private class NumberLowerCaseTokenizer extends CharTokenizer {
	 /** Construct a new LetterTokenizer. */
	public NumberLowerCaseTokenizer(Reader in) {
	    super(in);
	}
	
	/** puts chars to lower case. */
	protected char normalize(char c) {
	    return Character.toLowerCase(c);
	}
	
	/** Collects only characters which satisfy
	 * {@link Character#isLetter(char)} or {@link Character#isDigit(char)}.*/
	protected boolean isTokenChar(char c) {
	    return (Character.isLetter(c) || Character.isDigit(c));
	}
    }

}
