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
 XXXXX _XXXXX_XXXX
 XXXX ___XXX___XXXX
 XXXX ___XXX___XXXXX
 XXXX ___XXX___XXXXXX
 XXXX ___XXX___XXXXXXX
 XXXX ___XXX___XX  XXXX
 XXXX ___XXX___XX  __XXX
 XXXX ___XXX___XX   ___XXX
 XXXX ___XXX___XX    ____XXX
 XXXX ___XXX___XX      ____XXX
 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 XXXXXXXXXXXX XXX      ____ XXX_XXX
 XXXXX  XXXX _ XX      ____ XX _ XXXX
 XXXX  _ XXX___XX      ____ XX___XX_ XX
 XXXX ___XXX___XX      ____ XX___XX_   XX
 XXXX ___XXX___XX      ____ XX___XX_     XX
 XXXX ___XXX___XX      ____ XX___XX_       XX
 XXXX ___XXX___XX      ____ XX___XX_         XX
 XXXX ___XXX___XX      ____ XX___XX_           XX
 XXXX ___XXX___XX      ____ XX___XX_           XX
 XXXX _ _XXX___XX      ____ XX___XX_           XX
 XXXX _ _XXX___XX      ____ XX___XX_           XX
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

@builder.origin 607-mid_y, -15-mid_x, 65

@builder.create 187, 155, profile.size

@builder.image  4,  7, "level1.png"
@builder.image  8, 15, "level2.png"
@builder.image 16, 32, "level3.png"
@builder.image 26, 41, "level4.png"

level = 0
profile.each do |line|
  line.chars.each_with_index do |c, r|
    next unless c=="X"
    @builder.draw_ellipse level, mid_x, mid_y, mid_x-r-1, mid_y-r-1
  end
  level += 1
end

@builder.axis mid_x, mid_y