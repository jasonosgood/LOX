package lox.example;

import lox.Document;
import lox.LOXBuilder;
import lox.LOXException;

public class 
	ParentChildLOXExample3
extends
	LOXBuilder
{
	public static void main( String[] args)
		throws LOXException
	{
		ParentChildLOXExample3 example = new ParentChildLOXExample3();
		Document document = example.make();
		System.out.print( document );
	}
	
	public Document make()
		throws LOXException
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
		
		return getDocument();
	}
}
