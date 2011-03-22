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

import java.util.ArrayList;

/**
 * Our own Stack implementation.  This one isn't synchronized.
 * 
 * @author Jason Aaron Osgood
 */

@SuppressWarnings("serial")
public class 
	Stack<E>
extends 
	ArrayList<E>
{
	public Stack() {}

	public E push( E item )
	{
		add( item );
		return item;
	}

	public E pop()
	{
		E top;
		int len = size();
		top = peek();
		remove( len - 1 );
		return top;
	}

	public E peek()
	{
		int len = size();
		if( len == 0 ) return null;
		return get( len - 1 );
	}
	
	public E peek( int nth )
	{
		int len = size() - 1;
		return get( len - nth );
	}

	public boolean empty()
	{
		return size() == 0;
	}

	public int search( E o )
	{
		int i = lastIndexOf( o );

		if( i >= 0 )
		{
			return size() - i; 
		}
		
		return -1;
	}
}