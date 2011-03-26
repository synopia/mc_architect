package de.funky_clan.mc.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author synopia
 */
public class ChunkTest {
    @Test
    public void testChunkIds() {
        long id = Chunk.getChunkId(1,1);
        assertEquals( 1, Chunk.getChunkXForId(id) );
        assertEquals( 1, Chunk.getChunkYForId(id) );

        id = Chunk.getChunkId(1,2);
        assertEquals( 1, Chunk.getChunkXForId(id) );
        assertEquals( 2, Chunk.getChunkYForId(id) );

        id = Chunk.getChunkId(-1,2);
        assertEquals( -1, Chunk.getChunkXForId(id) );
        assertEquals( 2, Chunk.getChunkYForId(id) );

        id = Chunk.getChunkId(-1,-2);
        assertEquals( -1, Chunk.getChunkXForId(id) );
        assertEquals( -2, Chunk.getChunkYForId(id) );

        id = Chunk.getChunkId(0xfffffffe,0xfffffffe);
        assertEquals( 0xfffffffe, Chunk.getChunkXForId(id) );
        assertEquals( 0xfffffffe, Chunk.getChunkYForId(id) );

        id = Chunk.getChunkId(-0xfffffffe,-0xfffffffe);
        assertEquals( -0xfffffffe, Chunk.getChunkXForId(id) );
        assertEquals( -0xfffffffe, Chunk.getChunkYForId(id) );
    }
}
