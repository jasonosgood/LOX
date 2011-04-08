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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


// TODO: Add a "list modified?" check for the iterator 
// TODO: What's an ideal/optimal value for the increment function? Check page/block allocation w/ byte boundaries, etc.
@SuppressWarnings("serial")
public class 
	Element
extends
	Content
implements
	Iterable<Content>
{
	protected Element() {}
	
	public Element( String name ) 
	{
		name( name );
	}

	private String _name;
	
	public void name( String name )
	{
		_name = name;
	}
	
	public String name()
	{
		return _name;
	}
	
	private LinkedList<Attribute> _attributes;
	protected static LinkedList<Attribute> NULL_ATTRIBUTES = new LinkedList<Attribute>();
	
	public List<Attribute> attributes()
	{
		return _attributes != null ? _attributes : NULL_ATTRIBUTES;
	}
	
	public void add( Attribute attribute )
	{
		if( _attributes == null )
		{
			_attributes = new LinkedList<Attribute>();
		}
		_attributes.add( attribute );
	}
	
	public void remove( Attribute attribute )
	{
		if( _attributes != null ) 
		{
			_attributes.remove( attribute.name() );
		}
	}
	
	public Attribute getAttribute( String name )
	{
		Attribute result = null;
		
		if( _attributes != null )
		{
			int nth = _attributes.indexOf( name );
			result = _attributes.get( nth );
		}
		
		return result;
	}

	public boolean hasChildren()
	{
		return size() > 0;
	}
	
	public void serialize( DocumentWriter writer )
		throws IOException
	{
		String name = name();
		writer.elementStart( name );
		
		for( Attribute attribute : attributes() )
		{
			writer.attribute( attribute.name(), attribute.value() );
		}
		
		writer.elementStart( hasChildren() );
		
		for( Content node : this )
		{
			node.serialize( writer );
		}
		
		if( hasChildren() )
		{
			writer.elementEnd( name );
		}
	}

    /**
     * <p>
     * Returns the value of the element as defined by XPath 1.0.
     * This is the complete PCDATA content of the element, without
     * any tags, comments, or processing instructions after all 
     * entity and character references have been resolved.
     * </p>
     * 
     * @return XPath string value of this element
     * 
     */

	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		getText( sb );
		return sb.toString();
	}
	
	protected void getText( StringBuilder sb )
	{
		for( Content child : this )
		{
			if( child instanceof Element )
			{
				((Element) child).getText( sb );
			}
			else if( child instanceof Text )
			{
				sb.append( ((Text) child).value() );
			}
		}
	}

	private Content[] _children;
	private int _size = 0;
	private int _increment = 5;

	public void add( Content child )
	{
		if( child == null )
		{
			throw new NullPointerException( "child" );
		}
		
		if( _children == null )
		{
			_children = new Content[_increment];
		}
		
		if( _size >= _children.length )
		{
			Content[] temp = new Content[_children.length + _increment];
			System.arraycopy( _children, 0, temp, 0, _children.length );
			_children = temp;
		}
			
		_children[_size++] = child;
		
//		child.setParent( this );
	}
	
	public int size()
	{
		return _size;
	}

	public void clear()
	{
		int size = size();
		for( int nth = 0; nth < size; nth++ )
		{
//			_children[nth].setParent( null );
			// We're told that nulling pointers helps the garbage collector
			_children[nth] = null;
		}
		_children = null;
		_size = 0;
	}

//	public Content get( int index )
//	{
//		if( index < 0 || index >= _size )
//		{
//			throw new  IndexOutOfBoundsException( "index: " + index + ", size: " + _size );
//		}
//		return _children[index];
//	}
	
	public boolean isEmpty()
	{
		return size() == 0;
	}

	public Iterator<Content> iterator()
	{
		return new Iterator<Content>()
		{
			int nth = 0;
			public boolean hasNext()
			{
				return nth < size();
			}

			public Content next()
			{
				return _children[nth++];
			}

			public void remove() {}
		};
	}

	public List<Element> find( String expression )
	{
		return find( expression, false );
	}
	
	// TODO: validate expression
	protected List<Element> find( String expression, boolean first )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		ArrayList<String> query = new ArrayList<String>();
		for( String atom : expression.split( "/" ))
		{
			query.add( atom );
		}
		
		ArrayList<Element> result = new ArrayList<Element>();
		find( first, this, query, 0, false, result );
		return result;
	}

	private void find( boolean first, Element parent, ArrayList<String> query, int nth, boolean seeking, ArrayList<Element> result )
	{
		String spot = query.get( nth );
		for( Content content : parent )
		{
			if( content instanceof Element )
			{
				Element child = (Element) content;
				if( "*".equals( spot ) || child.name().equalsIgnoreCase( spot ))
				{
					if( nth + 1 < query.size() )
					{
						find( first, child, query, nth + 1, false, result );
					}
					else
					{
						result.add( child );
					}
				}
				else if( "**".equals( spot ))
				{
					find( first, child, query, nth + 1, true, result );
				}
				else if( seeking )
				{
					find( first, child, query, nth, true, result );
				}
			}
			
			if( first && result.size() > 0 ) break;
		}
	}

	public Element findFirst( String expression )
	{
		Element result = NullElement.NULL_ELEMENT;
		List<Element> found = find( expression, true );
		if( found.size() > 0 )
		{
			result = found.get( 0 );
		}
		return result;
	}
	
	public String findFirstString( String expression )
	{
		return findFirst( expression ).getText();
	}
	
	public int findFirstInteger( String expression )
		throws LOXException
	{
		String text = findFirst( expression ).getText();
		try
		{
			int result = Integer.parseInt( text );
			return result;
		}
		catch( NumberFormatException e )
		{
			String msg = "expression: " + expression + ", value: " + text; 
			throw new LOXException( e, msg );
		}
	}
	
	public int findFirstInteger( String expression, int defaultValue )
	{
		int result = defaultValue;
		try
		{
			result = findFirstInteger( expression );
		}
		catch( LOXException e ) 
		{
			// Eat it
		}
		return result;
	}

	public boolean findFirstBoolean( String expression )
		throws NumberFormatException
	{
		String text = findFirst( expression ).getText();
		boolean result = Boolean.parseBoolean( text );
		return result;
	}
	
	public double findFirstDouble( String expression )
		throws NumberFormatException
	{
		String text = findFirst( expression ).getText();
		double result = Double.parseDouble( text );
		return result;
	}

	public float findFirstFloat( String expression )
		throws NumberFormatException
	{
		String text = findFirst( expression ).getText();
		float result = Float.parseFloat( text );
		return result;
	}

	// TODO: I don't think this is complete, eg has no timezone or offset
	public final static String XML_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	
	public Date findFirstDate( String expression )
		throws ParseException
	{
		String text = findFirst( expression ).getText().trim();
		String pattern = XML_DATE_PATTERN;
//		// Make sure value and pattern are same length
//		pattern = pattern.substring( 0, text.length() );
//		text = text.substring( 0, pattern.length() );
		SimpleDateFormat sdf = new SimpleDateFormat ( pattern ); 
		Date result = sdf.parse( text );
		return result;
	}
	
	public Date findFirstDate( String expression, Date defaultValue )
	{
		Date result = defaultValue;
		try
		{
			result = findFirstDate( expression );
		}
		catch( ParseException e )
		{
			// Eat it
		}
		return result;
	}


}
