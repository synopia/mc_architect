package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.script.LoadScript;
import de.funky_clan.mc.events.script.RunScript;
import de.funky_clan.mc.events.script.ScriptFinished;
import de.funky_clan.mc.events.script.ScriptLoaded;
import de.funky_clan.mc.events.script.SendScriptData;
import de.funky_clan.mc.scripts.Script;
import de.funky_clan.mc.ui.renderer.ButtonEditor;
import de.funky_clan.mc.ui.renderer.ButtonRenderer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class ScriptsPanel extends JPanel {
    @Inject
    private EventDispatcher eventDispatcher;
    private JFileChooser    fileChooser;
    private JButton         load;

    @Inject
    public ScriptsPanel( SwingEventBus eventBus ) {
        super( new BorderLayout() );

        final ScriptsTableModel model = new ScriptsTableModel();
        final JTable            table = new JTable( model );

        eventBus.registerCallback( ScriptFinished.class, new EventHandler<ScriptFinished>() {
            @Override
            public void handleEvent( ScriptFinished event ) {
                model.fireTableDataChanged();
                table.repaint();
            }
        } );
        eventBus.registerCallback( ScriptLoaded.class, new EventHandler<ScriptLoaded>() {
            @Override
            public void handleEvent( ScriptLoaded event ) {
                Script script = event.getScript();

                if( !model.scripts.contains( script )) {
                    model.scripts.add( script );
                }

                model.fireTableDataChanged();
                table.repaint();
            }
        } );
        table.setPreferredScrollableViewportSize( new Dimension( 500, 70 ));
        table.setFillsViewportHeight( true );
        table.setRowSorter( new TableRowSorter<TableModel>( model ));
        table.getColumn( "Run" ).setCellEditor( new ButtonEditor( new JCheckBox() ));
        table.getColumn( "Run" ).setCellRenderer( new ButtonRenderer() );
        table.getColumn( "Send" ).setCellEditor( new ButtonEditor( new JCheckBox() ));
        table.getColumn( "Send" ).setCellRenderer( new ButtonRenderer() );

        JScrollPane scrollPane = new JScrollPane( table );

        build();
        add( load, BorderLayout.NORTH );
        add( scrollPane, BorderLayout.CENTER );
        new Timer( 500, new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                model.fireTableDataChanged();
                table.repaint();
            }
        } );
    }

    protected void build() {
        fileChooser = new JFileChooser();
        load        = new JButton( new AbstractAction( "Load Script" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                int result = fileChooser.showOpenDialog( ScriptsPanel.this );

                if( result == JFileChooser.APPROVE_OPTION ) {
                    File file = fileChooser.getSelectedFile();

                    if( file.isFile() && file.exists() ) {
                        eventDispatcher.fire( new LoadScript( file.getAbsolutePath(), false ));
                    }
                }
            }
        } );
    }

    class ScriptsTableModel extends AbstractTableModel {
        public static final int CHUNK_UPDATES = 4;
        public static final int NAME          = 0;
        public static final int NUMBER        = 6;
        public static final int PIXEL_UPDATES = 5;
        public static final int RUN           = 1;
        public static final int SEND          = 2;
        public static final int STATUS        = 3;
        private List<Script>    scripts       = new ArrayList<Script>();

        @Override
        public String getColumnName( int column ) {
            switch( column ) {
            case NAME:
                return "Name";

            case RUN:
                return "Run";

            case STATUS:
                return "Status";

            case CHUNK_UPDATES:
                return "Chunks";

            case PIXEL_UPDATES:
                return "Pixels";

            case SEND:
                return "Send";
            }

            return null;
        }

        @Override
        public int getRowCount() {
            return scripts.size();
        }

        @Override
        public int getColumnCount() {
            return NUMBER;
        }

        @Override
        public boolean isCellEditable( int rowIndex, int columnIndex ) {
            Script script = scripts.get( rowIndex );

            switch( columnIndex ) {
            case RUN:
                return true;

            case SEND:
                return script.isLoaded() && script.isFinished() && ( script.getError() == null );
            }

            return false;
        }

        @Override
        public Object getValueAt( int rowIndex, int columnIndex ) {
            Script script = scripts.get( rowIndex );
            Object result = null;

            switch( columnIndex ) {
            case 0:
                if( script.isLoaded() ) {
                    result = script.getName();
                } else {
                    result = script.getFilename();
                }

                break;

            case RUN:
                result = script.isLoaded()
                         ? "run"
                         : "reload";

                break;

            case STATUS:
                result = script.getStatusText();

                break;

            case CHUNK_UPDATES:
                result = script.getChunksUpdated();

                break;

            case PIXEL_UPDATES:
                result = script.getPixelsUpdated();

                break;

            case SEND:
                result = "send";

                break;
            }

            return result;
        }

        @Override
        public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
            Script script = scripts.get( rowIndex );

            if( aValue == null ) {
                return;
            }

            switch( columnIndex ) {
            case RUN:
                String cmd = aValue.toString();

                if( "reload".equals( cmd )) {
                    eventDispatcher.fire( new LoadScript( script ));
                    fireTableDataChanged();
                } else if( "run".equals( cmd )) {
                    eventDispatcher.fire( new RunScript( script ));
                    fireTableDataChanged();
                }

                break;

            case SEND:
                eventDispatcher.fire( new SendScriptData( script ));
                fireTableDataChanged();

                break;
            }
        }
    }
}
