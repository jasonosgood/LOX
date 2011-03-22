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
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Interface Content is subclassed by all of LOX's XML content classes.
 * 
 * @author Jason Aaron Osgood
 **/

public abstract class 
	Content
{
	private Element _parent = null;
	
	void setParent( Element parent )
	{
		_parent = parent;
	}
	
	public Element getParent()
	{
		return _parent; 
	}
	
	public final String toString()
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
