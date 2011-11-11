package lox;

import java.io.IOException;

public interface DocumentWriter
{

	void document() throws IOException;

	void doctype( String rootName, String systemID, String publicID ) throws IOException;

	public void elementStart( String name )	throws IOException;
	
	void elementStart( boolean children ) throws IOException;
	
	void elementEnd( String name ) throws IOException;
	
	void attribute( String name, Object value ) throws IOException;
	
	void text( Object value, boolean escape ) throws IOException;;

	void cdata( String value ) throws IOException;

	void comment( String value ) throws IOException;

	void pi( String target, String data ) throws IOException;

	void whitespace( String value ) throws IOException;;

	void close() throws IOException;

}
