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


public class 
	NullAttribute
extends
	Attribute
{
	public NullAttribute()
	{
	}
	
	public String name()
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
