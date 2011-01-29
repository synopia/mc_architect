// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SourceFile

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import de.funky_clan.mc.net.MineCraftServerThread;
import de.funky_clan.mc.net.ServerThread;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class pe extends or
{
    private MineCraftServerThread serverThread;

    public pe(Minecraft minecraft)
    {
        serverThread = new MineCraftServerThread(12345);
        serverThread.start();
        e = new ArrayList();
        f = new Random();
        a = null;
        h = 0;
        i = "";
        k = 0;
        c = 1.0F;
        g = minecraft;
    }

    public void a(float f1, boolean flag, int j, int l)
    {
        lw lw1 = new lw(g.c, g.d);
        int i1 = lw1.a();
        int j1 = lw1.b();
        nh nh1 = g.o;
        g.r.c();
        GL11.glEnable(3042);
        if(g.y.i)
            a(g.g.a(f1), i1, j1);
        gm gm1 = g.g.f.d(3);
        if(!g.y.x && gm1 != null && gm1.c == pj.ba.bi)
            a(i1, j1);
        float f2 = g.g.e + (g.g.d - g.g.e) * f1;
        if(f2 > 0.0F)
            b(f2, i1, j1);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, g.n.a("/gui/gui.png"));
        gl gl1 = g.g.f;
        this.j = -90F;
        b(i1 / 2 - 91, j1 - 22, 0, 0, 182, 22);
        b((i1 / 2 - 91 - 1) + gl1.c * 20, j1 - 22 - 1, 0, 22, 24, 22);
        GL11.glBindTexture(3553, g.n.a("/gui/icons.png"));
        GL11.glEnable(3042);
        GL11.glBlendFunc(775, 769);
        b(i1 / 2 - 7, j1 / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(3042);
        boolean flag1 = (g.g.bs / 3) % 2 == 1;
        if(g.g.bs < 10)
            flag1 = false;
        int k1 = g.g.S;
        int l1 = g.g.T;
        f.setSeed(h * 0x4c627);
        if(g.b.d())
        {
            int i2 = g.g.q();
            for(int k2 = 0; k2 < 10; k2++)
            {
                int l3 = j1 - 32;
                if(i2 > 0)
                {
                    int j5 = (i1 / 2 + 91) - k2 * 8 - 9;
                    if(k2 * 2 + 1 < i2)
                        b(j5, l3, 34, 9, 9, 9);
                    if(k2 * 2 + 1 == i2)
                        b(j5, l3, 25, 9, 9, 9);
                    if(k2 * 2 + 1 > i2)
                        b(j5, l3, 16, 9, 9, 9);
                }
                int k5 = 0;
                if(flag1)
                    k5 = 1;
                int i6 = (i1 / 2 - 91) + k2 * 8;
                if(k1 <= 4)
                    l3 += f.nextInt(2);
                b(i6, l3, 16 + k5 * 9, 0, 9, 9);
                if(flag1)
                {
                    if(k2 * 2 + 1 < l1)
                        b(i6, l3, 70, 0, 9, 9);
                    if(k2 * 2 + 1 == l1)
                        b(i6, l3, 79, 0, 9, 9);
                }
                if(k2 * 2 + 1 < k1)
                    b(i6, l3, 52, 0, 9, 9);
                if(k2 * 2 + 1 == k1)
                    b(i6, l3, 61, 0, 9, 9);
            }

            if(g.g.a(ic.f))
            {
                int i3 = (int)Math.ceil(((double)(g.g.bt - 2) * 10D) / 300D);
                int i4 = (int)Math.ceil(((double)g.g.bt * 10D) / 300D) - i3;
                for(int l5 = 0; l5 < i3 + i4; l5++)
                    if(l5 < i3)
                        b((i1 / 2 - 91) + l5 * 8, j1 - 32 - 9, 16, 18, 9, 9);
                    else
                        b((i1 / 2 - 91) + l5 * 8, j1 - 32 - 9, 25, 18, 9, 9);

            }
        }
        GL11.glDisable(3042);
        GL11.glEnable(32826);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
        p.b();
        GL11.glPopMatrix();
        for(int j2 = 0; j2 < 9; j2++)
        {
            int j3 = (i1 / 2 - 90) + j2 * 20 + 2;
            int j4 = j1 - 16 - 3;
            a(j2, j3, j4, f1);
        }

        p.a();
        GL11.glDisable(32826);
        if(Keyboard.isKeyDown(61))
        {
            nh1.a((new StringBuilder()).append("Minecraft Beta 1.2_02 (").append(g.I).append(")").toString(), 2, 2, 0xffffff);
            nh1.a(g.m(), 2, 12, 0xffffff);
            nh1.a(g.n(), 2, 22, 0xffffff);
            nh1.a(g.p(), 2, 32, 0xffffff);
            nh1.a(g.o(), 2, 42, 0xffffff);
            long l2 = Runtime.getRuntime().maxMemory();
            long l4 = Runtime.getRuntime().totalMemory();
            long l6 = Runtime.getRuntime().freeMemory();
            long l7 = l4 - l6;
            String s = (new StringBuilder()).append("Used memory: ").append((l7 * 100L) / l2).append("% (").append(l7 / 1024L / 1024L).append("MB) of ").append(l2 / 1024L / 1024L).append("MB").toString();
            b(nh1, s, i1 - nh1.a(s) - 2, 2, 0xe0e0e0);
            s = (new StringBuilder()).append("Allocated memory: ").append((l4 * 100L) / l2).append("% (").append(l4 / 1024L / 1024L).append("MB)").toString();
            b(nh1, s, i1 - nh1.a(s) - 2, 12, 0xe0e0e0);
            b(nh1, (new StringBuilder()).append("x: ").append(g.g.aF).toString(), 2, 64, 0xe0e0e0);
            b(nh1, (new StringBuilder()).append("y: ").append(g.g.aG).toString(), 2, 72, 0xe0e0e0);
            b(nh1, (new StringBuilder()).append("z: ").append(g.g.aH).toString(), 2, 80, 0xe0e0e0);
        } else
        {
            nh1.a("Minecraft Beta 1.2_02", 2, 2, 0xffffff);
        }
        if(k > 0)
        {
            float f3 = (float)k - f1;
            int k3 = (int)((f3 * 256F) / 20F);
            if(k3 > 255)
                k3 = 255;
            if(k3 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i1 / 2, j1 - 48, 0.0F);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                int k4 = Color.HSBtoRGB(f3 / 50F, 0.7F, 0.6F) & 0xffffff;
                nh1.b(i, -nh1.a(i) / 2, -4, k4 + (k3 << 24));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }
        byte byte0 = 10;
        boolean flag2 = false;
        if(g.p instanceof ei)
        {
            byte0 = 20;
            flag2 = true;
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j1 - 48, 0.0F);
        for(int i5 = 0; i5 < e.size() && i5 < byte0; i5++)
        {
            if(((nt)e.get(i5)).b >= 200 && !flag2)
                continue;
            double d1 = (double)((nt)e.get(i5)).b / 200D;
            d1 = 1.0D - d1;
            d1 *= 10D;
            if(d1 < 0.0D)
                d1 = 0.0D;
            if(d1 > 1.0D)
                d1 = 1.0D;
            d1 *= d1;
            int j6 = (int)(255D * d1);
            if(flag2)
                j6 = 255;
            if(j6 > 0)
            {
                byte byte1 = 2;
                int k6 = -i5 * 9;
                String s1 = ((nt)e.get(i5)).a;
                a(byte1, k6 - 1, byte1 + 320, k6 + 8, j6 / 2 << 24);
                GL11.glEnable(3042);
                nh1.a(s1, byte1, k6, 0xffffff + (j6 << 24));
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        serverThread.render( g );
    }

    private void a(int j, int l)
    {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, g.n.a("%blur%/misc/pumpkinblur.png"));
        jy jy1 = jy.a;
        jy1.b();
        jy1.a(0.0D, l, -90D, 0.0D, 1.0D);
        jy1.a(j, l, -90D, 1.0D, 1.0D);
        jy1.a(j, 0.0D, -90D, 1.0D, 0.0D);
        jy1.a(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        jy1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void a(float f1, int j, int l)
    {
        f1 = 1.0F - f1;
        if(f1 < 0.0F)
            f1 = 0.0F;
        if(f1 > 1.0F)
            f1 = 1.0F;
        c += (double)(f1 - c) * 0.01D;
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(0, 769);
        GL11.glColor4f(c, c, c, 1.0F);
        GL11.glBindTexture(3553, g.n.a("%blur%/misc/vignette.png"));
        jy jy1 = jy.a;
        jy1.b();
        jy1.a(0.0D, l, -90D, 0.0D, 1.0D);
        jy1.a(j, l, -90D, 1.0D, 1.0D);
        jy1.a(j, 0.0D, -90D, 1.0D, 0.0D);
        jy1.a(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        jy1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(770, 771);
    }

    private void b(float f1, int j, int l)
    {
        f1 *= f1;
        f1 *= f1;
        f1 = f1 * 0.8F + 0.2F;
        GL11.glDisable(3008);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
        GL11.glBindTexture(3553, g.n.a("/terrain.png"));
        float f2 = (float)(pj.be.bh % 16) / 16F;
        float f3 = (float)(pj.be.bh / 16) / 16F;
        float f4 = (float)(pj.be.bh % 16 + 1) / 16F;
        float f5 = (float)(pj.be.bh / 16 + 1) / 16F;
        jy jy1 = jy.a;
        jy1.b();
        jy1.a(0.0D, l, -90D, f2, f5);
        jy1.a(j, l, -90D, f4, f5);
        jy1.a(j, 0.0D, -90D, f4, f3);
        jy1.a(0.0D, 0.0D, -90D, f2, f3);
        jy1.a();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void a(int j, int l, int i1, float f1)
    {
        gm gm1 = g.g.f.a[j];
        if(gm1 == null)
            return;
        float f2 = (float)gm1.b - f1;
        if(f2 > 0.0F)
        {
            GL11.glPushMatrix();
            float f3 = 1.0F + f2 / 5F;
            GL11.glTranslatef(l + 8, i1 + 12, 0.0F);
            GL11.glScalef(1.0F / f3, (f3 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(l + 8), -(i1 + 12), 0.0F);
        }
        d.a(g.o, g.n, gm1, l, i1);
        if(f2 > 0.0F)
            GL11.glPopMatrix();
        d.b(g.o, g.n, gm1, l, i1);
    }

    public void a()
    {
        if(k > 0)
            k--;
        h++;
        for(int j = 0; j < e.size(); j++)
            ((nt)e.get(j)).b++;

    }

    public void a(String s)
    {
        int j;
        for(; g.o.a(s) > 320; s = s.substring(j))
        {
            for(j = 1; j < s.length() && g.o.a(s.substring(0, j + 1)) <= 320; j++);
            a(s.substring(0, j));
        }

        e.add(0, new nt(s));
        for(; e.size() > 50; e.remove(e.size() - 1));
    }

    public void b(String s)
    {
        i = (new StringBuilder()).append("Now playing: ").append(s).toString();
        k = 60;
    }

    private static al d = new al();
    private java.util.List e;
    private Random f;
    private Minecraft g;
    public String a;
    private int h;
    private String i;
    private int k;
    public float b;
    float c;

}
