#!/usr/bin/env bash
# Restore our copy of the dev server's database

LOCAL_DB=target/CeeMeWeb-1.0-SNAPSHOT/WEB-INF/appengine-generated/local_db.bin
BACKUP_DB=backup_local_db.bin

cp $BACKUP_DB $LOCAL_DB 
