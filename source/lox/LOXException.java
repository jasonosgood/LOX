/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

public class LOXException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public LOXException() {}
	
	public LOXException( String message )
	{
		super( message );
	}

	public LOXException( Throwable cause, String message )
	{
		super( message );
		initCause( cause );
	}

}
