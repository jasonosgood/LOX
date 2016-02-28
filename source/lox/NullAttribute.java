/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class 
	NullAttribute
extends
	Attribute
{
	public NullAttribute()
	{
	}
	
	public String key()
	{
		return null;
	}
	
	public Object value()
	{
		return null;
	}

	public String toString()
	{
		return null; 
	}
	
	public void serialize( XMLWriter writer )
		throws IOException
	{
	}
}
