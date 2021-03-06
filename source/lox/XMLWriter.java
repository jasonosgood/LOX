/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.Format;
import java.util.HashMap;

/**
 * XMLWriter wraps a Writer. XMLWriter takes care of escaping characters, encoding, 
 * platform (operating system) differences, and pretty printing.
 * 
 * @author Jason Aaron Osgood
 *
 */

/*
 when to not escape HTML

script
pre
code
style

http://www.javapractices.com/topic/TopicAction.do?Id=96
 */

//TODO: Support formatters
public class 
	XMLWriter 
extends 
	Writer
implements
	DocumentWriter
{
	protected Writer _writer;
	
	public XMLWriter( Writer writer )
	{
		_writer = writer;
	}
	
	public XMLWriter( OutputStream out )
	{
		this( new OutputStreamWriter( out ));
	}
	
	@Override
	public void close() 
		throws IOException
	{
//		_writer.close();
	}

	@Override
	public void flush() 
		throws IOException
	{
		_writer.flush();
	}

	@Override
	public void write( char[] arg0, int arg1, int arg2 ) 
		throws IOException
	{
		_writer.write( arg0, arg1, arg2 );
	}
	
	private boolean _pretty = false;
	
	public void setPretty( boolean pretty ) 
	{ 
		_pretty = pretty; 
	}
	
	public boolean getPretty() 
	{ 
		return _pretty; 
	}
	
	public static final String TAB = "\t";
	public static final String SPACES = "  ";

	private String _tab = SPACES;
	
	public void setTab( String tab )
	{
		_tab = tab;
	}
	
	public String getTab()
	{
		return _tab;
	}

	
	public enum NewLine
	{
		DOS
		{ 
			void write( Writer w ) throws IOException{
				w.write( '\r' );
				w.write( '\n' );
			}
		}, 
		
		MAC
		{ 
			void write( Writer w ) throws IOException {
				w.append( '\r' );
			}
		}, 

		UNIX
		{ 
			void write( Writer w ) throws IOException {
				w.append( '\n' );
			}
		}, 

		GAP
		{ 
			void write( Writer w ) throws IOException {
				w.append( ' ' );
			}
		}, 

		NONE
		{ 
			void write( Writer w ) throws IOException {}
		};
		
		abstract void write( Writer w ) throws IOException;
	}

	private NewLine _newLine = NewLine.DOS;

	public void setNewLine( NewLine newLine )
	{
		_newLine = newLine;
	}
	
	public NewLine getNewLine()
	{
		return _newLine;
	}
	
	protected void newline()
		throws IOException
	{
		if( !getPretty() ) return;
		
		getNewLine().write( _writer );
	}

	protected void indent()
		throws IOException
	{
		if( getPretty() )
		{
			for( int nth = 0; nth < _indent; nth++ )
			{
				_writer.write( getTab() );
			}
		}
	}
	
	/**
	 * Write the XML document header.
	 * 
	 * @throws IOException
	 */
	public void document()
		throws IOException
	{
		_writer.write( "<!DOCTYPE html>" );
//		_writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
		newline();
	}
	
	public void doctype( String name, String systemID, String publicID )
		throws IOException
	{
//		if( isNotNull( name) && isNotNull( systemID ))
//		{
//			_writer.write( "<!DOCTYPE " );
//			_writer.write( name );
//			_writer.write( " PUBLIC \"" );
//			_writer.write( systemID );
//			_writer.write( '\"' );
//			
//			if( publicID != null )
//			{
//				_writer.write( " \"" );
//				_writer.write( publicID );
//				_writer.write( '\"' );
//			}
//			
//			_writer.write( '>' );
//			newline();
//		}
	}
	
	private int _indent = -1;
	
	public void elementStart( String name )
		throws IOException
	{
		stillText = false;
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		_indent++;
		indent();
		_writer.write( '<' );
		_writer.write( name );
	}

	public void elementStart( boolean children )
		throws IOException
	{
		stillText = false;
		if( !children )
		{
			_indent--;
			_writer.write( '/' );
		}
		_writer.write( '>' );
		newline();
		
	}

	public void elementEnd( String name )
		throws IOException
	{
		stillText = false;
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		indent();
		_writer.write( '<' );
		_writer.write( '/' );
		_writer.write( name );
		_writer.write( '>' );
		newline();
		_indent--;
	}
	
	public void attribute( String name, Object value )
		throws IOException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		if( value == null )
		{
			throw new NullPointerException( "value" );
		}
		_writer.write( ' ' );
		_writer.write( name );
		_writer.write( '=' );
		_writer.write( '\"' );
		String temp = format( value );
		escape( temp );
		_writer.write( '\"' );
	}
	
	public void attribute( String name )
		throws IOException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		_writer.write( ' ' );
		_writer.write( name );
	}
		
	boolean stillText = false;
	
	public void text( Object text, boolean escape )
		throws IOException
	{
		if( !stillText )
		{
			_indent++;
			indent();
		}
		String temp = format( text );
		if( escape )
		{
			escape( temp );
		}
		else
		{
			_writer.write( temp );
		}
		if( !stillText )
		{
//			newline();
			_indent--;
		}
		stillText = true;
	}

	protected void escape( String text ) 
		throws IOException
	{
		String temp = text.toString();
		char[] ca = temp.toCharArray();
		int size = ca.length;
		for( int nth = 0; nth < size; nth++ )
		{
			char c = ca[nth];
			switch( c )
			{
				case '\t':
					if( !getPretty() )
					{
//						_writer.write( "&#x09;" );
						_writer.write( c );
					}
					break;
					
				case '\n':
					if( !getPretty() )
					{
//						_writer.write( "&#x0A;" );
						_writer.write( c );
					}
					break;
				
				case '\r':
					if( !getPretty() )
					{
//						_writer.write( "&#x0D;" );
						_writer.write( c );
					}
					break;
					
				// Not allowed, eat these characters
	            case 14:
	            case 15:
	            case 16:
	            case 17:
	            case 18:
	            case 19:
	            case 20:
	            case 21:
	            case 22:
	            case 23:
	            case 24:
	            case 25:
	            case 26:
	            case 27:
	            case 28:
	            case 29:
	            case 30:
	            case 31:
	                break;
	                
				case '&':
					_writer.write( "&amp;" );
					break;
	
				case '<':
					_writer.write( "&lt;" );
					break;
					
				case '>':
					_writer.write( "&gt;" );
					break;
					
				case '\"':
					_writer.write( "&quot;" );
					break;
				
				case '\'':
					_writer.write( "&apos;" );
					break;
					
				default:
					_writer.write( c );
					break;
			}
		}
	}
	
	public void whitespace( String value )
		throws IOException
	{
		if( !getPretty() )
		{
			_writer.write( value );
		}
	}
	
	public void comment( String value )
		throws IOException
	{
		stillText = false;
		_indent++;
		indent();
		_writer.write( "<!-- " );
		_writer.write( value );
		_writer.write( " -->" );
		newline();
		_indent--;
	}
	
	public void pi( String target, String data )
		throws IOException
	{
		stillText = false;
		_writer.write( "<?" );
		_writer.write( target );
		_writer.write( ' ' );
		_writer.write( data );
		_writer.write( "?>" );
		newline();
	}
	
	public void cdata( String value )
		throws IOException
	{
		stillText = false;
		_writer.write( "<![CDATA[ " );
		_writer.write( value );
		_writer.write( " ]]>" );
		newline();
	}
	
	private HashMap<Class<?>,Format> _formats;
	
	public void addFormat( Class<?> clazz, Format format )
	{
		if( clazz == null )
		{
			throw new NullPointerException( "clazz" );
		}
		
		if( format == null )
		{
			throw new NullPointerException( "format" );
		}
		
		if( _formats == null )
		{
			_formats = new HashMap<Class<?>,Format>();
		}
		
		_formats.put( clazz, format );
	}
	
	public HashMap<Class<?>,Format> getFormats()
	{
		return _formats;
	}
	
	protected String format( Object value )
	{
		if( value == null )
		{
			return "null";
		}
		
		if( _formats != null )
		{
			Class<? extends Object> clazz = value.getClass();
			Object temp = _formats.get( clazz );
			if( temp != null )
			{
				Format format = (Format) temp;
				String result = format.format( value );
				return result;
			}
		}

		String result = value.toString();
		return result;
	}
	
	public static boolean isNull( String value )
	{
		if( value == null ) return true;
		if( value.length() < 1 ) return true;
		return false;
	}
	
	public static boolean isNotNull( String value )
	{
		return !isNull( value );
	}
}
