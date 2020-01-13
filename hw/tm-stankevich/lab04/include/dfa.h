#include "grammar.h"
#include <utility>

class Situation {
	Rule rule;
	int point;
	Situation(rule, point) : rule(rule), point(point) {}
	NT next() {
		return rule.right[point];
	}
};

class State {
	vector <Situation> s; 
};

class DFA {
public:
	vector<State> states;
	map <pair<State, Token>, State> edges;
	

	void addEdge(State f, State s, Token go) {
		edges[{f, go}] = s;
	}

	void addState(State state) {
		return states.push_back(state);
	}
	
}