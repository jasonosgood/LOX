/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class 
	DocType
{
	private String _rootName;
	private String _systemID;
	private String _publicID;	
	
	public DocType( String rootName, String systemID, String publicID )
	
	{
		rootName( rootName );
		systemID( systemID );
		if( publicID != null )
		{
			publicID( publicID );
		}
	}
	
	public DocType( String rootName, String systemID )
	{
		rootName( rootName );
		systemID( systemID );
	}
	
	public void rootName( String rootName )
	{
		if( rootName == null )
		{
			throw new NullPointerException( "rootName" );
		}
		
		String temp = rootName.trim();
		
		if( temp.length() < 1 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		
		_rootName = temp;
	}
	
	public String rootName()
	{
		return _rootName;
	}
	
	public void systemID( String systemID )
	{
		if( systemID == null )
		{
			throw new NullPointerException( "systemID" );
		}
		
		String temp = systemID.trim();
		
		if( temp.length() < 1 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		
		_systemID = temp;
	}
	
	public String systemID()
	{
		return _systemID;
	}
	
	public void publicID( String publicID )
	{
		if( publicID == null )
		{
			throw new NullPointerException( "publicID" );
		}
		
		String temp = publicID.trim();
		
		if( temp.length() < 1 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		_publicID = temp;
	}
	
	public String publicID()
	{
		return _publicID;
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

	public void serialize( DocumentWriter writer )
		throws IOException
	{
		writer.doctype( rootName(), systemID(), publicID() );
	}
		
	public static DocType HTML20 = new DocType( "HTML", "-//IETF//DTD HTML//EN" );
//	public static DocType HTML32 = new DocType( "HTML", "-//W3C//DTD HTML 3.2 Final//EN" );
	public static DocType SVG10 = new DocType( "svg", "-//W3C//DTD SVG 1.0//EN", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd" );
/*
	int    HTML401_TRANSITIONAL         = 2;
	String HTML401_TRANSITIONAL_NAME    = "HTML";
	String HTML401_TRANSITIONAL_SYSTEM  = "-//W3C//DTD HTML 4.01 Transitional//EN";
	String HTML401_TRANSITIONAL_PUBLIC  = "http://www.w3.org/TR/html4/loose.dtd";

	int    HTML401_FRAMESET             = 3;
	String HTML401_FRAMESET_NAME        = "HTML";
	String HTML401_FRAMESET_SYSTEM      = "-//W3C//DTD HTML 4.01 Frameset//EN";
	String HTML401_FRAMESET_PUBLIC      = "http://www.w3.org/TR/html4/frameset.dtd";

	int    HTML401_STRICT               = 4;
	String HTML401_STRICT_NAME          = "HTML";
	String HTML401_STRICT_SYSTEM        = "-//W3C//DTD HTML 4.01//EN";
	String HTML401_STRICT_PUBLIC        = "http://www.w3.org/TR/html4/strict.dtd";

*/
	
}
