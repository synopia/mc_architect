java_import "de.funky_clan.mc.model.SliceType"
java_import "de.funky_clan.mc.config.DataValues"


@mid_x = 607
@mid_y = 64
@mid_z = -15

@size_x  = 187
@size_z  = 155

def info
  {
    :name   => "Kolloseum",
    :author => "synopia",
    :mid    => [@mid_x, @mid_y, @mid_z]
  }
end

def run
    level = 0
    @profile.each do |line|
      line.chars.each_with_index do |c, r|
        next unless c=="X"
        @slice_z.ellipse @mid_x, @mid_z, level+@mid_y, @size_z/2-r-1, @size_x/2-r-1, DataValues::GLASS.id
      end
      level += 1
    end
end


@profile = <<EOL
 XXXXXXXXXXX
 XXX     XX
 XXX     XX
 XXX     XX
 XXX     XX
 XXX     XX
 XXX     XX
 XXX     XX
 XXX     XX
 XXXXXXXXXXX
 XXXX    XXX
 XXXX    XXXX
 XXXX    XXX XX
 XXXX    XXX   XX
 XXXXXXXXXXXXXXXX
 XXXXX  XXXXX XXX
 XXXX    XXX   XX
 XXXX    XXX   XX
 XXXX    XXX   XX
 XXXXXXXXXXXXXXXX
 XXXXX  XXXXX XXXX
 XXXX    XXX   XXXX
 XXXX    XXX   XXXXX
 XXXX    XXX   XXXXXX
 XXXX    XXX   XXXXXXX
 XXXX    XXX   XX  XXXX
 XXXX    XXX   XX    XXX
 XXXX    XXX   XX      XXX
 XXXX    XXX   XX        XXX
 XXXX    XXX   XX          XXX
 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX_
 XXXXXXXXXXXX XXX           XXX XXX_
 XXXXX  XXXX   XX           XX   XXXX_
 XXXX    XXX   XX           XX   XX  XX_
 XXXX    XXX   XX           XX   XX    XX_
 XXXX    XXX   XX           XX   XX      XX_
 XXXX    XXX   XX           XX   XX        XX_
 XXXX    XXX   XX           XX   XX          XX_
 XXXX    XXX   XX           XX   XX            XX
 XXXX    XXX   XX           XX   XX            XX
 XXXX    XXX   XX           XX   XX            XX
 XXXX    XXX   XX           XX   XX            XX
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
X                                               X
X                                               X
X                                               X
EOL

@profile = @profile.split("\n").reverse
