DRYAD=$(HOME)/.nix-profile/
JARS=../gnu-bytecode/kawa-1.8.jar
javainjava = ../embedding/Java-15-in-Java-15.tbl

# parse Java file with parse table for embedding
%.ajava : %.java
   sglri -p $(javainjava) -i $< | pp-aterm -o $@

# assimilate embedded Java code to bytecode API calls
%.assim.ajava : %.ajava ../src/java-bytecode-assimilation
   ../src/java-bytecode-assimilation -i $< | pp-aterm -o $@

# flatten uses of expression blocks
%.eflat.ajava : %.assim.ajava ../src/java-bytecode-assimilation
   $(DRYAD)/libexec/core-lift-eblocks -i $< | pp-aterm -o $@

# pretty-print 
%.txt : %.ajava
   pp-java -i $< -o $@

# rename to proper Java file
# convention FooGen.java uses concrete syntax, but defines
# a class names FooGenerated.java
%erated.java : %.eflat.txt
   cp $< $@

clean:
   rm -f *.ajava

# compile Java code
%.class : %.java
   javac -cp $(JARS) $<

# run the Java code with appropriate command-line arguments
%.run : %.class
   java -cp $(JARS) $* $(RUNARGS_$*)

RUNARGS_RTCG4Generated = 1024
RUNARGS_RTCG4bGenerated = 1024

test : RTCG4Generated.class
