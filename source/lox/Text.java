/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;


public class 
	Text
extends 
	Content
{
	private Object _value;
	
	public Text( Object value )
	{
		value( value );
	}
	
	public void value( Object value )
	{
		if( value == null )
		{
			throw new NullPointerException( "value" );
		}

// TODO: figure out if we need auto trimming and if so, how to control it
		
//		if( text instanceof String )
//		{
//			String temp = ((String) text).trim();
//			if( temp.length() < 1 )
//			{
//				throw new IllegalArgumentException( "empty string not allowed" );
//			}
//		}
		
		_value = value;
	}
	
	public Object value()
	{
		return _value;
	}

	public void serialize( DocumentWriter writer ) 
		throws IOException
	{
		writer.text( value(), true );
	}
}
