package de.funky_clan.mc.ui;

import bibliothek.gui.dock.common.action.CCheckBox;
import bibliothek.gui.dock.common.action.CMenu;
import com.google.inject.Inject;
import de.funky_clan.mc.config.EntityType;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.events.swing.EntityFilterChanged;
import de.funky_clan.mc.services.ImageService;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class EntityPanel {
    private final EventDispatcher eventDispatcher;
    private final boolean[]       filter;
    @Inject
    private ImageService imageService;
    private List<CCheckBox> all = new ArrayList<CCheckBox>();
    private boolean setAllMode;

    @Inject
    public EntityPanel(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        filter = new boolean[EntityType.values().length];
        filter[EntityType.PLAYER.ordinal()] = true;
        filter[EntityType.SMP_PLAYER.ordinal()] = true;
    }
    
    public CMenu getMenu( final JComponent component ) {
        eventDispatcher.publish(new EntityFilterChanged(component, filter));

        final CMenu menu = new CMenu( "Entity", getIcon(EntityType.CREEPER) );
        all.clear();
        menu.add(new CCheckBox("all", null) {
            @Override
            protected void changed() {
                setAllMode = true;
                for (CCheckBox box : all) {
                    box.setSelected( this.isSelected() );
                }
                eventDispatcher.publish(new EntityFilterChanged(component, filter));
                setAllMode = false;
            }
        });
        menu.addSeparator();
        for (final EntityType type : EntityType.values()) {
            if( type==EntityType.PLAYER || type==EntityType.SMP_PLAYER ) {
                continue;
            }
            CCheckBox action = new CCheckBox(type.toString(), getIcon(type)) {
                @Override
                protected void changed() {
                    filter[type.ordinal()] = this.isSelected();
                    if( !setAllMode ) {
                        eventDispatcher.publish(new EntityFilterChanged(component, filter));
                    }
                }
            };
            menu.add(action);
            all.add(action);
        }
        return menu;
    }

    private ImageIcon getIcon( EntityType type ) {
        return new ImageIcon(imageService.getImage(type).getScaledInstance(16, 16, BufferedImage.SCALE_FAST));
    }
}
