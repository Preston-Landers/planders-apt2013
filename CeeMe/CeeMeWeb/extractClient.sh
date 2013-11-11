#!/usr/bin/env bash

LOCAL_DB=target/CeeMeWeb-1.0-SNAPSHOT/WEB-INF/appengine-generated/local_db.bin
BACKUP_DB=backup_local_db.bin

CLIENTTMP=/tmp/ceeme-android-client

# Backup the dev server's database
cp $LOCAL_DB $BACKUP_DB

for LIBNAME in register sync
do
	VER=v1
	LIBZIP=${LIBNAME}-${VER}-java.zip
	SRCJAR=${CLIENTTMP}/${LIBNAME}/*jar
	ANDROIDSRC=../CeeMeAndroid/src/main/java

	rm -rf ${CLIENTTMP}
	mkdir -p ${CLIENTTMP}

# -o == overwrite w/o asking
# -d == output dir
	unzip -o -d ${CLIENTTMP} ${LIBZIP}
	unzip -o -d ${ANDROIDSRC} ${SRCJAR}

done

