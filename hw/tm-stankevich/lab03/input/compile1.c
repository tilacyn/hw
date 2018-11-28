void* f() {
  int* y;
  scanf("%d", y);
  printf("we are going to print %d", fcall(farg));

  y = inc(y);
  return y;
  
}
