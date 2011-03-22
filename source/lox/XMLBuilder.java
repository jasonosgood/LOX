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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;


/**
 * XMLBuilder "builds" an XML document, directly. The output is written to a
 * XMLWriter. There is no in memory representation of the XML document.
 * 
 * XMLBuilder uses a stack data structure to track internal state.
 * 
 * 
 * @author Jason Aaron Osgood
 *
 */
//TODO: Verify root element and doctype have same root name

public class
	XMLBuilder
{
	private XMLWriter _writer;
	private Stack<String> _stack;
	private Checker _checker = NullChecker.NULL;
	
	private boolean _document = false;
	private boolean _doctype = false;
	private boolean _children = false;
	private boolean _tailed = false;
	

	public XMLBuilder( OutputStream out )
	{
		this( new PrintWriter( out ));
	}
	
	public XMLBuilder( Writer writer )
	{
		_writer = new XMLWriter( writer );
		_stack = new Stack<String>();
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
	}
	
	public void attribute( String name, String value )
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
		_writer.text( value );
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
	
	private void bracket()
		throws IOException
	{
		String top = _stack.peek();
		if( top != null )
		{
			if( !_tailed )
			{
				_writer.elementStart( _children );
				_tailed = true;
			}
		}
	}
	
	public void pop()
		throws IOException
	{
		String top = _stack.pop();
		if( top != null )
		{
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
