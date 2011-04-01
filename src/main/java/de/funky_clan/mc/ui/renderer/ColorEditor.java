package de.funky_clan.mc.ui.renderer;

import com.bric.swing.ColorPicker;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author synopia
 */
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    protected static final String EDIT = "edit";
    private final JButton         button;
    private Color                 currentColor;

    public ColorEditor() {

        // Set up the editor (from the table's point of view),
        // which is a button.
        // This button brings up the color chooser dialog,
        // which is the editor from the user's point of view.
        button = new JButton();
        button.setActionCommand( EDIT );
        button.addActionListener( this );
        button.setBorderPainted( false );
    }

    /**
     * Handles events from the editor button and from
     * the dialog's OK button.
     */
    public void actionPerformed( ActionEvent e ) {

        // The user has clicked the cell, so
        // bring up the dialog.
        button.setBackground( currentColor );

        if( currentColor == null ) {
            currentColor = new Color( 255, 255, 255, 0 );
        }

        currentColor = ColorPicker.showDialog( null, currentColor, true );

        // Make the renderer reappear.
        fireEditingStopped();
    }

    // Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return currentColor;
    }

    // Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        currentColor = (Color) value;

        return button;
    }
}
