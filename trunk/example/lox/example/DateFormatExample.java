package lox.example;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lox.Document;
import lox.LOXBuilder;
import lox.LOXException;
import lox.XMLWriter;


public class 
	DateFormatExample
{
	public static void main( String[] args)
		throws LOXException, IOException
	{
		DateFormat format = SimpleDateFormat.getDateInstance( SimpleDateFormat.FULL );
		Date now = new Date();
		PrintStream out = System.out;
		out.println( format.format( now ));
		out.println( now );
		
		LOXBuilder example = new LOXBuilder()
		{
			{
				Date now = new Date();
				
				document();
				element( "root" );
				element( "event" );
				attribute( "date", now );
				pop();
				element( "event" );
				text( now );
			}
		};
		
		Document document = example.getDocument();
		
		// No Date formatting
		{
			XMLWriter writer = new XMLWriter( out );
			document.serialize( writer );
//			writer.flush();
//			writer.close();
//		}
//		
//		// Using SimpleDateFormat.MEDIUM
//		{
			XMLWriter writer2 = new XMLWriter( out );
			DateFormat format2 = SimpleDateFormat.getDateInstance( SimpleDateFormat.MEDIUM );
			writer2.addFormat( Date.class, format2 );
			document.serialize( writer2 );
			writer2.flush();
//			writer.close();
		}
	}
}
