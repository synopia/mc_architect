java_import "de.funky_clan.mc.config.DataValues"
@mid_x = 607
@mid_y = 64
@mid_z = -15

def info
  {
    :name   => "Superformula",
    :author => "synopia",
    :mid    => [@mid_x, @mid_y, @mid_z]
  }
end

def run
    @model.clear_blueprint
    a=1 # a
    b=1 # b
    step = 0.005

    m=4  #m
    n1=100
    n2=100
    n3=100


    i=-Math::PI
    while i<Math::PI
      j=-Math::PI/2
      while j<Math::PI/2
        raux1=pow(abs(1/a*abs(cos(m*i/4))),n2)+pow(abs(1/b*abs(sin(m*i/4))),n3);
        r1=pow(abs(raux1),(-1/n1));
        raux2=pow(abs(1/a*abs(cos(m*j/4))),n2)+pow(abs(1/b*abs(sin(m*j/4))),n3);
        r2=pow(abs(raux2),(-1/n1));
        xx=r1*cos(i)*r2*cos(j)*10;
        yy=r1*sin(i)*r2*cos(j)*10;
        zz=r2*sin(j)*10;

        @world.set_pixel @mid_x+xx, @mid_y+yy, @mid_z+zz, DataValues::STONE.id
        j += step
      end
      i += step
    end
end

def pow x, a
  return x**a
end

def abs a
  return -a if a<0
  a
end

def cos a
  Math::cos(a)
end
def sin a
  Math::sin(a)
end
