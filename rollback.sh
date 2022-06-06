#!/usr/bin/env bash
#还原到之前版本
./mvnw versions:revert
#清除暂存区
git restore algorithm/pom.xml
git restore api/pom.xml
git restore common/pom.xml
git restore discovery/pom.xml
git restore pom.xml