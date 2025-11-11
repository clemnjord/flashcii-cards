#!/bin/bash

# Array to store absolute paths
absolute_paths=()

# Convert each argument to absolute path
for path in "$@"; do
    # Get absolute path
    abs_path=$(realpath "$path" 2>/dev/null || readlink -f "$path" 2>/dev/null || cd "$(dirname "$path")" && echo "$(pwd)/$(basename "$path")")
    absolute_paths+=("$abs_path")
done

# Join with commas
IFS=','
echo "${absolute_paths[*]}"