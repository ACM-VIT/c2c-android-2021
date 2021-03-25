#!/bin/sh

if [ -n "$1" ]; then
    SECRETS_DECRYPT_PASSPHRASE=$1
fi

script_path=$(dirname "$0")

gpg --quiet --batch --yes --decrypt --passphrase="$SECRETS_DECRYPT_PASSPHRASE" \
--output "${script_path}/../../app/google-services.json" "${script_path}/google-services.json.gpg"

gpg --quiet --batch --yes --decrypt --passphrase="$SECRETS_DECRYPT_PASSPHRASE" \
--output "${script_path}/../../app/firebase-service-credentials.json" "${script_path}/firebase-service-credentials.json.gpg"