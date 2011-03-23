java_import "de.funky_clan.mc.model.SliceType"

mid_x = 607
mid_y = 64
mid_z = -14



def sphere g, r
  (-r..+r).each do |x|
    (-r..+r).each do |y|
      (-r..+r).each do |z|
        dist = Math.sqrt( x**2 + y**2 + z**2 )
        if dist<r
          g.set_pixel x,y,z, 1
        end
      end
    end
  end
end


def helix g, r, a, b, c, offset=0
  (0..a*360).each do |deg|
    rad = (deg) / 180.0 * Math::PI
    z = c * rad

    rad += offset/180.0 * Math::PI
    x = r * Math.cos( b * rad )
    y = r * Math.sin( b * rad )
    g.set_pixel x, y, z, 1
  end
end

#helix 7,3, 1, 1.5, 90
#helix 7,3, 1, 1.5, 270

@world.origin mid_x, mid_z, mid_y
sphere @world, 30
