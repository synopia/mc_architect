package de.funky_clan.mc.scripts;

import de.funky_clan.mc.net.packets.P052BlockMultiUpdate;
import org.jruby.RubyHash;
import org.jruby.RubySymbol;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import java.io.StringWriter;
import java.util.HashMap;

/**
 * @author synopia
 */
public class Script {
    private boolean                         running  = false;
    private boolean                         finished = false;
    private boolean                         sent     = false;
    private String                          author;
    ScriptingContainer                      container;
    private final String                    filename;
    private Exception                       hasError;
    private boolean                         loaded;
    private String                          name;
    private HashMap<Long, P052BlockMultiUpdate> updates;
    private final boolean                   useClasspath;
    private StringWriter                    writer;

    public Script( String filename, boolean useClasspath ) {
        this.filename     = filename;
        this.useClasspath = useClasspath;
    }

    public void init() {
        if( container == null ) {
            writer    = new StringWriter();
            container = new ScriptingContainer();
        }
    }

    public void put( String key, Object value ) {
        init();
        container.put( key, value );
    }

    public void load() {
        init();

        Object result;

        try {
            result = interalRun( "info" );

            RubyHash hash = (RubyHash) result;

            author = getString( hash, "author" );
            name   = getString( hash, "name" );
            loaded = true;
        } catch( Exception e ) {
            hasError = e;
        }
    }

    public void run() {
        init();
        running = true;
        interalRun( "run" );
        running   = false;
        finished  = true;
        container = null;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isUseClasspath() {
        return useClasspath;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return( name != null )
              ? name
              : filename;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getStatusText() {
        String result;

        if( !loaded ) {
            result = "not loaded";
        } else {
            if( running ) {
                result = "running";
            } else {
                if( finished ) {
                    if( sent ) {
                        result = "sent";
                    } else {
                        result = "ready";
                    }
                } else {
                    result = "loaded";
                }
            }
        }

        return result;
    }

    public int getChunksUpdated() {
        return( updates != null )
              ? updates.size()
              : 0;
    }

    public int getPixelsUpdated() {
        int total = 0;

        if( updates != null ) {
            for( P052BlockMultiUpdate update : updates.values() ) {
                total += update.getSize();
            }
        }

        return total;
    }

    public void setChunkUpdates( HashMap<Long, P052BlockMultiUpdate> updates ) {
        this.updates = updates;
    }

    public HashMap<Long, P052BlockMultiUpdate> getChunkUpdates() {
        return updates;
    }

    public void setSent( boolean sent ) {
        this.sent = sent;
    }

    public Exception getError() {
        return hasError;
    }

    public String getOutput() {
        return writer.toString();
    }

    protected Object interalRun( String methodCall ) {
        container.put( "@dummy", this );
        writer = new StringWriter();
        container.setWriter( writer );
        container.setErrorWriter( writer );
        container.runScriptlet( "def info\nraise 'Script must implement " + methodCall + "()!'\nend\n" );

        if( useClasspath ) {
            container.runScriptlet( PathType.CLASSPATH, filename );
        } else {
            container.runScriptlet( PathType.ABSOLUTE, filename );
        }

        try {
            return container.runScriptlet( methodCall );
        } catch (Exception e) {
            hasError = e;
            return null;
        }

    }

    protected String getString( RubyHash hash, String key ) {
        Object                 result      = null;
        RubySymbol.SymbolTable symbolTable = container.getRuntime().getSymbolTable();
        RubySymbol             symbol      = symbolTable.getSymbol( key );

        if( symbol != null ) {
            result = hash.get( symbol );
        }

        if( result != null ) {
            return result.toString();
        } else {
            return null;
        }
    }
}
