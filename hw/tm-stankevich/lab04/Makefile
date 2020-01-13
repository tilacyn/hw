CXX = g++
CXXFLAGS = -O2 -Wall -std=c++14 -Iinclude
LDFLAGS = 

EXE = main
SRCDIR = src
BINDIR = bin

OBJECTS = $(patsubst $(SRCDIR)/%.cpp,$(BINDIR)/%.o,$(wildcard $(SRCDIR)/*.cpp))

all: $(EXE)

$(EXE): $(BINDIR) $(OBJECTS) tmp
	$(CXX) $(OBJECTS) -o $(EXE) $(LDFLAGS)

$(BINDIR)/%.o: $(SRCDIR)/%.cpp
	$(CXX) $(CXXFLAGS) -c -MMD -o $@ $<

include $(wildcard $(BINDIR)/*.d)

$(BINDIR):
	mkdir -p $(BINDIR)

tmp:
	mkdir -p tmp

extract-actions: src/extract-actions/extract-actions.cpp tmp
	$(CXX) $(CXXFLAGS) src/extract-actions/extract-actions.cpp -o extract-actions


clean:
	rm -rf $(BINDIR) $(EXE) tmp
	rm -f include/actions.h

.PHONY: clean all