#!/bin/bash


# Generate syntax definition
# Generate parse table
# Generate tree grammar
# Generate pretty-print table

pack-sdf -i syntax/PLF.sdf -o PLF.def \
    && sdf2table -i PLF.def -o PLF.tbl -m PLF \
    && sdf2rtg -i PLF.def -m PLF -o PLF.rtg \
    && ppgen -i PLF.def -o PLF.pp

if [ $? != 0 ]; then exit 1; fi


# Generate parenthesizer

sdf2parenthesize -i PLF.def -o PLF-parenthesize.str -m PLF \
    && rtg2sig -i PLF.rtg -o PLF.str \
    && strc -i PLF-parenthesize.str -m io-PLF-parenthesize -la stratego-lib

if [ $? != 0 ]; then exit 1; fi
rm -R PLF-parenthesize.c \
    PLF-parenthesize.dep \
    PLF-parenthesize.o \
    PLF-parenthesize.str \
    PLF.str \
    .libs


# Invoke tests

parse-unit -i tests/PLF.testsuite -p PLF.tbl


# Check parsed formulas against tree grammar
# This always succeeds so we are not interested in ouput

for i in `ls tests/*.plf`; do
    sglri -i $i -p PLF.tbl | format-check --rtg PLF.rtg --vis >/dev/null
done


# Do some checks on the parse-pp-parse pipeline

for i in `ls tests/*.plf`; do

    # Parse and store as base.ast
    sglri -i $i -p PLF.tbl | pp-aterm -o base.ast

    # Pretty print and parse again
    ./PLF-parenthesize -i base.ast \
        | ast2abox -p pretty-print/PLF-pretty.pp \
        | abox2text \
        | sglri -p PLF.tbl | pp-aterm -o base-pretty.ast

    # Pretty print the result
    #./PLF-parenthesize -i base-pretty.ast \
    #    | ast2abox -p pretty-print/PLF-pretty.pp \
    #    | abox2text

    # Show differences after parsing pretty-printed ast
    diff base.ast base-pretty.ast

done

# Clean up
rm base.ast base-pretty.ast 
