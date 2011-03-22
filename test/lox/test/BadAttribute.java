package lox.test;

import java.io.IOException;

import junit.framework.TestCase;
import lox.XMLBuilder;

public class 
	BadAttribute 
extends 
	TestCase
{
	public void testBadAttribute()
		throws IOException
	{
		try
		{
			XMLBuilder tops = new XMLBuilder( System.out );
			tops.element( "root" );
			tops.comment( "comment" );
			tops.attribute( "name", "value" );
			fail( "failed" );
		}
		catch( IllegalStateException e )
		{
		}
	}
	
	public void testGoodAttribute()
		throws IOException
	{
		XMLBuilder tops = new XMLBuilder( System.out );
		tops.element( "root" );
		tops.attribute( "name", "value" );
		tops.comment( "comment" );
	}
}
