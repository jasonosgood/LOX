/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

public interface 
	Checker
{
	void doctype( String rootName, String systemID, String publicID ) throws LOXException;
	
	void element( String name ) throws LOXException;
	
	void attribute( String name ) throws LOXException;
	
	void pi( String target, String data ) throws LOXException;
	
	void cdata( String value ) throws LOXException;
}
