package lox.test;

import lox.Checker;
import lox.LOXException;

/**
 * Always throws an exception
 * 
 * @author jasonosgood
 *
 */
public class 
	ExceptionalChecker 
implements 
	Checker
{
	public ExceptionalChecker() {}
	
	public void doctype( String name, String systemID, String publicID ) 
		throws LOXException 
	{
		throw new LOXException();
	}
	
	public void element( String name ) 
		throws LOXException 
	{
		throw new LOXException();
	}
	
	public void attribute( String name ) 
		throws LOXException 
	{
		throw new LOXException();
	}
	
	public void pi( String target, String data ) 
		throws LOXException 
	{
		throw new LOXException();
	}
		
	public void cdata( String value ) 
		throws LOXException 
	{
		throw new LOXException();
	}
	
}
