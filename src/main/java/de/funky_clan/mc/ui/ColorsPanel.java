package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.events.swing.ColorChanged;
import de.funky_clan.mc.ui.renderer.ColorEditor;
import de.funky_clan.mc.ui.renderer.ColorRenderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * @author synopia
 */
public class ColorsPanel extends JPanel {
    @Inject
    private Colors          colors;
    @Inject
    private EventDispatcher eventDispatcher;

    @Inject
    public ColorsPanel() {
        super( new GridLayout( 1, 0 ));

        ColorTableModel model = new ColorTableModel();
        JTable          table = new JTable( model );

        table.setPreferredScrollableViewportSize( new Dimension( 500, 70 ));
        table.setFillsViewportHeight( true );
        table.setRowSorter( new TableRowSorter<TableModel>( model ));

        JScrollPane scrollPane = new JScrollPane( table );

        table.setDefaultEditor( Color.class, new ColorEditor() );
        table.setDefaultRenderer( Color.class, new ColorRenderer( true ));
        add( scrollPane );
    }

    class ColorTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return DataValues.values().length - 1;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt( int rowIndex, int columnIndex ) {
            switch( columnIndex ) {
            case 0:
                return DataValues.values()[rowIndex].toString();

            case 1:
                return colors.getColorForBlock( DataValues.values()[rowIndex].getId() );

            case 2:
                return colors.getColorForBlueprint(DataValues.values()[rowIndex].getId());
            }

            return null;
        }

        @Override
        public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
            int id;
            switch( columnIndex ) {
                case 1:
                id = DataValues.values()[rowIndex].getId();

                if( !colors.getColorForBlock( id ).equals( aValue )) {
                    colors.setColorForBlock( id, (Color) aValue );
                    eventDispatcher.publish(new ColorChanged(id, (Color) aValue));
                }
                break;

            case 2:
                id = DataValues.values()[rowIndex].getId();

                if( !colors.getColorForBlueprint( id ).equals( aValue )) {
                    colors.setColorForBlueprint( id, (Color) aValue );
                    eventDispatcher.publish(new ColorChanged(id, (Color) aValue));
                }
                break;
            }
        }

        @Override
        public Class<?> getColumnClass( int columnIndex ) {
            switch( columnIndex ) {
            case 0:
                return String.class;

            case 1:
                return Color.class;

            case 2:
                return Color.class;
            }

            return null;
        }

        @Override
        public boolean isCellEditable( int rowIndex, int columnIndex ) {
            return columnIndex == 1 || columnIndex==2;
        }
    }
}
