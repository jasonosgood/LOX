package lox.example;

import lox.Document;
import lox.LOXBuilder;
import lox.LOXException;

public class 
	ParentChildLOXExample4
{
	public static void main( String[] args)
		throws LOXException
	{
		LOXBuilder example = new LOXBuilder()
		{
			{
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
			}
		};

		Document document = example.getDocument();
		System.out.print( document );
	}
}
