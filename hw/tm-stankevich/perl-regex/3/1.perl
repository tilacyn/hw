#!/usr/bin/perl

$empty="/^\s*$/";

#open (in, "kek");

@strings=<>;

$n = scalar @strings;

$start=0;
$flag=1;


foreach $str (@strings) {
  $str =~ s/<[^<>]*>//g;
}


while ($flag && $start < $n) {
  $str = $strings[$start];
  if ($str =~ /^\s*$/) {
  
  } else {
     $flag=0;
  }
  $start++;
}
$start--;

$end=$n - 1;
$flag=1;

while ($flag && $end > $start) {
  $str = $strings[$end];
  if ($str =~ /^\s*$/) {
  
  } else {
     $flag=0;
  }
  $end--;
}
$end++;


for ($i = $start; $i <= $end; $i++) {
  $str=$strings[$i];
  if ($flag) {
     if ($str =~ /^\s*$/) {
     } else {
       $str =~ s/^[ ]+//;
       $str =~ s/[ ]+$//;
       $str =~ s/[ ]+/ /g;
       
       print $str;
     }
  } else {
    $str =~ s/^[ ]+//;
    $str =~ s/[ ]+$//;
    $str =~ s/[ ]+/ /g;
       
    print $str;
  }
  if ($str =~ /^\s*$/) {
    $flag=1;
  } else {
    $flag=0;
  }
}

#close(in);



