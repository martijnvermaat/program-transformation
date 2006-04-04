JAVAFRONT    = $(HOME)/.nix-profile/
JAVAFRONTSDF = $(JAVAFRONT)/share/sdf/java-front
SDFINCLUDES  = -I $(JAVAFRONTSDF) -Idef $(JAVAFRONTSDF)/Java-15-Prefixed.def

all : Java-15-in-Java-15.tbl Java-15-in-Java-15.str

Java-15-in-Java-15.def : Java-15-in-Java-15.sdf
	pack-sdf -i $< -o $@ $(SDFINCLUDES)

Java-15-in-Java-15.tbl : Java-15-in-Java-15.def
	sdf2table -i $< -o $@ -m Java-15-in-Java-15

Java-15-in-Java-15.rtg : Java-15-in-Java-15.def
	sdf2rtg -i $< -o $@ --main Java-15-in-Java-15

Java-15-in-Java-15.str : Java-15-in-Java-15.rtg
	rtg2sig -i $< -o $@
