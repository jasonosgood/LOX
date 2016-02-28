/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

public class 
	NullChecker 
implements 
	Checker
{
	public static final Checker NULL = new NullChecker();
	
	private NullChecker() {}
	
	public void doctype( String name, String systemID, String publicID ) throws LOXException {}
	
	public void element( String name ) throws LOXException {}
	
	public void attribute( String name ) throws LOXException {}
	
	public void pi( String target, String data ) throws LOXException {}
	
	public void cdata( String value ) throws LOXException {}

}
