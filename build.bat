@echo off
echo "Building..."
.\gradlew build
echo "Moving"
copy .\build\libs\* ..\..\server\plugins