#!/bin/bash

# Generate syntax definition
# Generate parse table
# Invoke tests

pack-sdf -i PLF.sdf -o PLF.def \
  && sdf2table -i PLF.def -o PLF.tbl -m PLF \
  && parse-unit -i PLF.testsuite -p PLF.tbl
