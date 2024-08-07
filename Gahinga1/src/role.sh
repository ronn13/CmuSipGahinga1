#!/bin/bash
# Check for role of given user
searchresult=$(awk -F, '$1 == "'$1'" && $4 == "admin"' user_sample.csv)
if [ -n "$searchresult" ]; then
    echo "admin"
else
    echo ""
fi
