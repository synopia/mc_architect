package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.model.RenderContext;

/**
 * @author synopia
 */
public interface Renderer<T> {
    public void render( T object, RenderContext renderContext );
}
