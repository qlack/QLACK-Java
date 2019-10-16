#!/usr/bin/env bash
echo "drop database if exists qlack_liquibase; create database qlack_liquibase" | mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASS