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
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Our own Stack implementation.  This one isn't synchronized.
 * 
 * @author Jason Aaron Osgood
 */

@SuppressWarnings("serial")
public class 
	Stack
extends 
	ArrayList<Element>
{
	public Stack() {}

	public Element push( Element item )
	{
		add( item );
		return item;
	}

	public Element pop()
	{
		Element top;
		int len = size();
		top = peek();
		remove( len - 1 );
		return top;
	}

	public Element peek()
	{
		int len = size();
		if( len == 0 ) return null;
		return get( len - 1 );
	}
	
	public Element peek( int nth )
	{
		int len = size();
		if( len == 0 ) return null;
		return get( len - 1 - nth );
	}
	
	public Element peek( String name )
	{
		for( int nth = size() - 1; nth != 0; nth-- )
		{
			Element peek = get( nth );
			if( name.equalsIgnoreCase( peek.name() ))
			{
				return peek;
			}
		}
		return null;
	}
	
	public boolean empty()
	{
		return size() == 0;
	}
	
	static class Spec
	{
		Spec( Spec spec ) { this.next = spec; }
		String tag = null;
		String key = null;
		String value = null;
		Spec next = null;

		int size() 
		{ 
			return 1 + ( next == null ? 0 : next.size() ); 
		}
		
		public String toString()
		{
			return ( next == null ? "" : next.toString() + "/" ) + tag;
		}
	}
	
	public boolean match( String expression )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		Spec head = null;
		for( String atom : expression.split( "/" ))
		{
			atom = atom.trim();
			if( "".equals( atom )) continue;
	
			head = new Spec( head );
			
			if( "**".equals( atom ))
			{
				head.tag = atom;
			}
			else
			{
				// matches "tag[key=value]"
				Pattern pattern = Pattern.compile( "(\\w+|\\*)(\\[((\\w+)(\\:\\w+)*)(\\=(\\w+))?\\])?" );
				Matcher matcher = pattern.matcher( atom );
				if( matcher.find() )
				{
					head.tag = matcher.group( 1 );
					head.key = matcher.group( 3 );
					head.value = matcher.group( 7 );
				}
			}
		}
		
		if( head == null ) return true;
		
		int nth = size() - 1;
		
		return match( head, nth, false );
	}
	
	private boolean match( Spec spec, int nth, boolean seeking )
	{
		if( seeking && spec == null ) return true;
		
		if( spec == null && nth < 0 ) return true;
		
		if( spec == null || nth < 0 ) return false;
		
		Element child = this.get( nth );
		boolean match = false;
		
		if( "*".equals( spec.tag ) || child.name().equalsIgnoreCase( spec.tag ))
		{
			if( spec.key == null )
			{
				match = true;
			}
			else
			{
				if( child.hasAttribute( spec.key ))
				{
					if( spec.value == null )
					{
						match = true;
					}
					else
					{
						String value = child.getAttribute( spec.key ).value().toString();
						if( value.equalsIgnoreCase( spec.value ))
						{
							match = true;
						}
					}
				}
			}
		}
		
		if( match )
		{
//			if( spec.next == null ) return true;
//			if( nth == size() ) return false;
			return match( spec.next, nth - 1, false );
		}
		else if( "**".equals( spec.tag ))
		{
			return match( spec.next, nth, true );
		}
		else if( seeking )
		{
			return match( spec, nth - 1, true );
		}
		return false;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for( Element item : this )
		{
			if( sb.length() > 0 ) sb.append( '/' );
			sb.append( item.name() );
		}
		return sb.toString();

	}

}