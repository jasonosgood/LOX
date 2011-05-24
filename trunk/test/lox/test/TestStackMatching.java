package lox.test;

import java.util.regex.Pattern;

import lox.Attribute;
import lox.Element;
import lox.Stack;

public class TestStackMatching 
{

	public static final void main( String[] args )
	{
		
		Stack stack = new Stack();
		stack.add( new Element( "a" ));
		stack.add( new Element( "b" ));
		Element c = new Element( "c" );
		c.add( new Attribute( "whiz:id", "goof" ));
		stack.add( c );
		System.out.println( stack.match( "*[whiz:id]" ) );
		System.out.println( stack.match( "a/b" ) );
		System.out.println( stack.match( "a/b/c" ) );
		System.out.println( stack.match( "a/b/c[whiz:id]" ) );
		System.out.println( stack.match( "a/b/c/d[whiz:id]" ) );
		System.out.println( stack.match( "a/d/c[whiz:id]" ) );
		System.out.println( stack.match( "a/b/*[whiz:id]" ) );
		System.out.println( stack.match( "**/c" ) );
		System.out.println( stack.match( "**/c[whiz:id]" ) );
	}
}
