# Makefile for compiling java-bytecode-assimilation.str

# declare path for pkg-config
export PKG_CONFIG_PATH := $(HOME)/.nix-profile/lib/pkgconfig:$(PKG_CONFIG_PATH)

JAVAFRONT = $(HOME)/.nix-profile
DRYAD = $(HOME)/.nix-profile

XTCFLAGS = `pkg-config --variable=strcxtcflags dryad`
STRCFLAGS = `pkg-config --variable=strcflags dryad`

# assume that syntax definition is in ../embedding

STRINCLUDES = \
   -I $(JAVAFRONT)/share/sdf/java-front/ \
   -I $(JAVAFRONT)/share/java-front/ \
   -I $(DRYAD)/share/dryad

STRRUNLIBS = -la stratego-lib 

all: test

test : test.str Makefile
	strc -i $< $(STRINCLUDES) $(STRCFLAGS) $(STRRUNLIBS) $(XTCFLAGS)
