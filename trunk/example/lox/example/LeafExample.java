package lox.example;

import lox.Document;
import lox.LOXBuilder;
import lox.LOXException;

public class 
	LeafExample
extends
	LOXBuilder
{
	public static void main( String[] args)
		throws LOXException
	{
		LeafExample example = new LeafExample();
		Document document = example.make();
		System.out.print( document );
	}
	
	public Document make()
		throws LOXException
	{
		element( "parent" );
		leaf( "child0" );
		attribute( "key", "value" );
		
		leaf( "child1" );
		attribute( "key", "value" );
		
		element( "child2" );
		attribute( "key", "value" );
	
		element( "grandchild1" );
		attribute( "key", "value" );
		text( "data" );
		pop();
			
		element( "grandchild2" );
		attribute( "key", "value" );
		text( "data" );
		pop();
		
		pop();

		leaf( "child3" );
		attribute( "key", "value" );
		
		return getDocument();
	}
}
