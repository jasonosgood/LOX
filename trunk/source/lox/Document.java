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
//		writer.flush();
		writer.close();
	}

	public List<Element> find( String expression )
	{
		Element fakeRoot = new Element( "fakeRoot" );
		fakeRoot.add( getRoot() );
		return fakeRoot.find( expression );
	}
}
