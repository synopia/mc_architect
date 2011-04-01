package de.funky_clan.mc.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import de.funky_clan.mc.util.StatusBar;

/**
 * @author synopia
 */
public class ArchitectModule implements Module {
    @Override
    public void configure( Binder binder ) {
        binder.bind( StatusBar.class ).annotatedWith( Names.named( "Status" )).toInstance( new StatusBar() );
        binder.bind( StatusBar.class ).annotatedWith( Names.named( "Info" )).toInstance( new StatusBar() );
    }
}
