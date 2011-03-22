
def hyperboloid xm, ym, zm, a, b, c
  a_sq = a**2
  b_sq = b**2
  c_sq = c**2

  (-20..40).each do |y|
    (-25..25).each do |x|
      (-25..25).each do |z|
        value = x**2 / a_sq + z**2 / b_sq - y**2/c_sq
        if (value-1).abs<0.3
          @world.set_pixel xm+x, ym+40-y, zm+z, 1
        end
      end
    end
  end
end

def cylinder xm, ym, zm, radius, height
  (0..height).each do |h|
    @slice_z.ellipse xm, zm, h+ym, radius, radius, 1
  end
end

def half_sphere xm, ym, zm, radius
  (0..radius).each do |y|
    (-radius..radius).each do |x|
      (-radius..radius).each do |z|
        value = Math.sqrt( z**2 + x**2 + y**2 )
        if( value-radius).abs<0.8
          @world.set_pixel xm+x, ym+y, zm+z, 1
        end
      end
    end
  end
end


xm, ym, zm = -220, 63, -880
#xm, ym, zm = 0,0,0

a = b = 10.0
c = 25.0
@model.clear_blueprint

hyperboloid  xm+25, ym+0, zm+25, a, b, c
hyperboloid  xm+75, ym+0, zm+25, a, b, c

half_sphere xm+50-15,ym+10,zm+60,10
half_sphere xm+50+15,ym+10,zm+60,10

cylinder xm+50-15,ym+0,zm+60,10, 10
cylinder xm+50+15,ym+0,zm+60,10, 10
