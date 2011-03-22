package lox.example;

import lox.Element;
import lox.LOXBuilder;
import lox.LOXException;

public class GetContent
{
	public static void main( String[] args )
		throws LOXException
	{
		LOXBuilder builder = new LOXBuilder()
		{{
			document();
			comment( "prolog" );
			element( "root" );
			comment( "comment" );
			text( "abc" );
			element( "child" );
			text( "123" );
			pop();
			text( "xyz" );
		}};
		
		System.out.println( builder );
		Element root = builder.getDocument().getRoot();
		System.out.println( "content: " + root.toContent() );
	}

}
