java_import "de.funky_clan.mc.config.DataValues"

@mid_x = -500
@mid_y = 30
@mid_z = 145

def info
  {
    :name   => "Nagoya",
    :author => "synopia",
    :mid    => [@mid_x, @mid_y, @mid_z]
  }
end

def run
    @model.clear_blueprint

    @slice_x.set_origin @mid_z,-@mid_y-62,@mid_x

    @slice_x.set_scale 1, -1, -1

    #@binvox.load @slice_x, "nagoya.binvox",  23,6,34, 127,127,98, DataValues::SAND.id
    @binvox.load @slice_x, "nagoya.binvox",  23,6,6, 127,127,127, DataValues::SAND.id
end
