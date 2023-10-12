#!/bin/bash

# Remove the existing dist.tar.gz file if it exists
rm -f dist.tar.gz

# Change to the dist/deu-viewer/ directory
cd dist/

# Create a new dist.tar.gz file with the contents of the current directory
tar -czvf dist.tar.gz .

# Move the dist.tar.gz file back to the parent directory
mv dist.tar.gz ../

# Change back to the original directory
cd ../

