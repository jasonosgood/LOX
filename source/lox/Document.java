/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class 
	Document
extends
	ArrayList<Content>
{
	private DocType _doctype;
	
	public DocType doctype()
	{
		return _doctype;
	}
	
	public void doctype( DocType doctype )
	{
		if( getRoot() != null )
		{
			throw new IllegalStateException( "DocType must be set before RootElement is added" );
		}
		
		// Allow null doctype
		_doctype = doctype;
	}
	
	public boolean add( Content root )
	{
		if( root == null )
		{
			throw new NullPointerException( "rootable" );
		}
		
		return super.add( root );
	}
	
	public Element getRoot()
	{
		Element root = null;
		
		for( Content node : this )
		{
			if( node instanceof Element )
			{
				root = (Element) node;
				break;
			}
		}
		
		return root;
	}

	public String toString()
	{
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter( sw );
		try
		{
			serialize( xw );
			xw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace( new PrintWriter( sw ));
		}
		return sw.toString(); 
	}

	/**
	 * Serialize this Document instance, and all its children, to the provide XMLWriter. Then calls
	 * XMLWriter.flush().
	 *
	 * @param writer
	 * @throws IOException
	 */
	public void serialize( DocumentWriter writer )
		throws IOException
	{
		writer.document();
		DocType doctype = doctype();
		if( doctype != null )
		{
			doctype.serialize( writer );
		}
	
		for( Content node : this )
		{
			node.serialize( writer );
		}
		writer.close();
	}

	public List<Element> find( String expression )
	{
		Element root = getRoot();
		Element fake = new Element();
		fake.add( root );
		return fake.find( expression );
	}
	
	public Element findFirst( String expression )
	{
		Element root = getRoot();
		Element fake = new Element();
		fake.add( root );
		return fake.findFirst( expression );
	}
}
