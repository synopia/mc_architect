java_import "de.funky_clan.mc.config.DataValues"

@mid_x = 1135
@mid_y = 65
@mid_z = -131

def info
  {
    :name   => "Spider",
    :author => "tmp",
    :mid    => [@mid_x, @mid_y, @mid_z]
  }
end

def run
    puts "Hallo Spinne"
    @model.clear_blueprint
	@world.set_origin @mid_x,@mid_y,@mid_z
    @world.set_scale 1, 1, -1
    @binvox.load @world, "spider.binvox",   0, 0, 0, 64,64,64, DataValues::SAND.id
end
