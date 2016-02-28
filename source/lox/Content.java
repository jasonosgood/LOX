/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Abstract class Content, subclassed by all content classes.
 **/

public abstract class 
	Content
{
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

	public abstract void serialize( DocumentWriter writer ) 
		throws IOException;
}
