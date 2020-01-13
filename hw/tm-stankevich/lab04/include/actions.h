#include <string>
#include <cstdlib>
#include <iostream>

using namespace std;

#define ATR_TYPE string


ATR_TYPE action0(ATR_TYPE& arg1, ATR_TYPE& arg2, ATR_TYPE& arg3) {
	ATR_TYPE zubrResult;
			cout << arg1;
		return zubrResult;
}

ATR_TYPE action1(ATR_TYPE& arg1, ATR_TYPE& arg2, ATR_TYPE& arg3) {
	ATR_TYPE zubrResult;
		zubrResult = arg1 + arg3;
	return zubrResult;
}

ATR_TYPE action2(ATR_TYPE& arg1, ATR_TYPE& arg2, ATR_TYPE& arg3) {
	ATR_TYPE zubrResult;
		zubrResult = arg1;
	return zubrResult;
}