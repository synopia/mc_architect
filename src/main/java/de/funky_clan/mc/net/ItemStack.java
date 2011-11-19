package de.funky_clan.mc.net;

/**
 * @author synopia
 */
public class ItemStack {
    private short           itemId;
    private byte            itemCount;
    private short           itemUses;
    private byte[]          data;

    public ItemStack(short itemId, byte itemCount, short itemUses) {
        this.itemId = itemId;
        this.itemCount = itemCount;
        this.itemUses = itemUses;
    }

    public short getItemId() {
        return itemId;
    }

    public byte getItemCount() {
        return itemCount;
    }

    public short getItemUses() {
        return itemUses;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
