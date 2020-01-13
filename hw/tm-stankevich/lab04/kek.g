

#token{string} PLUS -> "\+"
#token{regex, string} NUM -> "[1-9][0-9]*"
#token{skip, string, regex} BLANKS -> "\n|\r|\t| "
#token{regex} WORD -> "[a-z]+"

##rules

#rule input ->
	expr
		#action{3}
			cout << $1;
		#action


#rule expr -> expr PLUS NUM
	#action{3}
		$$ = $1 + $3;
	#action

#rule expr -> NUM
	#action{3}
		$$ = $1;
	#action

