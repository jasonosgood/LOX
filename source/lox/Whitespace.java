/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;


public class 
	Whitespace
extends 
	Content
{
	private String _value;
	
	public Whitespace( String value )
	{
		value( value );
	}
	
	public void value( String value )
	{
		if( value == null )
		{
			throw new NullPointerException( "value" );
		}

		_value = value;
	}
	
	public String value()
	{
		return _value;
	}

	public String toString()
	{
		return value();
	}
	
	public void serialize( DocumentWriter writer ) 
		throws IOException
	{
		writer.whitespace( value() );
	}
}
