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
	private static LinkedList<Attribute> NULL_ATTRIBUTES = new LinkedList<Attribute>();
	
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
	
	public Element get( String name )
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
//		boolean spaced = name.indexOf( ':' ) > -1;
		
		Element result = null;
		for( Content node : this )
		{
			if( node instanceof Element )
			{
				Element element = (Element) node;
				String other = element.name();
				if( name.equals( other ))
				{
					result = element;
					break;
				}
				
//				if( !spaced )
//				{
//					int oof = other.indexOf( ':' );
//					if( name.equals( other.substring( oof + 1 )))
//					{
//						result = element;
//						break;
//					}
//				}
			}
		}
		return result;
	}
	
	public List<Element> getAll( String name )
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
		ArrayList<Element> result = new ArrayList<Element>();
		for( Content node : this )
		{
			if( node instanceof Element )
			{
				Element element = (Element) node;
				if( name.equals( element.name() ))
				{
					result.add( element );
				}
			}
		}
		return result;
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

	public String toContent()
	{
		StringBuilder sb = new StringBuilder();
		toContent( sb );
		return sb.toString();
	}
	
	protected void toContent( StringBuilder sb )
	{
		for( Content child : this )
		{
			if( child instanceof Element )
			{
				((Element) child).toContent( sb );
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

	public boolean add( Content child )
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
		
		child.setParent( this );
		
		return true;
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
			_children[nth].setParent( null );
			// We're told that nulling pointers helps the garbage collector
			_children[nth] = null;
		}
		_children = null;
		_size = 0;
	}

	public Content get( int index )
	{
		if( index < 0 || index >= _size )
		{
			throw new  IndexOutOfBoundsException( "index: " + index + ", size: " + _size );
		}
		return _children[index];
	}
	
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

	public Element findFirst( String expression )
	{
		Element result = null;
		List<Element> found = find( expression );
		if( found.size() > 0 ) result = found.get( 0 );
		return result;
	}
	
	public List<Element> find( String expression )
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
		
		Stack<Element> path = new Stack<Element>();
		ArrayList<Element> result = new ArrayList<Element>();
		crawl( this, path, query, result );
		return result;
	}

	public void crawl( Element parent, Stack<Element> path, ArrayList<String> query, ArrayList<Element> result )
	{
		for( Content content : parent )
		{
			if( content instanceof Element )
			{
				Element element = (Element) content;
				path.push( element  );
				if( match( path, query ))
				{
					result.add( (Element) content );
				}
				else if( content instanceof Element )
				{
					crawl( (Element) content, path, query, result );
				}
				path.pop();
			}
			
		}
	}

	private boolean match( Stack<Element> pathList, ArrayList<String> queryList ) 
	{
		if( pathList.size() != queryList.size() ) return false;
		
		Iterator<Element> path = pathList.iterator();
		Iterator<String> query = queryList.iterator();
		while( path.hasNext() && query.hasNext() )
		{
			String name = path.next().name();
			String temp = query.next();
			if( !name.equalsIgnoreCase( temp )) return false;
		}
		
		return true;
	}
}
