package lox;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Convert SAX events into a LOX object graph.
 * 
 * @author Jason Aaron Osgood
 *
 */
public class 
	LOXHandler
extends
	DefaultHandler
{
	private LOXBuilder _builder = null;
	
	public LOXBuilder getLOXBuilder()
	{
		return _builder;
	}
	
	public Document getDocument()
	{
		return _builder.getDocument();
	}

	
	public static Document load( InputStream in ) 
		throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		LOXHandler handler = new LOXHandler();
		parser.parse( in, handler );
		Document document = handler.getDocument();
		
		return document;
	}
	
	public void startDocument() 
		throws SAXException
	{
		_builder = new LOXBuilder();
	}
	
	public void endDocument() 
		throws SAXException
	{
		_builder.close();
	}

	private String _namespacesFuckingSuck = "";
	
	public void startElement( String namespaceURI, String localName, String qName, Attributes attribs ) 
		throws SAXException
	{
		try
		{
			_builder.element( qName );
			if( attribs != null )
			{
				int count = attribs.getLength();
				for( int nth = 0; nth < count; nth++ )
				{
					String attribName = attribs.getQName( nth );
					String attribValue = attribs.getValue( nth );
					_builder.attribute( attribName, attribValue );
				}
			}
			
			if( !_namespacesFuckingSuck.equals( namespaceURI ))
			{
				if( namespaceURI != null )
				{
					_builder.attribute( "xmlns", namespaceURI );
					_namespacesFuckingSuck = namespaceURI;
				}
			}
				
		}
		catch( LOXException e )
		{
			throw new SAXException( e );
		}

	}
	
	public void endElement( String namespaceURI, String localName, String qName ) 
		throws SAXException
	{
		_builder.pop();
	}

	// TODO: Extract embedded whitespace, not just the front and ends
	// TODO: Decode entities (e.g. &amp; &lt;) back to characters
//	public void characters( char[] ch, int start, int length ) 
//		throws SAXException
//	{
//		int front;
//		front:
//		for( front = start; front < length; front++ )
//		{
//			char c = ch[front];
//			switch( c )
//			{
//				case ' ':
//				case '\t':
//				case '\r':
//				case '\n':
//					break;
//				default:
//					break front;
//			}
//		}
//		int size = front - start;
//		ignorableWhitespace( ch, start, size );
//		
//		int back;
//		back:
//		for( back = length; back > 0; back-- )
//		{
//			char c = ch[back - 1];
//			switch( c )
//			{
//				case ' ':
//				case '\t':
//				case '\r':
//				case '\n':
//					break;
//				default:
//					break back;
//			}
//		}
//		
//		ignorableWhitespace( ch, back, length - back );
//		
//		if( back - front > 0 )
//		{
//			String trim = new String( ch, front, back - front );
//			System.out.println( "characters (trim): '" + trim + "'" );
//			_builder.text( trim );
//		}
//	}
    public void characters( char[] ch, int start, int length ) 
	    throws SAXException
	{
	    int front;
	    front:
	    for( front = start; front - start < length; front++ )
	    {
	            switch( ch[front] )
	            {
	                    case ' ':
	                    case '\t':
	                    case '\r':
	                    case '\n':
	                            break;
	                    default:
	                            break front;
	            }
	    }
	    int size = front - start;
	    ignorableWhitespace( ch, start, size );
	    start += size;
	    length -= size;
	    
	    int back;
	    back:
	    for( back = 0; back < length; back++ )
	    {
	            switch( ch[length - back] )
	            {
	                    case ' ':
	                    case '\t':
	                    case '\r':
	                    case '\n':
	                            break;
	                    default:
	                            break back;
	            }
	    }
	    
	    ignorableWhitespace( ch, start + length - back, back );
	    length -= back;
	    
	    if( length > 0 )
	    {
	            String trim = new String( ch, start, length );
	//          System.out.println( "characters (trim): '" + trim + "'" );
	            _builder.text( trim );
	    }
	}
	public void startPrefixMapping( String prefix, String uri ) 
		throws SAXException
	{
		System.out.printf( "startPrefixMapping prefix: %s, uri: %s\n", prefix, uri );
	}
	
	public void endPrefixMapping( String prefix ) 
		throws SAXException
	{
		System.out.printf( "startPrefixMapping prefix: %s\n", prefix );
	}

	public void ignorableWhitespace( char[] ch, int start, int length ) 
		throws SAXException
	{
		if( length > 0 )
		{
			String temp = new String( ch, start, length );
			_builder.whitespace( temp );
		}
	}

	public void processingInstruction( String target, String data ) 
		throws SAXException
	{
		try
		{
			_builder.processingInstruction( target, data );
		}
		catch( LOXException e )
		{
			throw new SAXException( e );
		}
	}

	Locator _locator = null;
	public void setDocumentLocator( Locator locator )
	{
		System.out.println( "setDocumentLocator" + locator );
//		_locator = locator;
//		throw new NullPointerException( "setDocumentLocator" );
	}

	public void skippedEntity( String name ) 
		throws SAXException
	{
		System.out.printf( "skippedEntity name: %s\n", name );
//		throw new NullPointerException( "skippedEntity" );
	}

	public void notationDecl( String arg0, String arg1, String arg2) throws SAXException
	{
		System.out.println( "notationDecl" );
//		throw new NullPointerException( "notationDecl" );
		
	}

	public void unparsedEntityDecl( String arg0, String arg1, String arg2, String arg3) throws SAXException
	{
		System.out.println( "unparsedEntityDecl" );
//		throw new NullPointerException( "unparsedEntityDecl" );
	}

	public InputSource resolveEntity( String arg0, String arg1) throws SAXException, IOException
	{
		System.out.println( "resolveEntity" );
//		throw new NullPointerException( "reolveEntity" );
		return null;
	}

	public void error( SAXParseException arg0) throws SAXException
	{
		System.out.println( "error" );
//		throw new NullPointerException( "error" );
		
	}

	public void fatalError( SAXParseException arg0) throws SAXException
	{
		System.out.println( "fatalError" );
//		throw new NullPointerException( "fatalError" );
		
	}

	public void warning( SAXParseException arg0) throws SAXException
	{
		System.out.println( "warning" );
//		throw new NullPointerException( "warning" );
		
	}
}
