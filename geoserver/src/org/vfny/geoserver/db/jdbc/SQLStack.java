/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;


/**
 * Extends a stack to act as a repository for SQL predicate operators
 * and literals.
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class SQLStack extends Stack {


	 /**
		* Empty constructor.
		*/ 
		public SQLStack () {
		}


	 /**
		* Reduces/simplifies stack as much as possible before pushing new string on top.
		*
		* <p>This recursive method examines the top of the stack to determine how many strings are
		* present (vs. operators).  Depending on the number of strings and the presense of
		* operators below them on the stack, the method combines elements to form new, complex
		* strings, which it then places on the top of the stack.</p>
		*
		* <p>The best way to think about this method is as a quasi 'reverse polish notation' approach
		* to the translation of XML to SQL.   Due to the structure of the Filter specification, 
		* this works well to reduce the complexity of the incoming XML tags and transform them
		* into nested SQL with minimal coding complexity.</p>
		*
		* @param firstLiteral The string literal to push onto the stack after stack reduction.
		*/ 
		public void reduce(String firstLiteral) {

				String secondLiteral = new String();
				String thirdLiteral = new String();
				String aggregatedLiteral = new String();
				Operator currentOperator = new Operator();

				// check to see if stack has elements
				// and that the top element is a string
				// before recursing
				if( ( !this.empty() )  && ( this.peek().getClass().getName().equals("java.lang.String") ) ) {
						
						// pop off top string from stack
						secondLiteral = ((String) this.pop());
						
						// CASE: THREE ARGUMENT OPERATOR
						// pop off top string, then operator, then combine all three literals and  operator
						if( this.peek().getClass().getName().equals("String") ) {
								thirdLiteral = ((String) this.pop());
								currentOperator = ((Operator) this.pop());
								aggregatedLiteral = "(" + thirdLiteral + " is between " + secondLiteral + " and " + firstLiteral + ")";								
						}	
						
						// CASE: TWO ARGMENT OPERATOR
						// pop off top operator, then combine both literals and operator
						else {
								currentOperator = ((Operator) this.pop());
								aggregatedLiteral = "(" + secondLiteral + currentOperator.getSQL() + firstLiteral + ")";
						}

						// after combining operators and literals, recurse through stack
						this.reduce( aggregatedLiteral );
						
				}
				
				// CASE: FIRST LITERAL
				// push on top of stack and exit
				else {
						this.push( firstLiteral );
				}				
		}
		

	 /**
		* Convenience method to print the class names on the stack at any given point.
		*
		*/ 
		public String printStack() {
				SQLStack tempStack = new SQLStack();
				String returnString = new String();

				while( !this.empty() )
						tempStack.push( this.pop() );
				while( !tempStack.empty() ) {
						returnString = returnString + tempStack.peek().getClass().getName() +  ":";
						this.push ( tempStack.pop() );
				}
				
				return returnString;
		}


	 /**
		* Method to print the top element of the stack or (if no elements) EMPTY keyword.
		*
		*/ 
		public String finalSQL() {
				if( !this.empty() )
						return this.peek().toString();
				else
						return "";
		}

}
