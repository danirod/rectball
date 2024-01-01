#!/bin/sh
#
# This script exists as an attempt to normalize all the copyright
# comments on top of the .kt and .java files. Running this file
# should update every .kt and .java file tracked by Git and make
# sure that the comment follows the template, with the copyright
# date first set to 2015, and the copyright date last set to the
# current year.

cd "$(dirname $0)/.."

function file_list() {
    git ls-tree --full-tree -r --name-only HEAD | grep '.\(kt\|java\)$'
}

header=$(cat scripts/header_template.txt)
header=$(echo "$header" | sed "s/\$YEAR/$(date +%Y)/")

for file in `file_list`; do
    line_pkg=$(grep -n 'package' $file | head -n1 | cut -d: -f1)
    
    echo "Updating $file..."
    echo "$header" > $file.tmp
    tail -n+$line_pkg $file >> $file.tmp
    mv $file.tmp $file
done
