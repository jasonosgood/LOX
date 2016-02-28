/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;


/**
 * XMLBuilder "builds" an XML document, directly. The output is written to a
 * XMLWriter. There is no in memory representation of the XML document; use 
 * LOXBuilder construct an object graph.
 * 
 * XMLBuilder uses a stack data structure to track internal state.
 *
 */
//TODO: Verify root element and doctype have same root name

public class
	XMLBuilder
{
	private XMLWriter _writer;
	private java.util.Stack<String> _stack;
	private Checker _checker = NullChecker.NULL;
	
	private boolean _document = false;
	private boolean _doctype = false;
	private boolean _children = false;
	private boolean _tailed = false;

	public XMLBuilder()
	{
		this( new PrintWriter( System.out ));
	}

	public XMLBuilder( OutputStream out )
	{
		this( new PrintWriter( out ));
	}
	
	public XMLBuilder( Writer writer )
	{
		setWriter( writer );
	}
	
	public void setWriter( Writer writer )
	{
		_writer = new XMLWriter( writer );
		_stack = new java.util.Stack<String>();
	}
	
	public void setChecker( Checker checker )
	{
		if( checker == null )
		{
			_checker = NullChecker.NULL;
		}
		else
		{
			_checker = checker;
		}
	}
	
	public Checker getChecker()
	{
		return _checker;
	}
	
	public void document()
		throws IOException
	{
		if( !_document )
		{
			_writer.document();
			_document = true;
		}
		else
		{
			throw new IllegalStateException( "duplicate header" );
		}
	}


	public void doctype( String rootName, String systemID, String publicID )
		throws IOException
	{
		if( rootName == null )
		{
			throw new NullPointerException( "rootName" );
		}
		if( systemID == null )
		{
			throw new NullPointerException( "systemID" );
		}
		if( publicID == null )
		{
			throw new NullPointerException( "publicID" );
		}
		
		try
		{
			getChecker().doctype( rootName, systemID, publicID );
		}
		catch( LOXException e )
		{
			throw new IOException( e );
		}
		
		if( !_doctype )
		{
			if( !_document )
			{
				document();
			}
			
			_writer.doctype( rootName, systemID, publicID );
			_doctype = true;
		}
		else
		{
			throw new IllegalStateException( "duplicate doctype" );
		}
	}
	
	public void element( String name )
		throws IOException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
		try
		{
			getChecker().element( name );
		}
		catch( LOXException e )
		{
			throw new IOException( e );
		}
		
		if( !_stack.empty() )
		{
			_children = true;
		}
		bracket();
		_writer.elementStart( name );
		_stack.add( name );
		_children = false;
		_tailed = false;
//		// HTML DTD says these elements always require closing tags
//		if( "p".equalsIgnoreCase( name ) || "script".equalsIgnoreCase( name ))
//		{
//			_children = true;
////			_tailed = true;
//		}
	}
	
	public void element( String name, Object text )
		throws IOException
	{
		element( name );
		text( text );
		pop();
	}

	public void attribute( String name, Object value )
		throws IOException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
		try
		{
			getChecker().attribute( name );
		}
		catch( LOXException e )
		{
			throw new IOException( e );
		}
		
		if( !_tailed )
		{
			_writer.attribute( name, value );
		}
		else
		{
			throw new IllegalStateException( "cannot add Attribute to closed Element" );
		}
	}
	
	public void text( Object value )
		throws IOException
	{
		_children = true;
		bracket();
		String top = _stack.peek();
		// TODO: Add "style" and "pre"?
		boolean noescape = "script".equalsIgnoreCase( top );
		_writer.text( value, !noescape );
	}
	
	public void comment( String value )
		throws IOException
	{
		bracket();
		_writer.comment( value );
	}
	
	public void processingInstruction( String target, String data )
		throws IOException
	{
		if( target == null )
		{
			throw new NullPointerException( "target" );
		}
		
		try
		{
			getChecker().pi( target, data );
		}
		catch( LOXException e )
		{
			throw new IOException( e );
		}
		
		bracket();
		_writer.pi( target, data );
	}
	
	public void cdata( String value )
		throws IOException
	{
		try
		{
			getChecker().cdata( value );
		}
		catch( LOXException e )
		{
			throw new IOException( e );
		}
		
		bracket();
		_writer.cdata( value );
	}
	
	public void whitespace( String value )
		throws IOException
	{
		_writer.whitespace( value );
	}
	
	private void bracket()
		throws IOException
	{
		// Fucking stupid Stack implementation
		if( !_stack.isEmpty() )
		{
			if( !_tailed )
			{
				_writer.elementStart( _children );
				_tailed = true;
			}
		}
//		
//		String top = _stack.peek();
//		if( top != null )
//		{
//			if( !_tailed )
//			{
//				_writer.elementStart( _children );
//				_tailed = true;
//			}
//		}
	}
	
	public void pop()
		throws IOException
	{
		String top = _stack.pop();
		if( top != null )
		{
			// HTML DTD says these elements always require closing tags
			// TODO: Complete list of tags
			if( "p".equalsIgnoreCase( top ) || "script".equalsIgnoreCase( top ))
			{
				_children = true;
				bracket();
			}
			
			if( _children )
			{
				_writer.elementEnd( top );
			}
			else
			{
				_writer.elementStart( false );
				_tailed = true;
			}
			_children = true;
		}
		
		_writer.flush();
	}
	
	public void close()
		throws IOException
	{
		while( !_stack.empty() )
		{
			pop();
		}
		_writer.close();
	}
}
