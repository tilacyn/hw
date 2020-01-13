#pragma once


#include "grammar.h"
#include "dfa.h"


class DFAGenerator {
	vector<Rule> rules;
	DFAGenerator(vector<Rule>rules) : rules(rules) {}


	DFA generateNDFA() {
		DFA dfa;
		// state with E_0 -> . input
		Rule additional;
		additional.left = NT("zubr");
		additional.right.push_back(NT("input"));
		State start;
		start.s.push_back(Situation(additional));



	}

	bool closure(State& state) {
		for (Situation sit : state.s) {
			state
		}
	}


};
