package de.funky_clan.mc.ui;

import bibliothek.gui.dock.common.action.CCheckBox;
import bibliothek.gui.dock.common.action.CMenu;
import com.google.inject.Inject;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.events.swing.OreFilterChanged;
import de.funky_clan.mc.model.Ore;
import de.funky_clan.mc.services.ImageService;

import javax.swing.JComponent;

/**
 * @author synopia
 */
public class OrePanel {
    EventDispatcher   eventDispatcher;
    private boolean[] filter;
    @Inject
    ImageService      imageService;

    @Inject
    public OrePanel( final EventDispatcher eventDispatcher ) {
        this.eventDispatcher = eventDispatcher;

        int length = Ore.OreType.values().length;

        filter = new boolean[length];
    }

    public CMenu getMenu( final JComponent component ) {
        CMenu menu = new CMenu( "Ore", imageService.getIcon( DataValues.DIAMONDORE ));

        for( final Ore.OreType oreType : Ore.OreType.values() ) {
            if( oreType == Ore.OreType.NO_ORE ) {
                continue;
            }

            menu.add( new CCheckBox( oreType.toString(),
                                     imageService.getIcon( DataValues.find( oreType.getBlockId() ))) {
                @Override
                protected void changed() {
                    filter[oreType.ordinal()] = this.isSelected();
                    eventDispatcher.fire( new OreFilterChanged( component, filter ));
                }
            } );
        }

        return menu;
    }
}
