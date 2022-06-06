#!/usr/bin/env bash
#pom版本增加脚本
#1、修改版本
./mvnw versions:set -DnewVersion= -DprocessAllModules=true -DallowSnapshots=true -Dmaven.test.skip=true
#2、回滚版本
#./mvnw versions:revert
#3、提交版本变更
./mvnw versions:commit