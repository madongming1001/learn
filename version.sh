#!/usr/bin/env bash
#1、修改版本
./mvnw versions:set -DnewVersion= -DprocessAllModules=true -DallowSnapshots=true
#2、回滚版本
#./mvnw versions:revert
#3、提交版本变更
./mvnw versions:commit