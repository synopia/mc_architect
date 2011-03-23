java_import "de.funky_clan.mc.model.SliceType"

mid_x = 1217
mid_y = 64
mid_z = -50

def sphere g, r
  (-r..+r).each do |x|
    (-r..+r).each do |y|
      (-r..+r).each do |z|
        dist = Math.sqrt( x**2 + y**2 + z**2 )
        if (r-dist).abs<0.5
          g.set_pixel x,y,z, 1
        end
      end
    end
  end
end

@world.set_origin mid_x, mid_y, mid_z
sphere @world, 63