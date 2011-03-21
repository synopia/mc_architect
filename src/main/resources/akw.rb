# -214, 66, -730
java_import "de.funky_clan.mc.config.DataValues"

a = b = 10.0
c = 25.0

a_sq = a**2
b_sq = b**2
c_sq = c**2

(-20..40).each do |z|
  (-25..25).each do |x|
    (-25..25).each do |y|
      value = x**2 / a_sq + y**2 / b_sq - z**2/c_sq
      if (value-1).abs<0.3
        @world.set_pixel x, 40-z, y, DataValues::STATIONARYLAVA.id
      end
    end
  end
end


#@slice_z.ellipse 0,0,0, 20, 20, DataValues::STATIONARYLAVA.id
#@slice_z.ellipse 0,0,1, 20, 20, DataValues::STATIONARYLAVA.id
#@slice_z.ellipse 0,0,2, 20, 20, DataValues::STATIONARYLAVA.id