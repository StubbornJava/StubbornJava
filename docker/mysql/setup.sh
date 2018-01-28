#!/bin/sh

echo 'creating databases'
mysql -u root -e 'CREATE DATABASE IF NOT EXISTS stubbornjava;'

mysql -u root -e 'CREATE DATABASE IF NOT EXISTS sj_access;'
