#!/bin/bash
CURRENT=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
TARGET_JS='/js/tizen.js'

#if it is already exists, remove it first (maybe out of sync)
if [ -f "$CURRENT""$TARGET_JS" ] ; then
    rm "$CURRENT""$TARGET_JS"
fi

# make an empty js
touch "$CURRENT""$TARGET_JS"