#!/usr/bin/env bash
CLIENTTMP=/tmp/android-client
LIBNAME=streamlist
VER=v1
LIBZIP=${LIBNAME}-${VER}-java.zip
SRCJAR=${CLIENTTMP}/${LIBNAME}/*jar
ANDROIDSRC=../ConnexusAndroid/src/main/java

rm -rf ${CLIENTTMP}
mkdir -p ${CLIENTTMP}

# -o == overwrite w/o asking
# -d == output dir
unzip -o -d ${CLIENTTMP} ${LIBZIP}
unzip -o -d ${ANDROIDSRC} ${SRCJAR}
