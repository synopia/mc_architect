package de.funky_clan.mc.scripts;

import org.jruby.Ruby;
import org.jruby.RubyHash;
import org.jruby.RubySymbol;
import org.jruby.embed.*;

import java.io.*;

/**
 * @author synopia
 */
public class Script {
    private String filename;
    private boolean useClasspath;

    private String author;
    private String name;

    private EvalFailedException hasError;
    private boolean loaded;
    private boolean running = false;
    private boolean finished = false;

    ScriptingContainer container;
    private StringWriter writer;

    public Script(String filename, boolean useClasspath) {
        this.filename = filename;
        this.useClasspath = useClasspath;
    }

    public void init() {
        if( container==null ) {
            writer = new StringWriter();
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
            result = interalRun("info");
            RubyHash hash = (RubyHash) result;
            author = getString(hash,"author");
            name   = getString(hash,"name");
            loaded = true;
        } catch (EvalFailedException e) {
            hasError = e;
        }
    }

    public void run() {
        init();

        running = true;
        interalRun("run");
        running = false;
        finished = true;

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
        return name!=null?name:filename;
    }

    public boolean isLoaded() {
        return loaded;
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
                    result = "finished";
                } else {
                    result = "loaded";
                }
            }
        }
        return result;
    }

    public EvalFailedException getError() {
        return hasError;
    }

    public String getOutput() {
        return writer.toString();
    }

    protected Object interalRun( String methodCall ) {
        Object result;
        container.put("@dummy", this);
        writer = new StringWriter();
        container.setWriter(writer);
        container.setErrorWriter(writer);
        container.runScriptlet("def info\nraise 'Script must implement "+methodCall+"()!'\nend\n");
        if( useClasspath ) {
            container.runScriptlet(PathType.CLASSPATH, filename);
        } else {
            container.runScriptlet(PathType.ABSOLUTE, filename);

        }
        return container.runScriptlet(methodCall);
    }

    protected String getString( RubyHash hash, String key ) {
        Object result = null;
        RubySymbol.SymbolTable symbolTable = container.getRuntime().getSymbolTable();
        RubySymbol symbol = symbolTable.getSymbol(key);
        if( symbol!=null ) {
            result = hash.get(symbol);
        }
        if( result!=null ) {
            return result.toString();
        } else {
            return null;
        }
    }
}
