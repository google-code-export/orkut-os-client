#!/bin/bash
#
# Copyright 2009 Google Inc. All Rights Reserved.
# Author: Shishir Birmiwal

HG=hg
REPO=https://orkut-os-client.googlecode.com/hg/
TIMESTAMP=`date "+%s"`
TMP_TARGET=/tmp/orkut-os-client/release/$TIMESTAMP
REV=$1
RELEASE_NAME=$2

if [ "x$RELEASE_NAME" == "x" ]; then
    echo Usage: $0 "<hg-revision-number> <release-name/id>"
    exit;
fi

echo Building release in $TMP_TARGET

rm -rf $TMP_TARGET
mkdir -p $TMP_TARGET

# pull from release
$HG clone $REPO $TMP_TARGET/orkut-os-client
cd $TMP_TARGET/orkut-os-client

# build archive
$HG archive -X "scripts/" --rev $REV --type "tbz2" "../orkut-os-client-full-$RELEASE_NAME-$REV.tar.bz2"

# go back to the current directory
cd -
mv "$TMP_TARGET/orkut-os-client-full-$RELEASE_NAME-$REV.tar.bz2" .

echo cleaning up
rm -rf $TMP_TARGET

echo Release package prepared as orkut-os-client-full-$RELEASE_NAME-$REV.tar.bz2

