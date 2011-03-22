package lox.example;

import java.io.IOException;
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
			XMLWriter writer = new XMLWriter( System.out );
			document.serialize( writer );
		}
		
		// Using SimpleDateFormat.MEDIUM
		{
			XMLWriter writer = new XMLWriter( System.out );
			DateFormat format = SimpleDateFormat.getDateInstance( SimpleDateFormat.MEDIUM );
			writer.addFormat( Date.class, format );
			document.serialize( writer );
		}
	}
}
