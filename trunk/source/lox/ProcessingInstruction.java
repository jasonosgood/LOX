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
	ProcessingInstruction
extends
	Content
{
	private String _target;
	private String _data;
	
	public ProcessingInstruction( String target, String data )
	{
		target( target );
		data( data );
	}
	
	public void target( String target )
	{
		if( target == null )
		{
			throw new NullPointerException( "target" );
		}
		
		String temp = target.trim();
		
		if( temp.length() < 0 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		_target = temp;
	}
	
	public String target()
	{
		return _target;
	}
	
	public void data( String data )
	{
		if( data == null )
		{
			throw new NullPointerException( "data" );
		}
		
		String temp = data.trim();
		
		if( temp.length() < 0 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		_data = temp;
	}
	
	public String data()
	{
		return _data;
	}

	public void serialize( DocumentWriter writer ) 
		throws IOException
	{
		writer.pi( target(), data() );
	}
}
