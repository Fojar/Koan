#!/bin/bash
./gradlew compileJava
nohup ./gradlew run &
./gradlew -t compileJava
