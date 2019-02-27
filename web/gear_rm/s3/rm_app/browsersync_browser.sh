#!/bin/bash
CURRENT=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
PARENT=$( cd "$( dirname "$CURRENT" )" && pwd )
TIZEN=$( cd "$( dirname "$PARENT" )" && pwd )
ORIGINAL_JS='/TizenSystem/tizen.js'
TARGET_JS='/js/tizen.js'

#if it is already exists, remove it first (maybe out of sync)
if [ -f "$CURRENT""$TARGET_JS" ] ; then
    rm "$CURRENT""$TARGET_JS"
fi
ln "$TIZEN""$ORIGINAL_JS" "$CURRENT""$TARGET_JS"

browser-sync start -s $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )