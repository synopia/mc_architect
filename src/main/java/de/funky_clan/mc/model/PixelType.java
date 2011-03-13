package de.funky_clan.mc.model;

/**
 * @author synopia
 */
public enum PixelType {
    BLOCK_ID(8),
    BLUEPRINT(1);

    private static int bitLength = 0;
    private int bits;
    private int startBit;
    private int mask;

    PixelType(int bits) {
        this.startBit = currentBitPosition(bits);
        this.bits = bits;
        mask = ((1<<bits)-1)<<startBit;
    }

    public int set( int current, int value ) {
        int result = current & ~mask;
        result += value<<startBit;
        return result;
    }

    public int get( int current ) {
        return (current & mask)>>startBit;
    }

    public static int currentBitPosition(int bits) {
        int curr = bitLength;
        bitLength += bits;
        return curr;
    }
}
