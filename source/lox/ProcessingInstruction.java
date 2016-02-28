/* Copyright 2007, 2016 Jason Aaron Osgood

   See copyright.txt for license.
*/


package lox;

import java.io.IOException;


public class 
	ProcessingInstruction
extends
	Content
{
	private String _target;
	private String _data;
	
	public ProcessingInstruction( String target, String data )
	{
		target( target );
		data( data );
	}
	
	public void target( String target )
	{
		if( target == null )
		{
			throw new NullPointerException( "target" );
		}
		
		String temp = target.trim();
		
		if( temp.length() < 0 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		_target = temp;
	}
	
	public String target()
	{
		return _target;
	}
	
	public void data( String data )
	{
		if( data == null )
		{
			throw new NullPointerException( "data" );
		}
		
		String temp = data.trim();
		
		if( temp.length() < 0 )
		{
			throw new IllegalArgumentException( "empty string not allowed" );
		}
		_data = temp;
	}
	
	public String data()
	{
		return _data;
	}

	public void serialize( DocumentWriter writer ) 
		throws IOException
	{
		writer.pi( target(), data() );
	}
}
