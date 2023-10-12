#!/bin/ash

function join_by { local IFS="$1"; shift; echo "$*"; }

echo I am $(whoami)

# Find vue env vars
vars=$(env | grep VUE_APP_ | awk -F = '{print "$"$1}')
vars=$(join_by ',' $vars)
echo "Found variables $vars"
echo "$VUE_APP_ROOT_API"
echo "$VUE_APP_SCORING_START_DATE"
echo "$VUE_APP_SCORING_STEP_SIZE"
echo "$VUE_APP_SCORING_MAX_POINTS"
echo "$VUE_APP_HISTORY_START_DATE"
echo "$VUE_APP_HISTORY_RESOLUTION"
echo "$VUE_APP_SHOW_SPARQL"

for file in /usr/share/nginx/html/static/js/app.*.js;
do
  echo "Processing $file ...";

  # Use the existing JS file as template
  if [ ! -f $file.tmpl.js ]; then
    cp $file $file.tmpl.js
  fi
  envsubst "$vars" < $file.tmpl.js > $file
  rm $file.tmpl.js
done

echo "Starting nginx"
nginx -g 'daemon off;'
