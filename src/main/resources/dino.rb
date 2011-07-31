java_import "de.funky_clan.mc.config.DataValues"

def run
    puts "running"
    @model.clear_blueprint

    mid_x  =960
    mid_y  = 90 # height
    mid_z  = -490

    @schematic.load @world, "Skull_and_body_hollow.schematic", mid_x, mid_y, mid_z, -1, 1,1
end

def info
    puts "XYZ"
    {
        :name=>"Dino skelett",
        :author=>"winddelay"
    }
end

puts "done"