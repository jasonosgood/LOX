/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

//TODO: Verify root element and doctype have same root name
public class 
	LOXBuilder
{
	private Document _document = null;
	private Stack _stack = null;
	private Checker _checker = NullChecker.NULL;

	private boolean _leaf = false;
	
	
	public LOXBuilder()
	{
		_stack = new Stack();
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
	
	/**
	 * Closes the builder (calls close() method) and returns the Document instance constructed by the LOXBuilder. 
	 * 
	 * @return Document
	 */
	public Document getDocument()
	{
		close();
		return _document;
	}

	public void document()
	{
		if( _document == null )
		{
			_document = new Document();
		}
		else
		{
			throw new IllegalStateException( "Document instance already created" );
		}
	}
	
	/**
	 * Adds a DocType instance to this builder's Document. Adds a Document instance, if needed. 
	 *
	 * @throws IllegalStateException if doctype(...) method is called after Document already has a root Element 
	 * 
	 * @param rootName
	 * @param systemID
	 * @param publicID
	 */
	public void doctype( String rootName, String systemID, String publicID )
		throws LOXException
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
		
		getChecker().doctype( rootName, systemID, publicID );
		
		if( _document == null )
		{
			document();
		}
		
		DocType doctype = new DocType( rootName, systemID, publicID );
		_document.doctype( doctype );
	}

	/**
	 * Creates a child Element instance, pushes it onto the (internal stack), and adds the child instance to the
	 * parent. If the child is the root of the document, a RootElement instance is created instead and added to the
	 * Document instance.
	 * 
	 * @param name
	 */
	public void element( String name )
		throws LOXException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
		getChecker().element( name );

		if( _document == null )
		{
			document();
		}
		
		if( _stack.empty() )
		{
			if( _document.getRoot() == null )
			{
				Element root = new Element( name );
				_document.add( root );
				_stack.push( root );
			}
			else
			{
				throw new IllegalStateException( "Document already has a RootElement" );
			}
		}
		else
		{
			Element parent = _stack.peek();
			if( _leaf )
			{
				_stack.pop();
				_leaf = false;
				element( name );
			}
			else
			{
				Element child = new Element( name );
				_stack.add( child );
				parent.add( child );
			}
		}
	}

	public void leaf( String name )
		throws LOXException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
		getChecker().element( name );
		
		if( _stack.empty() )
		{
			throw new IllegalStateException( "Document root element cannot be a leaf" );
		}
		
		Element parent = _stack.peek();
		if( _leaf )
		{
			_stack.pop();
			_leaf = false;
			leaf( name );
		}
		else
		{
			Element child = new Element( name );
			_stack.add( child );
			parent.add( child );
			_leaf = true;
		}
	}
	
	public void attribute( String name, Object value )
		throws LOXException
	{
		if( name == null )
		{
			throw new NullPointerException( "name" );
		}
		
		getChecker().attribute( name );

		if( _stack.empty() )
		{
			throw new IllegalStateException( "cannot add Attribute directly to Document" );
		}
		Element parent = _stack.peek();
		Attribute attrib = new Attribute( name, value );
		parent.add( attrib );
	}

	public void text( Object text )
	{
		if( _stack.empty() )
		{
			throw new IllegalStateException( "cannot add Text directly to Document" );
		}
		Element parent = _stack.peek();
		Text child = new Text( text );
		parent.add( child );
	}

	public void whitespace( String whitespace )
	{
		if( _stack.empty() )
		{
			throw new IllegalStateException( "cannot add Whitespace directly to Document" );
		}
		Element parent = _stack.peek();
		Whitespace child = new Whitespace( whitespace );
		parent.add( child );
	}

	public void comment( String comment )
	{
		if( comment == null )
		{
			throw new NullPointerException( "comment" );
		}
		if( _stack.empty() )
		{
			if( _document == null )
			{
				document();
			}
			Comment child = new Comment( comment );
			_document.add( child );
		}
		else
		{
			Comment child = new Comment( comment );
			Element parent = _stack.peek();
			parent.add( child );
		}
	}

	public void processingInstruction( String target, String data )
		throws LOXException
	{
		if( target == null )
		{
			throw new NullPointerException( "target" );
		}
		
		getChecker().pi( target, data );
		
		if( _stack.empty() )
		{
			if( _document == null )
			{
				document();
			}
	
			ProcessingInstruction child = new ProcessingInstruction( target, data );
			_document.add( child );
		}
		else
		{
			Element parent = _stack.peek();
			ProcessingInstruction child = new ProcessingInstruction( target, data );
			parent.add( child );
		}
	}

	public void cdata( String value )
		throws LOXException
	{
		getChecker().cdata( value );

		if( _stack.empty() )
		{
			throw new IllegalStateException( "cannot add CDATA directly to Document" );
		}
		Element parent = _stack.peek();
		CDATA child = new CDATA( value );
		parent.add( child );
	}

	public void pop()
	{
		if( !_stack.empty() )
		{
			_stack.pop();
		}
		else
		{
			throw new IllegalStateException( "internal Element stack already empty" );
		}
	}

	/**
	 * Pops all Node instances from the stack, ending processing. Method named "close", versus "done", 
	 * to match the XMLSerializer class.
	 */
	public void close()
	{
		while( !_stack.empty() )
		{
			_stack.pop();
		}
	}
	
	public String toString()
	{
		String result = null;
		if( _document != null )
		{
			result = _document.toString();
		}
		return result;
	}
}
