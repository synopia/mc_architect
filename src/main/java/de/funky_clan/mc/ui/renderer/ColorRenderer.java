package de.funky_clan.mc.ui.renderer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * @author synopia
 */
public class ColorRenderer extends JLabel implements TableCellRenderer {
    Border  selectedBorder   = null;
    Border  unselectedBorder = null;
    boolean isBordered       = true;

    public ColorRenderer( boolean isBordered ) {
        this.isBordered = isBordered;
        setOpaque( true );    // MUST do this for background to show up.
    }

    public Component getTableCellRendererComponent( JTable table, Object color, boolean isSelected, boolean hasFocus,
            int row, int column ) {
        Color newColor = (Color) color;

        setBackground( newColor );

        if( isBordered ) {
            if( isSelected ) {
                if( selectedBorder == null ) {
                    selectedBorder = BorderFactory.createMatteBorder( 2, 5, 2, 5, table.getSelectionBackground() );
                }

                setBorder( selectedBorder );
            } else {
                if( unselectedBorder == null ) {
                    unselectedBorder = BorderFactory.createMatteBorder( 2, 5, 2, 5, table.getBackground() );
                }

                setBorder( unselectedBorder );
            }
        }

        if( newColor != null ) {
            setToolTipText( "RGBA value: " + newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue()
                            + ", " + newColor.getAlpha() );
        }

        return this;
    }
}
