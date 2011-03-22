package lox.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import lox.Document;
import lox.LOXHandler;

import org.xml.sax.SAXException;


public class TestLoadDoc
{
	public static void main( String[] args ) throws ParserConfigurationException, SAXException, IOException
	{
		InputStream in = new FileInputStream( "./test/lox/test/zooinventory.xml" );
		Document doc = LOXHandler.load( in );
		System.out.println( doc );
	}

}
