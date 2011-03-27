package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.Main;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.script.LoadScript;
import de.funky_clan.mc.events.script.RunScript;
import de.funky_clan.mc.events.script.ScriptFinished;
import de.funky_clan.mc.events.script.ScriptLoaded;
import de.funky_clan.mc.scripts.Script;
import de.funky_clan.mc.ui.renderer.ButtonEditor;
import de.funky_clan.mc.ui.renderer.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
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

    private JButton load;
    private JFileChooser fileChooser;


    class ScriptsTableModel extends AbstractTableModel {
        public static final int NAME   = 0;
        public static final int RUN    = 1;
        public static final int STATUS = 2;
        public static final int NUMBER = 3;

        private List<Script> scripts = new ArrayList<Script>();

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case NAME:   return "Name";
                case RUN:    return "Run";
                case STATUS: return "Status";
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
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex==RUN;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Script script = scripts.get(rowIndex);
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
                    result = script.isLoaded() ? "run" : "reload";
                    break;
                case STATUS:
                    result = script.getStatusText();
                    break;
            }
            return result;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Script script = scripts.get(rowIndex);
            if( columnIndex==RUN && aValue!=null ) {
                String cmd = aValue.toString();
                if( "reload".equals(cmd) ) {
                    fireTableDataChanged();
                    eventDispatcher.fire( new LoadScript(script) );
                } else if( "run".equals(cmd) ) {
                    fireTableDataChanged();
                    eventDispatcher.fire( new RunScript(script) );
                }
            }
        }
    }

    @Inject
    public ScriptsPanel(SwingEventBus eventBus) {
        super(new BorderLayout());

        final ScriptsTableModel model = new ScriptsTableModel();
        final JTable table = new JTable(model);

        eventBus.registerCallback(ScriptFinished.class, new EventHandler<ScriptFinished>() {
            @Override
            public void handleEvent(ScriptFinished event) {
                model.fireTableDataChanged();
                table.repaint();
            }
        });

        eventBus.registerCallback(ScriptLoaded.class, new EventHandler<ScriptLoaded>() {
            @Override
            public void handleEvent(ScriptLoaded event) {
                Script script = event.getScript();
                if( !model.scripts.contains(script) ) {
                    model.scripts.add(script);
                }
                model.fireTableDataChanged();
                table.repaint();
            }
        });

        table.setPreferredScrollableViewportSize(new Dimension(500,70));
        table.setFillsViewportHeight(true);
        table.setRowSorter(new TableRowSorter<TableModel>(model));
        table.getColumn("Run").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Run").setCellRenderer(new ButtonRenderer());

        JScrollPane scrollPane = new JScrollPane(table);

        build();
        add( load, BorderLayout.NORTH  );
        add( scrollPane, BorderLayout.CENTER);

        new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireTableDataChanged();
                table.repaint();
            }
        });
    }

    protected void build() {
        fileChooser = new JFileChooser();
        load = new JButton(new AbstractAction("Load Script") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(ScriptsPanel.this);
                if( result == JFileChooser.APPROVE_OPTION ) {
                    File file = fileChooser.getSelectedFile();
                    if( file.isFile() && file.exists() ) {
                        eventDispatcher.fire(new LoadScript(file.getAbsolutePath(), false));
                    }
                }
            }
        });
    }
}
