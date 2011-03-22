/* Copyright 2007 Jason Aaron Osgood
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 2.1 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the 
   Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
   Boston, MA 02111-1307  USA
   
   Jason Osgood
   jason@jasonosgood.com
   http://code.google.com/p/lox/
   
*/
package lox;

public class 
	NullChecker 
implements 
	Checker
{
	public static final Checker NULL = new NullChecker();
	
	private NullChecker() {}
	
	public void doctype( String name, String systemID, String publicID ) throws LOXException {}
	
	public void element( String name ) throws LOXException {}
	
	public void attribute( String name ) throws LOXException {}
	
	public void pi( String target, String data ) throws LOXException {}
	
	public void cdata( String value ) throws LOXException {}

}
