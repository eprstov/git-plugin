package hudson.plugins.git;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class Statusbook
{
	public static class Entry
	{
		public String revision;
		public String path;
	}
	
    private static final Pattern PTRN = Pattern.compile( "^.([0-9a-f]{40}) (.+) \\(.*\\)$");

    public static List<Entry> Parse( File file ) throws IOException
	{
        LineIterator iter = null;
        try
        {
        	iter = FileUtils.lineIterator( file, "UTF-8" );
        	return Parse( iter );
        }
        finally
        {
        	LineIterator.closeQuietly( iter );
        }
	}

    private static List<Entry> Parse( Iterator<String> iter )
    {
		LinkedList<Entry> entries = new LinkedList<Entry>();

        while( iter.hasNext() )
        {
            String line = iter.next();
            Matcher matcher = PTRN.matcher( line );
            
            if( matcher.matches() && matcher.groupCount() >= 2 )
            {
            	Entry entry = new Entry(); 
                entry.revision = matcher.group(1);
                entry.path = matcher.group(2);
                
                entries.add( entry );
            }
        }

        return entries;
    }
}
