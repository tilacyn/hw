
while (<>) {
  s/\([^\)]*\)/()/g;
  print;
}
