cd Common

javac util/logging/CustomLogger.java
javac util/logging/LogFormatter.java

jar cvf common.jar *

echo "Build Common Library Completed!"

echo ""

cd ..