/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;


public class 
	CDATA
extends 
	Content
{
	private String _value;
	
	public CDATA( String value )
	{
		value( value );
	}
	
	public void value( String value )
	{
		if( value == null )
		{
			throw new NullPointerException( "value" );
		}
		
		String temp = value.trim();
		
		if( temp.length() < 0 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		_value = temp;
	}
	
	public String value()
	{
		return _value;
	}

	public void serialize( DocumentWriter writer )
		throws IOException
	{
		writer.cdata( value() );
	}
}
