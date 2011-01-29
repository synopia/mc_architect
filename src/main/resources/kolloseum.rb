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
 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 XXXXXXXXXXXX XXX           XXX XXX
 XXXXX  XXXX   XX           XX   XXXX
 XXXX    XXX   XX           XX   XX  XX
 XXXX    XXX   XX           XX   XX    XX
 XXXX    XXX   XX           XX   XX      XX
 XXXX    XXX   XX           XX   XX        XX
 XXXX    XXX   XX           XX   XX          XX
 XXXX    XXX   XX           XX   XX            XX
 XXXX    XXX   XX           XX   XX            XX
 XXXX    XXX   XX           XX   XX            XX
 XXXX    XXX   XX           XX   XX            XX
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
X                                               X
X                                               X
X                                               X
EOL


profile = profile.split("\n").reverse

size_x  = 187
size_y  = 155
mid_x   = size_x/2
mid_y   = size_y/2

@builder.origin -607-mid_y, -15-mid_x, 65

@builder.create 187, 155, profile.size

@builder.image  4,  7, "level1.png"
@builder.image  8, 15, "level2.png"
@builder.image 16, 32, "level3.png"
@builder.image 26, 41, "level4.png"

level = 0
profile.each do |line|
  line.chars.each_with_index do |c, r|
    next unless c=="X"
    @builder.ellipse level, mid_x, mid_y, mid_x-r-1, mid_y-r-1
  end
  level += 1
end

@builder.axis mid_x, mid_y