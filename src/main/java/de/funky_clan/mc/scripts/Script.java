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
public abstract class Script {
    protected String                    filename;
    protected boolean                   useClasspath;

    protected boolean                         running  = false;
    protected boolean                         finished = false;
    protected boolean                         sent     = false;
    protected String                          author;
    protected Exception                       hasError;
    protected boolean                         loaded;
    protected String                          name;
    protected HashMap<Long, P052BlockMultiUpdate> updates;
    protected StringWriter                    writer;

    public void setFilename( String filename, boolean useClasspath ) {
        this.filename     = filename;
        this.useClasspath = useClasspath;
        writer = new StringWriter();
    }

    public abstract void init();

    public abstract void load();

    public abstract void run();

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

}
