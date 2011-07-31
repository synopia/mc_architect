java_import "de.funky_clan.mc.config.DataValues"

def run
    puts "running"
    @model.clear_blueprint

    mid_x  = 920
    mid_y  = 90 # height
    mid_z  = -519
    @schematic.load @world, "new_skull_hollow.schematic", mid_x, mid_y, mid_z, 1, 1, -1
end

def info
    puts "XYZ"
    {
        :name=>"Skull",
        :author=>"winddelay"
    }
end

puts "done"