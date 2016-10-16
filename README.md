Use LOX to read, navigate, query, build, and write XML documents. Alternative to W3C DOM and XPath.

![lox diagram]
(/doc/lox.diagram.png)

ugh

(https://github.com/jasonosgood/LOX/blob/master/doc/lox.diagram.png)

Unique features.

 - Uses JDK 1.5's generics and foreach.
Builders for both object graphs and Writers.
Optional data type Formatters.
'toString()' method outputs XML formatted data. Great for debugging.
Treats XML Namespaces as syntactic vinegar.

Example

The following four examples will recreate this document using four different strategies: manual construction, using the LOXBuilder, subclassing the LOXBuilder, and using a static initializer with LOXBuilder. Which strategy you choose is dependent on your esthetics and temperment.

<?xml version="1.0" encoding="UTF-8"?> <parent> <child1 key="value"/> <child2 key="value"> <grandchild1 key="value">data</grandchild1> <grandchild2 key="value">data</grandchild2> </child2> </parent>

Example 1 - Manually

Here's how to make the above document the "traditional" way. It's familiar, but not very much fun. Notice all the temporary local variables and how children are added to parents out of order. ``` public class ParentChildLOXExample1 { public static void main( String[] args ) { ParentChildLOXExample1 example = new ParentChildLOXExample1(); Document document = example.make(); System.out.print( document ); }

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
} ```

Example 2 - Using LOXBuilder

Here's creating the same document using the LOXBuilder. Instead of creating instances of Element, you use LOXBuilder's 'element( String name )' method. (Ditto Document, Attribute, etc.) This eliminates the local variables (and the assignment statements), creating a more "functional programming" feel. (TODO: Link to JavaDoc.)

There's a stack data structure within LOXBuilder. When you call method 'element(...)', internally an Element instance is created. That instance is pushed to the top of the stack. If there's already a parent Element instance on the stack, the new instance is added as a child. The 'pop()' method just pops the top Element off of the internal stack.

Using the LOXBuilder, the XML document is constructed in "document order". Meaning that the order of method calls mirrors the structure of the constructed XML document. This is also called a "depth-first traversal", where each element's first child is traversed before its siblings.

``` public class ParentChildLOXExample2 { public static void main( String[] args ) { ParentChildLOXExample1 example = new ParentChildLOXExample1(); Document document = example.make(); System.out.print( document ); }

public Document make()
{
    LOXBuilder builder = new LOXBuilder();
    builder.element( "parent" );
    builder.element( "child1" );
    builder.attribute( "key", "value" );
    builder.pop();
    builder.element( "child2" );
    builder.attribute( "key", "value" );
    builder.element( "grandchild1" );
    builder.attribute( "key", "value" );
    builder.text( "data" );
    builder.pop();
    builder.element( "grandchild2" );
    builder.attribute( "key", "value" );
    builder.text( "data" );

    Document document = builder.getDocument();
    return document;
}
} ```

Example 3 - Subclassing LOXBuilder

Subclassing LOXBuilder can result in more concise code.

``` public class ParentChildLOXExample3 extends LOXBuilder { public static void main( String[] args ) { ParentChildLOXExample3 example = new ParentChildLOXExample3(); Document document = example.make(); System.out.print( document ); }

public Document make()
{
    element( "parent" );
    element( "child1" );
    attribute( "key", "value" );
    pop();

    element( "child2" );
    attribute( "key", "value" );

    element( "grandchild1" );
    attribute( "key", "value" );
    text( "data" );
    pop();

    element( "grandchild2" );
    attribute( "key", "value" );
    text( "data" );

    return getDocument();
}
} ```

Example 4 - LOXBuilder with a static initializer

Using the static initializer "trick" with a LOXBuilder can result in even more concise code.

``` public class ParentChildLOXExample4 { public static void main( String[] args) { LOXBuilder example = new LOXBuilder() { { element( "parent" ); element( "child1" ); attribute( "key", "value" ); pop(); element( "child2" ); attribute( "key", "value" ); element( "grandchild1" ); attribute( "key", "value" ); text( "data" ); pop(); element( "grandchild2" ); attribute( "key", "value" ); text( "data" ); } };

    Document document = example.getDocument();
    System.out.print( document );
}
} ```

Status / TODO

build script
initial (alpha) distributable .jar file
documentation, flesh out JavaDocs (yuck)
compile XML file to code (similar to XMLC, but better)
convert examples to unit tests
figure out the licensing stuff
some kind of xpath support
fake Namespace support
