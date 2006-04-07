# Makefile for compiling java-bytecode-assimilation.str

# declare path for pkg-config
export PKG_CONFIG_PATH := $(HOME)/.nix-profile/lib/pkgconfig:$(PKG_CONFIG_PATH)

JAVAFRONT = $(HOME)/.nix-profile
DRYAD = $(HOME)/.nix-profile

XTCFLAGS = `pkg-config --variable=strcxtcflags dryad`
STRCFLAGS = `pkg-config --variable=strcflags dryad`

# assume that syntax definition is in ../embedding

STRINCLUDES = \
   -I ../embedding \
   -I $(JAVAFRONT)/share/sdf/java-front/ \
   -I $(JAVAFRONT)/share/java-front/ \
   -I $(DRYAD)/share/dryad

TESTINCLUDES = \
   -I $(JAVAFRONT)/share/sdf/java-front/ \
   -I $(JAVAFRONT)/share/java-front/ \
   -I $(DRYAD)/share/dryad

STRRUNLIBS = -la stratego-lib 

all: java-bytecode-assimilation test-assimilate-java

java-bytecode-assimilation : java-bytecode-assimilation.str Makefile
	strc -i $< $(STRINCLUDES) $(STRCFLAGS) $(STRRUNLIBS) $(XTCFLAGS)

test-assimilate-java : test-assimilate-java.str Makefile
	strc -i $< $(TESTINCLUDES) $(STRCFLAGS) $(STRRUNLIBS) $(XTCFLAGS)
