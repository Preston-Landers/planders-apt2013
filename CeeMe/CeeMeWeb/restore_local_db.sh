#!/usr/bin/env bash
# Restore our copy of the dev server's database

LOCAL_DB_DIR=target/CeeMeWeb-1.0-SNAPSHOT/WEB-INF/appengine-generated
LOCAL_DB=${LOCAL_DB_DIR}/local_db.bin
BACKUP_DB=backup_local_db.bin

mkdir -p ${LOCAL_DB_DIR}
cp $BACKUP_DB $LOCAL_DB 
