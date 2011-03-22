package lox.example;

import lox.XMLBuilder;

public class Example1
{
	public static void main( String[] args)
		throws Exception
	{
		XMLBuilder xml = new XMLBuilder( System.out );
		xml.document();
		xml.doctype( "root", "MYSYSTEM", "MYPUBLIC" );
		xml.element( "root" );
		xml.element( "first" );
		xml.attribute( "key", "value" );
		xml.attribute( "key", "value" );
		xml.element( "second" );
		xml.text( "data" );
		xml.pop();
		xml.element( "third" );
		xml.pop();
		xml.element( "fourth" );
		xml.attribute( "key", "value" );
		xml.pop();
		xml.pop();
		
		xml.close();
	}
}
