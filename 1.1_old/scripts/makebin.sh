#!/bin/bash
#
# Copyright 2010 Google Inc. All Rights Reserved.
# Author: Bruno Oliveira

# Makes a binary release of the library, that is, a big JAR with all 
# the contents of the JARs
# that a developer needs. The idea is that the developer can link against
# this single big JAR file and use the client library easily, without the
# need for anything else.

# The jars_in_bin file lists the JAR files that we should include in
# the binary release.

trap 'echo \*\*\* Bin creation aborted. See error above; exit 3' ERR

ORIGPWD=$PWD

RELEASE=$1
while [ -z "$RELEASE" ]; do
   echo -n "What's the name of this release? "
   read RELEASE
done

(cd ../java && ant && ant dist)

TMP=/tmp/orkut-os-client-bin-$RELEASE
rm -rvf $TMP
mkdir -p $TMP/BIN
mkdir -p $TMP/ORIG_JARS

# Copy the client library JAR to the bin
cp -v ../java/dist/lib/*.jar $TMP/ORIG_JARS

# Copy all the other necessary JARs to the bin
for j in `cat jars_in_bin | grep -v '^#'`; do
   cp -v ../$j $TMP/ORIG_JARS
done

cd $TMP/BIN

# Pour each jar into bin
for j in ../ORIG_JARS/*.jar; do
   unzip $j

   # remove dirt (loose files like README, build.xml, etc)
   for i in * .[^.]*; do
      [ -f $i ] && rm -f $i
   done
   [ -d META-INF ] && rm -rf META-INF
done

# back to $TMP
cd ..

# Copy our readme file here
cp -v $ORIGPWD/README_bin README

echo >>README
echo >>README
echo "=============================================================" >>README
echo "Binary package info:" >>README
echo "   Release name: $RELEASE" >>README
echo "   Package date : `date`" >>README

# Pack the bin
(cd BIN && jar cf ../orkut-os-client-bin-$RELEASE.jar *)

# Remove the BIN directory
rm -rf BIN

# Add the sample(s)
cp -a $ORIGPWD/../sample-new .
find sample-new -name '.*' -exec rm -f {} \;  #delete source control files,etc

cd /tmp
zip -r $ORIGPWD/orkut-os-client-bin-$RELEASE.zip orkut-os-client-bin-$RELEASE

cd $ORIGPWD
echo "Binary package ready:"
ls -l *.zip


