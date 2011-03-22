package lox.example;

import java.io.IOException;

import lox.XMLBuilder;


public class 
	XMLDocType
{
	public static void main( String[] args)
		throws IOException
	{
		new XMLBuilder( System.out )
		{
			{
				document();
				doctype( "parent", "SYSTEMID", null );
				element( "child1" );
				attribute( "key", "value" );
				pop();
				element( "child2" );
				attribute( "key", "value" );
				element( "grandchild1" );
				attribute( "key", "value" );
				text( "data" );
				pop();
				element( "grandchild2" );
				attribute( "key", "value" );
				text( "data" );
				close();
			}
		};
	}
}
