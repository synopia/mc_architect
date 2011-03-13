package de.funky_clan.mc.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author synopia
 */
public class PixelTypeTest {
    @Test
    public void testPixel() {
        int data = 0;

        data = PixelType.BLUEPRINT.set(data, 1);
        assertEquals( 1, PixelType.BLUEPRINT.get(data) );

        data = PixelType.BLOCK_ID.set(data, 50);
        assertEquals( 1, PixelType.BLUEPRINT.get(data) );
        assertEquals( 50, PixelType.BLOCK_ID.get(data) );


        data = PixelType.BLUEPRINT.set(data, 0);
        assertEquals( 0, PixelType.BLUEPRINT.get(data) );
        assertEquals( 50, PixelType.BLOCK_ID.get(data) );

        data = PixelType.BLOCK_ID.set(data, 55);
        assertEquals( 0, PixelType.BLUEPRINT.get(data) );
        assertEquals( 55, PixelType.BLOCK_ID.get(data) );

    }
}
