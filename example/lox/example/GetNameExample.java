package lox.example;

import java.util.List;

import lox.Document;
import lox.Element;
import lox.LOXBuilder;
import lox.LOXException;


public class 
	GetNameExample
{
	public static void main( String[] args)
		throws LOXException
	{
		LOXBuilder example = new LOXBuilder()
			{{
				element( "parent" );
				element( "child" );
				attribute( "name", "Adam" );
				pop();
				element( "child" );
				attribute( "name", "Eve" );
				element( "grandchild" );
				attribute( "name", "Mary" );
				text( "data" );
				pop();
				element( "grandchild" );
				attribute( "name", "John" );
				text( "data" );
			}};

		Document document = example.getDocument();
		System.out.print( document );
		Element root = document.getRoot();
		Element child = root.get( "child" );
		System.out.println( child );
		List<Element> children = root.getAll( "child" );
		System.out.println( children );
		
		List ugh = document.query( "//child" );
		for( Object b :ugh )
		{
			System.out.print( b );
		}
		
		ugh = document.query( "//child/@name" );
		for( Object b :ugh )
		{
			System.out.print( b );
		}
		
	}
}
