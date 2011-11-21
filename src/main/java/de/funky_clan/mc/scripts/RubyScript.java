package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.file.SchematicLoader;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import org.jruby.RubyHash;
import org.jruby.RubySymbol;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import java.io.StringWriter;

/**
 * @author synopia
 */
public class RubyScript extends Script {
    private ScriptingContainer container;
    @Inject
    private BinvoxLoader          binvoxLoader;
    @Inject
    private Model model;
    @Inject
    private SchematicLoader schematicLoader;
    @Inject
    private SliceGraphics sliceGraphicsX;
    @Inject
    private SliceGraphics sliceGraphicsY;
    @Inject
    private SliceGraphics sliceGraphicsZ;
    @Inject
    private WorldGraphics worldGraphics;

    public void init() {
        if( container == null ) {
            container = new ScriptingContainer();
        }
    }

    protected void put( String key, Object value ) {
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

        sliceGraphicsX.setSliceType(SliceType.X);
        sliceGraphicsY.setSliceType(SliceType.Y);
        sliceGraphicsZ.setSliceType(SliceType.Z);

        put("@slice_y", sliceGraphicsY);
        put("@slice_x", sliceGraphicsX);
        put("@slice_z", sliceGraphicsZ);
        put("@world", worldGraphics);
        put("@binvox", binvoxLoader);
        put("@schematic", schematicLoader);
        put("@model", model);

        running = true;
        interalRun( "run" );
        running   = false;
        finished  = true;
        container = null;
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
