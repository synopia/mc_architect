size_x  = 187
size_y  = 155
mid_x   = size_x/2
mid_y   = size_y/2

@builder.origin -607-mid_y, -15-mid_x, 65
@builder.create 187, 155, 30

def sphere xm, ym, zm, r
  (-r..+r).each do |x|
    (-r..+r).each do |y|
      (-r..+r).each do |z|
        #(x-xm)**2 + (y-ym)**2 +
        dist = Math.sqrt( x**2 + y**2 + z**2 )
        if dist<r
          @builder.set_pixel xm + x, ym + y, zm + z, 1
        end
      end
    end
  end
end


def helix xm, ym, zm, r, a, b, c, offset=0
  (0..a*360).each do |deg|
    rad = (deg) / 180.0 * Math::PI
    z = c * rad

    rad += offset/180.0 * Math::PI
    x = r * Math.cos( b * rad )
    y = r * Math.sin( b * rad )
    @builder.set_pixel xm + x, ym + y, zm + z, 1
  end
end

#helix mid_x, mid_y, 5, 7,3, 1, 1.5, 90
#helix mid_x, mid_y, 5, 7,3, 1, 1.5, 270

sphere mid_x, mid_y,15, 30