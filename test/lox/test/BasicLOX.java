package lox.test;

import junit.framework.TestCase;
import lox.Document;
import lox.LOXBuilder;
import lox.LOXException;

public class BasicLOX extends TestCase
{
	public static void main( String[] args )
		throws LOXException
	{
		BasicLOX basic = new BasicLOX();
		basic.testBasicLOX();
	}
	
	public void testBasicLOX()
		throws LOXException
	{
		LOXBuilder builder = new LOXBuilder();
		builder.element( "root" );
		builder.element( "child0" );
		builder.text( "text" );
		builder.pop();
		builder.comment( "comment" );
		builder.element( "child1" );
		builder.attribute( "name", "value" );
		builder.pop();
		builder.element(  "child2" );
		builder.attribute( "name", "value" );
		builder.attribute( "name", "value" );
		builder.element(  "grandchild1" );
		builder.attribute( "name", "value" );
		builder.text( "text" );
		builder.pop();
		builder.element(  "grandchild2" );
		builder.attribute( "name", "value" );
		
		Document document = builder.getDocument();
		System.out.print( document );
	}
}
