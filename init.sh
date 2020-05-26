#!/bin/bash

learnName=${1}

echo "************************************************"
echo "*                INIT PROJECT                  *"
echo "************************************************"
echo ""
echo "Generate source code directory & files . . ."
mkdir "$learnName"
mkdir "$learnName/com"
mkdir "$learnName/com/demo1"
touch "$learnName/com/demo1/Main.java"

echo "Generate source code content . . ."
echo "package com.demo1;" >> "$learnName/com/demo1/Main.java"
echo "import java.util.logging.Level;" >> "$learnName/com/demo1/Main.java"
echo "import java.util.logging.Logger;" >> "$learnName/com/demo1/Main.java"
echo "public class Main {" >> "$learnName/com/demo1/Main.java"
echo "private static Logger logger = Logger.getLogger(Main.class.getSimpleName());" >> "$learnName/com/demo1/Main.java"
echo "public static void main(String[] args) {" >> "$learnName/com/demo1/Main.java"
echo "logger.log(Level.INFO, \"Hello World\");" >> "$learnName/com/demo1/Main.java"
echo "}" >> "$learnName/com/demo1/Main.java"
echo "}" >> "$learnName/com/demo1/Main.java"

echo "Generate build script files . . ."
fileName="build-$(tr [A-Z] [a-z] <<< "$learnName").sh"
mkdir "build"
touch "./build/$fileName"

echo "Generate build script content . . ."
echo "" >> "./build/$fileName"
echo "cd $learnName" >> "./build/$fileName"
echo "" >> "./build/$fileName"
echo "javac com/demo1/Main.java" >> "./build/$fileName"
echo "" >> "./build/$fileName"
echo "echo \"Build Completed!\"" >> "./build/$fileName"
echo "" >> "./build/$fileName"
echo "cd .." >> "./build/$fileName"

echo "Add new build script to 'build.sh' . . ."
echo "" >> "build.sh"
echo "sh ./build/$fileName" >> "build.sh"