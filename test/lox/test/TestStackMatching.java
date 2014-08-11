package lox.test;

import lox.Attribute;
import lox.Element;
import lox.Stack;

public class TestStackMatching 
{

	public static final void main( String[] args )
	{
		firstTest();
		secondTest();
		thirdTest();
	}
	public static void firstTest()
	{
		Stack stack = new Stack();
		stack.add( new Element( "a" ));
		stack.add( new Element( "b" ));
		stack.add( new Element( "c" ).addAttribute( "martini", "goof" ));
		
		System.out.println( "false : " + stack.match( "*[martini]" ) );
		System.out.println( "false : " + stack.match( "a/b" ) );
		System.out.println( "true : " + stack.match( "a/b/c" ) );
		System.out.println( "true : " + stack.match( "a/b/c[martini]" ) );
		System.out.println( "false : " + stack.match( "a/b/c/d[martini]" ) );
		System.out.println( "false : " + stack.match( "a/d/c[martini]" ) );
		System.out.println( "true : " + stack.match( "a/b/*[martini]" ) );
		System.out.println( "true : " + stack.match( "**/c" ) );
		System.out.println( "true : " + stack.match( "**/c[martini]" ) );
		System.out.println( "true : " + stack.match( "**/*[martini]" ) );
//		System.out.println( "true : " + stack.match( "**/a/**/*[martini]" ));	
	}
	// 
	public static void secondTest()
	{
		// html/body/ul/li/a/span
		Stack stack = new Stack();
		stack.add( new Element( "html" ));
		stack.add( new Element( "body" ));
		stack.add( new Element( "ul" ));
		stack.add( new Element( "li" ));
		stack.add( new Element( "a" ));
		stack.add( new Element( "span" ));
		System.out.println( "true : " + stack.match( "html/body/ul/li/a/span" ) );
		System.out.println( "true : " + stack.match( "html/body/ul/li/**/*" ) );
	}

	public static void thirdTest()
	{
		// html/body/ul/li/a/span
		Stack stack = new Stack();
//		stack.add( new Element( "html" ));
//		stack.add( new Element( "body" ));
//		stack.add( new Element( "ul" ));
		stack.add( new Element( "li" ));
		stack.add( new Element( "a" ));
		stack.add( new Element( "span" ));
		System.out.println( "true : " + stack.match( "li/a/span" ) );
		System.out.println( "true : " + stack.match( "li/**/span" ) );
		System.out.println( "true : " + stack.match( "li/a/**/span" ) );
	}

}
