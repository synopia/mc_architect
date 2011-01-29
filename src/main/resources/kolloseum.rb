profile = <<EOL
  XXXXXXXXXX
  XXX    XX
  XXX    XX
  XXX    XX
  XXX    XX
  XXX    XX
  XXX    XX
  XXX    XX
  XXX    XX
  XXXXXXXXXX
  XXXX   XXX
  XXXX   XXXX
  XXXX   XXX XX
  XXXX   XXX   XX
  XXXXXXXXXXXXXXX
  XXXXX XXXXX XXX
  XXXX   XXX   XX
  XXXX   XXX   XX
  XXXX   XXX   XX
  XXXXXXXXXXXXXXX
  XXXXX_XXXXX_XXXX
  XXXX___XXX___XXXX
  XXXX___XXX___XXXXX
  XXXX___XXX___XXXXXX
  XXXX___XXX___XXXXXXX
  XXXX___XXX___XX  XXXX
  XXXX___XXX___XX  __XXX
  XXXX___XXX___XX   ___XXX
  XXXX___XXX___XX    ____XXX
  XXXX___XXX___XX      ____XXX
  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  XXXXX XXXXX XXX      ____ XXX_XXX
  XXXX   XXX _ XX      ____ XX _ XXXX
  XXXX _ XXX___XX      ____ XX___XX_ XX
  XXXX___XXX___XX      ____ XX___XX_   XX
  XXXX___XXX___XX      ____ XX___XX_     XX
  XXXX___XXX___XX      ____ XX___XX_       XX
  XXXX___XXX___XX      ____ XX___XX_         XX
  XXXX___XXX___XX      ____ XX___XX_           XX
  XXXX___XXX___XX      ____ XX___XX_           XX
  XXXX___XXX___XX      ____ XX___XX_           XX
  XXXX___XXX___XX      ____ XX___XX_           XX
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

level = 0
profile.each do |line|
  line.chars.each_with_index do |c, r|
    next unless c=="X"
    @builder.draw_ellipse level, mid_x, mid_y, mid_x-r-1, mid_y-r-1
  end
  level += 1
end

@builder.axis mid_x, mid_y