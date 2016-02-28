/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;


//TODO: Comments cannot contain double hyphen "--"
//TODO: Comments cannot contain carriage returns '\n'
//TODO: Comments cannot contain end with "-"

public class 
	Comment
extends 
	Content
{
	private String _value;
	
	public Comment( String value )
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
		writer.comment( value() );
	}
}
