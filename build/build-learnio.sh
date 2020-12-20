cd LearnIO

javac -cp ".:../Common/common.jar" oldInputOutputStream/MyAutoCloseable.java
javac -cp ".:../Common/common.jar" oldInputOutputStream/Main.java

javac -cp ".:../Common/common.jar" newJavaIO/Main.java
javac -cp ".:../Common/common.jar" newJavaIO/ZipHelper.java
javac -cp ".:../Common/common.jar" newJavaIO/FindJavaVisitor.java
javac -cp ".:../Common/common.jar" newJavaIO/FindSymbolicLinkVisitor.java

echo "Build Learn I/O Completed!"

cd ..