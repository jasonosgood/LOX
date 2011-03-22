/* Copyright 2007 Jason Aaron Osgood
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 2.1 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the 
   Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
   Boston, MA 02111-1307  USA
   
   Jason Osgood
   jason@jasonosgood.com
   http://code.google.com/p/lox/
   
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
		writer.text( value() );
	}
}
