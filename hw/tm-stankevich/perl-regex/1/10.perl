
while (<>) {
  print if /(^|\W)(\w+)\g2($|\W)/;
}
