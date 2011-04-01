package de.funky_clan.mc.ui.renderer;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author synopia
 */
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private boolean   isPushed;
    private String    label;

    public ButtonEditor( JCheckBox checkBox ) {
        super( checkBox );
        button = new JButton();
        button.setOpaque( true );
        button.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                fireEditingStopped();
            }
        } );
    }

    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        if( isSelected ) {
            button.setForeground( table.getSelectionForeground() );
            button.setBackground( table.getSelectionBackground() );
        } else {
            button.setForeground( table.getForeground() );
            button.setBackground( table.getBackground() );
        }

        label = ( value == null )
                ? ""
                : value.toString();
        button.setText( label );
        isPushed = true;

        return button;
    }

    public Object getCellEditorValue() {
        String result = null;

        if( isPushed ) {
            result = label;
        }

        isPushed = false;

        return result;
    }

    public boolean stopCellEditing() {
        isPushed = false;

        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
