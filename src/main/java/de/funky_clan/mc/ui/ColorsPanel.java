package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.events.swing.ColorChanged;
import de.funky_clan.mc.ui.renderer.ColorEditor;
import de.funky_clan.mc.ui.renderer.ColorRenderer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * @author synopia
 */
public class ColorsPanel extends JPanel {
    @Inject
    private EventDispatcher eventDispatcher;
    @Inject
    private Colors colors;

    @Inject
    public ColorsPanel() {
        super(new GridLayout(1,0));

        JTable table = new JTable(new ColorTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500,70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setDefaultEditor(Color.class, new ColorEditor());
        table.setDefaultRenderer(Color.class, new ColorRenderer(true));

        add( scrollPane );
    }

    class ColorTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return DataValues.values().length;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return DataValues.values()[rowIndex].toString();
                case 1:
                    return colors.getColorForBlock(DataValues.values()[rowIndex].getId());
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 1:
                    int id = DataValues.values()[rowIndex].getId();
                    if( !colors.getColorForBlock(id).equals(aValue) ) {
                        colors.setColorForBlock(id, (Color)aValue);
                        eventDispatcher.fire( new ColorChanged(id, (Color) aValue));
                    }
            }

        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return Color.class;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex==1;
        }
    }
}
