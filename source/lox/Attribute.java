/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class 
	Attribute
{
	protected Attribute() {}
	
	public Attribute( String key, Object value )
	{
		key( key );
		value( value );
	}
	
	private String _key;
	
	public String key()
	{
		return _key;
	}
	
	public void key( String name )
	{
		_key = name;
	}
	
	private Object _value;

	public void value( Object value )
	{
		_value = value;
	}

	public Object value()
	{
		return _value;
	}
	
	public String valueAsString()
	{
		return _value != null ? _value.toString() : null;
	}

	public String toString()
	{
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter( sw );
		try
		{
			serialize( xw );
			xw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace( new PrintWriter( sw ));
		}
		return sw.toString(); 
	}
	
	public void serialize( XMLWriter writer )
		throws IOException
	{
		writer.attribute( key(), value() );
	}
}
