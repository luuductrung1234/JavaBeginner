cd LearnGeneric

javac erasure/ErasureDemo.java

javac reflection/MyLogger.java
javac reflection/StringList.java
javac reflection/Injector.java
javac reflection/Utils.java
javac -cp ".;../Common/common.jar" reflection/Main.java

echo "Build Learn Generic Completed!"

cd ..