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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings("serial")
public class 
	NullElement
extends
	Element
{
	public final static NullElement NULL_ELEMENT = new NullElement();
	
	public NullElement() 
	{
	}

	public void name( String name )
	{
	}
	
	public String name()
	{
		return null;
	}
	
	public void add( Attribute attribute )
	{
	}
	
	public void remove( Attribute attribute )
	{
	}
	
	public Attribute getAttribute( String name )
	{
		return new NullAttribute();
	}

	public boolean hasChildren()
	{
		return false;
	}
	
	public void serialize( DocumentWriter writer )
		throws IOException
	{
	}

	public String getText()
	{
		return null;
	}
	
	public void add( Content child ) {}

	public int size()
	{
		return 0;
	}

	public void clear() {}

	public Content get( int index )
	{
		return this;
	}
	
	public boolean isEmpty()
	{
		return true;
	}

	public Iterator<Content> iterator()
	{
		// NullIterator
		return new Iterator<Content>()
		{
			public boolean hasNext()
			{
				return false;
			}

			public Content next()
			{
				return null;
			}

			public void remove() {}
		};
	}

	public List<Element> find( String expression )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		return new ArrayList<Element>();
	}

	public Element findFirst( String expression )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		return this;
	}
	
	public int findFirstInteger( String expression )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		return 0;
	}
	
	public boolean findFirstBoolean( String expression )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		return false;
	}
	
}
