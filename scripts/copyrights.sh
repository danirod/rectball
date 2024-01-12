#!/bin/sh
# This script exists as an attempt to normalize all the copyright comments on
# top of the .kt and .java files. Running this file should update every .kt and
# .java file tracked by Git and make sure that the comment follows the template,
# with the copyright date first set to 2015, and the copyright date last set to
# the current year.

set -e
cd "$(dirname $0)/.."

# Get the files to check, which are every Java and Kotlin file. Some exceptions
# apply (for instance, StartupHelper.kt has its own copyright header, and I
# think I should not touch that).
function file_list() {
  git ls-tree --full-tree -r --name-only HEAD | \
    grep '.\(kt\|java\)$' | \
    grep -v "lwjgl3/src/main/kotlin/es/danirod/rectball/lwjgl3/StartupHelper.kt"
}

# This function will receive as a parameter the path to a file, and it will
# get the first line of code that should be used as a reference when formatting
# the comment header, which is either the line that contains the package, or
# the line that contains a @file annotation.
function find_first_line_of_code() {
  package_line=$(grep -n '^package' $* | head -n1 | cut -d: -f1)
  file_line=$(grep -n '^@file' $* | head -n1 | cut -d: -f1)
  if [[ -z "$package_line" ]] && [[ -z "$file_line" ]]; then
    return 1 # return error to stop processing this file
  elif [[ -n "$package_line" ]] && [[ -z "$file_line" ]]; then
    echo $package_line
  elif [[ -z "$package_line" ]] && [[ -n "$file_line" ]]; then
    echo $file_line
  elif [[ -n "$package_line" ]] && [[ -n "$file_line" ]]; then
    echo "$(($package_line < $file_line ? $package_line : $file_line))"
  fi

  # For any other case, return OK.
  return 0
}

header=$(cat scripts/header_template.txt)
header=$(echo "$header" | sed "s/\$YEAR/$(date +%Y)/")

for file in `file_list`; do
  line_pkg=$(find_first_line_of_code "$file") || continue
  echo "Updating $file..."
  echo "$header" > $file.tmp
  tail -n+$line_pkg $file >> $file.tmp
  mv $file.tmp $file
done
