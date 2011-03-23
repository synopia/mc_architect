package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.events.model.OreFilterChanged;
import de.funky_clan.mc.model.Ore;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author synopia
 */
public class OrePanel extends JPanel {
    private EventBus eventBus;
    private JCheckBox[] ores;
    private boolean [] filter;

    @Inject
    public OrePanel(final EventBus eventBus) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.eventBus = eventBus;
        int length = Ore.OreType.values().length;
        filter = new boolean[length];
        ores = new JCheckBox[length];
        for (int i = 1; i < length; i++) {
            final Ore.OreType type = Ore.OreType.values()[i];
            ores[i] = new JCheckBox(new AbstractAction(type.toString()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filter[type.ordinal()] = ((JCheckBox)e.getSource()).isSelected();
                    eventBus.fireEvent( new OreFilterChanged(filter));
                }
            });
            add(ores[i]);
        }
    }
}
