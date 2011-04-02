java_import "de.funky_clan.mc.config.DataValues"

@mid_x = 1705
@mid_y = 65
@mid_z = 188

def info
  {
    :name   => "AKW",
    :author => "synopia",
    :mid    => [@mid_x, @mid_y, @mid_z]
  }
end

def run
puts "Hello from Run"
@xx.x
    @model.clear_blueprint

    @slice_x.set_origin @mid_z,-@mid_y-32,@mid_x
    @slice_x.set_pixel 1,-33,0,1
    @slice_x.set_pixel 0,-34,0,1
    @slice_x.set_pixel 0,-33,1,1
    @slice_x.set_pixel -1,-33,0,1
    @slice_x.set_pixel 0,-32,0,1
    @slice_x.set_pixel 0,-33,-1,1

    @slice_x.set_scale 1, -1, -1

    @binvox.load @slice_x, "akw.binvox", 155, 166, 108, 379, 225, 309, DataValues::SAND.id
end
