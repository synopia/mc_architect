java_import "de.funky_clan.mc.config.DataValues"

puts "Loading schematic"
#Above TorchHeight
@left_x = 686
@left_y = 71
@left_z = 508

# N,W,S,E 
@direction = "S"
@extensions = 1
@slots = 6
# 0.. left, 1..right
@slot_begin = 1

def info
  {
    :name   => "Station Redstone Select Schematic",
    :author => "synopia & attrib",
    :mid    => [@left_x, @left_y, @left_z]
  }
end

def run
	(0..(@slots-1)).each do |slot|
	  x,y,z = direction_correction @left_x,@left_y,@left_z,-15,0,slot
	  #above
	  if (@slot_begin==0)
	    profile = @profile_left_above
      else
	    profile = @profile_right_above
	  end	  
	  schematic  x,y,z,profile
	  y -= 3
	  
	  #extender
	  if (@slot_begin==0)
	    profile = @profile_left_extender
      else
	    profile = @profile_right_extender
	  end	  	  
      (1..@extensions).each do |extension|
	    schematic  x,y,z,profile
		y = y-4*extension
	  end
	  
	  #schematic
	  if (@slot_begin==0)
	    profile = @profile_left
      else
	    profile = @profile_right
	  end	  	  	  
	  schematic  x,y,z,profile
	  
	  if (@slot_begin==0)
	    @slot_begin = 1
      else
	    @slot_begin = 0
	  end
	end
end

def schematic o_x,o_y,o_z,profile
  profile.each_with_index do |line, c_y|
	line.chars.each_with_index do |c, c_x|
	  x,y,z = direction_correction o_x,o_y,o_z,c_x,-c_y,0
      @world.set_pixel x,y,z,get_type(c)
	end
  end
end

def get_type c
  case c
    when "D":
	  type = DataValues::DIRT.id
	  #type = DataValues::STONE.id
	when "R":
	  #type = DataValues::REDSTONEORE.id
	  type = DataValues::GOLDBLOCK.id
	when "T":
	  #type = DataValues::REDSTONETORCHON.id
	  type = DataValues::IRONBLOCK.id
	when "B":
	  #type = DataValues::STONEBUTTON.id
	  type = DataValues::SLAB_STONE.id
	else
	  type = DataValues::AIR.id
  end
  return type
end

def direction_correction o_x,o_y,o_z,corr_x,corr_y,corr_z
  case @direction
    when "N":
      x = o_x + corr_x
      y = o_y + corr_y
	  z = o_z - corr_z
	when "E":
      x = o_z + corr_z
      y = o_y + corr_y
	  z = o_x - corr_x
	when "S":
      x = o_x - corr_x
      y = o_y + corr_y
	  z = o_z + corr_z
	when "W":
      x = o_z - corr_z
      y = o_y + corr_y
	  z = o_x + corr_x
  end
  return x,y,z
end

@profile_left = <<EOL
           T  R  
           D TD  
          DT R   
 RR      DT  DT  
RDDTD   DT    R  
DTDRRRRDT RTDTD  
T  DDDD  TDDRR   
D  T     R  DDT  
T  DRTDRTD    R  
DR  D DDD  D TD  
 DRRTDRRRRRRRR   
EOL

@profile_left = @profile_left.split("\n")

@profile_left_extender = <<EOL
           T  R  
           D TD  
           T R   
           D DT  
EOL

@profile_left_extender = @profile_left_extender.split("\n")

@profile_left_above = <<EOL
             DT  
           RDT   
           D  DB 
EOL

@profile_left_above = @profile_left_above.split("\n")


@profile_right = <<EOL
           T R   
   RRRR   RD DT  
   DDDDT RD   R  
   T TDRRD   TD  
   DRT DD RT R   
    DDDT TDDRDT  
     TRDRR  D R  
     DD DD   TD  
     T D    RR   
     DRRRTDRDDD  
      DDD  D     
EOL

@profile_right = @profile_right.split("\n")

@profile_right_extender = <<EOL
           T R   
           D DT  
           T  R  
           D TD  
EOL

@profile_right_extender = @profile_right_extender.split("\n")

@profile_right_above = <<EOL
            RDT  
           DD    
           DTTDB 
EOL

@profile_right_above = @profile_right_above.split("\n")
