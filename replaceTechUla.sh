#!/bin/bash

# Define the directory to search in
SEARCH_DIR="UserLAnd"
SEARCH_TERM="tech.ula"
REPLACE_TERM="com.hayinfx"

# Find and replace in text files, excluding .git and .gradle directories, and skipping .so files
grep -rl --exclude-dir={.git,.gradle} --exclude=*.so "$SEARCH_TERM" "$SEARCH_DIR" | while read -r file; do
    # Handle text files using sed
    sed -i "s/$SEARCH_TERM/$REPLACE_TERM/g" "$file"
    echo "Replaced in text file: $file"
done

# Rename folders from com/romide/terminal to com/hayinfx/ansolex
find "$SEARCH_DIR" -type d -path "*/com/romide/terminal" | while read -r dir; do
    new_dir=$(echo "$dir" | sed "s|com/romide/terminal|com/hayinfx/ansolex|")
    mkdir -p "$(dirname "$new_dir")"
    mv "$dir" "$new_dir"
    echo "Renamed directory: $dir to $new_dir"
done
