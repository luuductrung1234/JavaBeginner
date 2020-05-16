#!/bin/bash

learnName=${1}

echo "Generate source code"
mkdir "$learnName"
mkdir "$learnName/com"
mkdir "$learnName/com/learning"
touch "$learnName/com/learning/Main.java"
echo "package com.learning;" >> "$learnName/com/learning/Main.java"
echo "import java.util.logging.Level;" >> "$learnName/com/learning/Main.java"
echo "import java.util.logging.Logger;" >> "$learnName/com/learning/Main.java"
echo "public class Main {" >> "$learnName/com/learning/Main.java"
echo "private static Logger logger = Logger.getLogger(Main.class.getSimpleName());" >> "$learnName/com/learning/Main.java"
echo "public static void main(String[] args) {" >> "$learnName/com/learning/Main.java"
echo "logger.log(Level.INFO, "Hello World");" >> "$learnName/com/learning/Main.java"
echo "}" >> "$learnName/com/learning/Main.java"
echo "}" >> "$learnName/com/learning/Main.java"

echo "Generate build script"
fileName="build-$(tr [A-Z] [a-z] <<< "$learnName").sh"
touch "$fileName"

echo "cd $learnName" >> $fileName
echo "javac com/learning/Main.java" >> $fileName
echo "cd .." >> $fileName