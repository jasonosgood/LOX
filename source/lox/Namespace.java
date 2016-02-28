/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/

package lox;

public class 
	Namespace 
extends 
	Attribute
{
	public Namespace( String uri )
	{
		this( null, uri );
	}
	
	public Namespace( String prefix, String uri )
	{
		super( "xmlns", uri );
	}
	
	
}
