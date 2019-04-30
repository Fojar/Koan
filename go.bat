@echo off
call gradlew compileJava
start /min cmd /c gradlew run
call gradlew -t compileJava
