#!/bin/sh
# shellcheck disable=SC2039
# shellcheck disable=SC1090
dirname="$(dirname "$0")"
chmod +x "${dirname}/exports.sh"
. "${dirname}/exports.sh"

check_is_last_exec_time() {
  local current_time diff_unix last_exec

  current_time=$(get_current_time)
  diff_unix=$((HOUR_IN_UNIX * $(get_value_at_key "$DIFF")))
  last_exec=$(get_value_at_key "$LAST_EXEC" | grep -oP '"\K[^"\047]+(?=["\047])')

  echo $(((current_time - last_exec) > diff_unix))
}

enable_work_run() {
  set_value_at_key "$HAS_CHANGES" 0 >/dev/null
  set_value_at_key "$LAST_EXEC" "$(get_current_time)" >/dev/null
  echo "::set-output name=should_run::true"
}

extract_release_notes() {
  local text groups deferrable notes noexec current_groups persist_groups

  text="$(echo "${TITLE} \n ${BODY}" | tr -s " ")"
  if [ -z "$text" ]; then
    echo 0
    return 0
  fi

  noexec="$(format_command "$text" command NOEXEC)"
  if [ "$noexec" = true ]; then
    echo 0
    return 0
  fi

  notes=$(get_value_at_key "$RELEASE_NOTES")
  if [ -n "$notes" ]; then
    notes="${notes} \n \n $(format_command "$text" text)"
  else
    notes="$(format_command "$text" text)"
  fi
  set_value_at_key "$RELEASE_NOTES" "$notes" >/dev/null

  current_groups=$(get_value_at_key "$DIST_GROUPS")
  groups="$(format_command "$text" command GROUPS)"
  if [ -n "$groups" ]; then
    set_value_at_key "$DIST_GROUPS" "$groups" >/dev/null

    persist_groups="$(format_command "$text" command PERSIST_GROUPS)"
    if [ "$persist_groups" = true ]; then
      set_value_at_key "$PERSISTED_GROUPS" "$groups"
    else
      set_value_at_key "$PERSISTED_GROUPS" "$current_groups"
    fi

  fi

  deferrable="$(format_command "$text" command DEFERRABLE)"
  if [ "$deferrable" = false ]; then
    set_value_at_key "$LAST_EXEC" 0 >/dev/null
  fi

  echo 1
}

if ([ "$GITHUB_EVENT_NAME" = "schedule" ] && [ "$(get_value_at_key "$HAS_CHANGES")" = 1 ]) ||
  [ "$GITHUB_EVENT_NAME" = "workflow_dispatch" ]; then
  enable_work_run

elif [ "$GITHUB_EVENT_NAME" = "pull_request" ] && [ "$PR_MERGED" = true ]; then
  should_continue=$(extract_release_notes)
  if [ "$should_continue" = 1 ]; then

    if [ "$(check_is_last_exec_time)" = 0 ]; then
      set_value_at_key "$HAS_CHANGES" 1 >/dev/null
    else
      enable_work_run
    fi

  fi
fi
