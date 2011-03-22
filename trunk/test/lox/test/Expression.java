package lox.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import lox.Document;
import lox.Element;
import lox.LOXHandler;

import org.xml.sax.SAXException;

public class Expression 
{
	public static void main( String[] args ) throws ParserConfigurationException, SAXException, IOException
	{
		InputStream in = new FileInputStream( "./test/lox/test/zooinventory.xml" );
		Document doc = LOXHandler.load( in );
		System.out.println( doc );
		
		String expression = "Inventory/Animal";
		List<Element> animals = doc.find( expression );
		for( Element animal : animals )
		{
			Element habitat = animal.findFirst( "Habitat" );
			System.out.println( "habitat: " + habitat );
		}
	}

}
