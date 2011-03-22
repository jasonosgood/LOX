package lox.test;

import java.io.IOException;

import junit.framework.TestCase;
import lox.XMLBuilder;

public class BasicXML extends TestCase
{
	public static void main( String[] args )
		throws IOException
	{
		BasicXML basic = new BasicXML();
		basic.testBasicXML();
	}
	
	public void testBasicXML()
		throws IOException
	{
		XMLBuilder tops = new XMLBuilder( System.out );
		tops.element( "root" );
		tops.element( "child0" );
		tops.text( "text" );
		tops.pop();
		tops.comment( "comment" );
		tops.element( "child1" );
		tops.attribute( "name", "value" );
		tops.pop();
		tops.element(  "child2" );
		tops.attribute( "name", "value" );
		tops.attribute( "name", "value" );
		tops.element(  "grandchild1" );
		tops.attribute( "name", "value" );
		tops.text( "text" );
		tops.pop();
		tops.element(  "grandchild2" );
		tops.attribute( "name", "value" );
		tops.close();
	}


}
