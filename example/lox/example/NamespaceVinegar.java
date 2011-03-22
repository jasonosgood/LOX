package lox.example;

import lox.Document;
import lox.Element;
import lox.LOXBuilder;
import lox.LOXException;

public class 
	NamespaceVinegar
{
	public static void main( String[] args )
		throws LOXException
	{
		LOXBuilder example = new LOXBuilder()
		{{
			element( "root" );
			attribute( "xmlns:sour", "http://www.namespacevinegar.zzz/" );
			element( "sour:item" );
			attribute( "sour:key", "value" );
		}};
		
		Document document = example.getDocument();
		System.out.println( document );
		
		Element root = document.getRoot();
		
		Element item1 = root.findFirst( "item" );
		System.out.println( "item1: " + item1 );
		
		Element item2 = root.findFirst( "sour:item" );
		System.out.println( "item2: " + item2 );
		
	}
}
