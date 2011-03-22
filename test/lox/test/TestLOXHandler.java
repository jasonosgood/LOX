package lox.test;

import lox.LOXHandler;

public class TestLOXHandler
{
	
	public static void main( String[] args )
		throws Exception
	{
		
		LOXHandler handler = new LOXHandler();
		handler.startDocument();
		handler.startElement( null, "test", "test", null );
		String trimmable = "\ntrimmable\n";
		handler.characters( trimmable.toCharArray(), 0, trimmable.length() );

	}

}
