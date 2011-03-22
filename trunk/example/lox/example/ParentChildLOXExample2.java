package lox.example;

import lox.Document;
import lox.LOXBuilder;
import lox.LOXException;

public class ParentChildLOXExample2
{
	public static void main( String[] args )
		throws LOXException
	{
		ParentChildLOXExample1 example = new ParentChildLOXExample1();
		Document document = example.make();
		System.out.print( document );
	}
	
	public Document make()
		throws LOXException
	{
		LOXBuilder builder = new LOXBuilder();
		builder.element( "parent" );
		builder.element( "child1" );
		builder.attribute( "key", "value" );
		builder.pop();
		builder.element( "child2" );
		builder.attribute( "key", "value" );
		builder.element( "grandchild1" );
		builder.attribute( "key", "value" );
		builder.text( "data" );
		builder.pop();
		builder.element( "grandchild2" );
		builder.attribute( "key", "value" );
		builder.text( "data" );
		
		Document document = builder.getDocument();
		return document;
	}
}
