package de.funky_clan.mc.model;

/**
 * @author synopia
 */
public interface Renderable<C extends RenderContext> {
    public void render( C renderContext );
}
