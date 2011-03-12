profile = <<EOL
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

java_import "de.funky_clan.mc.model.SliceType"

profile = profile.split("\n").reverse

mid_x = 607
mid_y = 64
mid_z = -14

size_x  = 187
size_z  = 155

@builder.origin mid_x, mid_z, mid_y, SliceType::Z

@builder.image  4,  7, "level1.png"
@builder.image  8, 15, "level2.png"
@builder.image 16, 32, "level3.png"
@builder.image 26, 41, "level4.png"

level = 0
profile.each do |line|
  line.chars.each_with_index do |c, r|
    next unless c=="X"
    @builder.ellipse level, 0, 0, size_z/2-r-1, size_x/2-r-1
  end
  level += 1
end

#@builder.axis mid_x, mid_y