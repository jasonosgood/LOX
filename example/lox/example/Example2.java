package lox.example;


import java.io.StringWriter;
import java.io.Writer;

import lox.XMLBuilder;


public class 
	Example2
extends
	XMLBuilder
{
	public Example2( Writer writer )
	{
		super( writer );
	}

	public static void main( String[] args)
		throws Exception
	{
		StringWriter writer = new StringWriter();
		Example2 test2 = new Example2( writer );
		test2.doctype( "MYNAME", "MYSYSTEM", "MYPUBLIC" );
		test2.make();
		test2.close();
		System.out.println( writer.toString() );
	}
	
	public void make()
		throws Exception
	{
		element( "root" );
		element( "first" );
		attribute( "key", "value" );
		attribute( "key", "value" );
		element( "second" );
		text( "data" );
		pop();
		element( "third" );
		pop();
		element( "fourth" );
		attribute( "key", "value" );
	}

}
