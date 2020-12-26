cd LearnGeneric

javac erasure/ErasureDemo.java

javac reflection/MyLogger.java
javac reflection/StringList.java
javac -cp ".:../Common/common.jar" reflection/Injector.java
javac -cp ".:../Common/common.jar" reflection/Main.java

javac -cp ".:../Common/common.jar" advancedTopics/Main.java

echo "Build Learn Generic Completed!"

cd ..