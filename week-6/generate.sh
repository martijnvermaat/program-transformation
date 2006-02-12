#!/bin/bash

# Generate parse table
pack-sdf -i Logic.sdf \
    | sdf2table -o Logic.tbl -m Logic

# Invoke tests
parse-unit -i Logic.testsuite -p Logic.tbl
