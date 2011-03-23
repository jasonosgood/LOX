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
		
		String expression = "Inventory/Animal";
		List<Element> animals = doc.find( expression );
		System.out.println( "animals found: " + animals.size() );
		for( Element animal : animals )
		{
			String habitat = animal.findFirstString( "Habitat" );
			String lifestyle = animal.findFirstString( "Lifestyle" );
			System.out.println( "habitat: " + habitat );
			System.out.println( "lifestyle: " + lifestyle );
		}
		
		List<Element> animals2 = doc.find( "*/Animal" );
		System.out.println( "animals2 found: " + animals2.size() );
		
		Element nameX = doc.getRoot().findFirst( "*/Name" );
		System.out.println( "nameX found: " + nameX.getText() );
		
		List<Element> names = doc.find( "Inventory/*/Name" );
		System.out.println( "names found: " + names.size() );
		
		List<Element> foodRecipe = doc.find( "**/FoodRecipe" );
		System.out.println( "food recipes found: " + foodRecipe.size() );
		
		List<Element> fail = doc.find( "FruitCake" );
		System.out.println( "fail found: " + fail.size() );
		
		List<Element> fruitCake = doc.find( "**/FruitCake" );
		System.out.println( "fruitCake found: " + fruitCake.size() );
		
	}

}
