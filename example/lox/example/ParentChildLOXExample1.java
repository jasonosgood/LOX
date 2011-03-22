package lox.example;

import lox.Attribute;
import lox.Document;
import lox.Element;
import lox.Text;

public class ParentChildLOXExample1
{
	public static void main( String[] args)
	{
		ParentChildLOXExample1 example = new ParentChildLOXExample1();
		Document document = example.make();
		System.out.print( document );
	}
	
	public Document make()
	{
		Document document = new Document();
		
		Element parent = new Element( "parent" );
		document.add( parent );
		
		Element child1 = new Element( "child1" );
		child1.add( new Attribute( "key", "value" ));
		parent.add( child1 );
		
		Element child2 = new Element( "child2" );
		child2.add( new Attribute( "key", "value" ));
		parent.add( child2 );
		
		Element grandchild1 = new Element( "grandchild1" );
		grandchild1.add( new Attribute( "key", "value" ));
		grandchild1.add( new Text( "data" ));
		child2.add( grandchild1 );

		Element grandchild2 = new Element( "grandchild2" );
		grandchild2.add( new Attribute( "key", "value" ));
		grandchild2.add( new Text( "data" ));
		child2.add( grandchild2 );

		return document;
	}
}
