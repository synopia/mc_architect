java_import "de.funky_clan.mc.config.DataValues"

def run
    puts "running"
    @model.clear_blueprint

    mid_x  = 0# 1116
    mid_y  = 64 # height
    mid_z  = 0#27

    @schematic.load @world, "Skull_and_body_hollow.schematic", mid_x, mid_y, mid_z
end

def info
    puts "XYZ"
    {
        :name=>"Dino skelett",
        :author=>"winddelay"
    }
end

puts "done"