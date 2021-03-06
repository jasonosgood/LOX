/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Add a "list modified?" check for the iterator 
// TODO: Add iterator for just child elements
@SuppressWarnings("serial")
public class 
	Element
extends
	Content
implements
	Iterable<Content>, Comparable<Element>
{
	private static int __counter = 0;
	private int _nth = __counter++;
	
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
	
	public int compareTo( Element that )
	{
		return Integer.compare( this._nth, that._nth );
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
	
	public Element addAttribute( String key, Object value )
	{
		Attribute attrib = new Attribute( key, value );
		add( attrib);
		return this;
	}
	
	public void remove( Attribute attribute )
	{
		if( _attributes != null ) 
		{
			_attributes.remove( attribute.key() );
		}
	}
	
	public String getID()
	{
		return getAttributeValue( "id" );
	}
	
	public Attribute getAttribute( String key )
	{
		if( key == null )
		{
			throw new NullPointerException( "name" );
		}
		if( _attributes == null ) return null;
		for( Attribute attrib : _attributes )
		{
			if( key.equalsIgnoreCase( attrib.key()) )
			{
				return attrib;
			}
				
		}
		return null;
	}

	public boolean hasAttribute( String key )
	{
		boolean result = getAttribute( key ) != null;
		return result;
	}

	public String getAttributeValue( String key )
	{
		return getAttributeValue( key, null );
	}
	
	public String getAttributeValue( String key, String missing )
	{
		if( key == null )
		{
			throw new NullPointerException( "key" );
		}
		
		Attribute attribute = getAttribute( key );
		if( attribute == null ) return missing;
		
		Object value = attribute.value();
		if( value == null ) return missing;
		
		return value.toString();
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
			writer.attribute( attribute.key(), attribute.value() );
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
		return getText( true );
	}
	
	public String getText( boolean children )
	{
		StringBuilder sb = new StringBuilder();
		getText( sb, children );
		return sb.toString();
	}
	
	protected void getText( StringBuilder sb, boolean children )
	{
		for( Content child : this )
		{
			if( child instanceof Element && children )
			{
				((Element) child).getText( sb, true );
			}
			else if( child instanceof Text )
			{
				sb.append( ((Text) child).value() );
			}
			else if( child instanceof Whitespace )
			{
				sb.append( child.toString() );
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
		
		if( child instanceof NullElement ) return;
		
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
			// We're told that nulling pointers helps the garbage collector
			_children[nth] = null;
		}
		_children = null;
		_size = 0;
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

	public List<Element> find( String expression )
	{
		List<Element> list = find( expression, false );
		return list;
	}
	
	static class Spec
	{
		String tag = null;
		String key = null;
		String value = null;
	}

	// matches "tag[key=value]"
	static Pattern pattern = Pattern.compile( "(\\w+|\\*)(\\[((\\w+)(\\:\\w+)*)(\\=(\\w+))?\\])?" );

	// TODO: validate expression
	protected List<Element> find( String expression, boolean first )
	{
		if( expression == null )
		{
			throw new NullPointerException( "expression" );
		}
		
		ArrayList<Element> result = new ArrayList<>();

		for( String clause : expression.split( "\\|" ))
		{
			ArrayList<Spec> query = new ArrayList<Spec>();
			for( String atom : clause.split( "/" ))
			{
				atom = atom.trim();
				if( "".equals( atom )) continue;
				Spec spec = new Spec();
				if( "**".equals( atom ))
				{
					spec.tag = atom;
					query.add( spec );
				}
	//			else if( "..".equals( atom ))
	//			{
	//				spec.tag = atom;
	//				query.add( spec );
	//			}
				else
				{
					Matcher matcher = pattern.matcher( atom );
					if( matcher.find() )
					{
						spec.tag = matcher.group( 1 );
						spec.key = matcher.group( 3 );
						spec.value = matcher.group( 7 );
						query.add( spec );
					}
				}
			}
			
			find( first, this, query, 0, false, result );
		}

		// FIXME: This can't be right, right? Query is recursive descent...
		// TODO: Maybe Replace _nth & comparator with iterative path expression
		// evaluator which returns elements in document order
		Collections.sort( result );

		return result;
	}

	private void find( boolean first, Element parent, ArrayList<Spec> query, int nth, boolean seeking, ArrayList<Element> result )
	{
		Spec spec = query.get( nth );
		if( "**".equals( spec.tag ))
		{
			find( first, parent, query, nth + 1, true, result );
		}
		for( Content content : parent )
		{
			if( content instanceof Element )
			{
				Element child = (Element) content;
				boolean match = false;
				
				if( "*".equals( spec.tag ))
				{
					match = true;
				}
				else 
				{
					if( child.name().equalsIgnoreCase( spec.tag ))
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
				}
				
				if( match )
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
				else if( seeking )
				{
					find( first, child, query, nth, true, result );
				}
			}
			
			if( first && result.size() > 0 ) break;
		}
	}

	/**
	 * Either returns first matching Element or NullElement.
	 * 
	 * @param expression
	 * @return
	 */
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

	public boolean isNull() { return false; }
}
