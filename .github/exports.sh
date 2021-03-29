#!/bin/sh
# shellcheck disable=SC2039
# shellcheck disable=SC2034

HOUR_IN_UNIX=3600
RELEASE_NOTES="release_notes"
HAS_CHANGES="has_changes"
DIST_GROUPS="dist_groups"
LAST_EXEC="last_exec"
DIFF="diff"
PERSISTED_GROUPS="persisted_groups"

set_value_at_key() {
  curl -s -X PATCH -d "$(json "$1" "$2")" "https://${FIREBASE_PROJECT_ID}.firebaseio.com/github.json"
}

get_current_time() {
  local current_time
  current_time=$(curl -s https://api.keyvalue.xyz/timestamp)
  echo "${current_time}"
}

json () {
  jq -n --arg a "$1" --arg b "$2" '{($a): $b}'
}

format_command () {
  if [ "$2" = "command" ]; then
    echo "$1" | grep -Po "\!${3}=\"\K.*?(?=\")"
  elif [ "$2" = "text" ]; then
    echo "$1" | grep -vwE "\!.*"
  fi
}

get_value_at_key() {
  local value
  value=$(curl -s "https://${FIREBASE_PROJECT_ID}.firebaseio.com/github/${1}.json")
  value="${value%\"}"
  value="${value#\"}"
  echo "${value}"
}



