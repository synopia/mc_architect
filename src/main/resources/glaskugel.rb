# you have access to the following objects:
#
#  * @model
#       full access to the world model (be careful...)
#  * @world
#       a "graphics" object, with some neat stuff like set_origin or ellipse
#  * @slice_x, @slice_y, @slice_z
#       works the same like @world, but with switched coordinate systems
#       when you set a pixel using @slice_y.set_pixel(x,y,z,1), it will be placed at x,z,y in world coordinates
#       y in world coordinate means up
#  * @binvox
#       experimental binvox loader
#
java_import "de.funky_clan.mc.config.DataValues"

@mid_x = 1217
@mid_y = 64
@mid_z = -50

# this method gets called to gather some info about this script
def info
  {
    :name   => "Glaskugel",
    :author => "synopia",
    :mid    => [@mid_x, @mid_y, @mid_z]
  }
end

# this is called when "running" the script
def run
  @world.set_origin @mid_x, @mid_y, @mid_z
  sphere @world, 63, DataValues::GLASS.id
end

# a simple sphere
def sphere g, r, type
  (-r..+r).each do |x|
    (-r..+r).each do |y|
      (-r..+r).each do |z|
        dist = Math.sqrt( x**2 + y**2 + z**2 )
        if (r-dist).abs<0.5
          g.set_pixel x,y,z, type
        end
      end
    end
  end
end
