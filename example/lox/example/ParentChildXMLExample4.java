package lox.example;

import java.io.IOException;

import lox.XMLBuilder;


public class 
	ParentChildXMLExample4
{
	public static void main( String[] args)
		throws IOException
	{
		new XMLBuilder( System.out )
		{
			{
				document();
				element( "parent" );
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
