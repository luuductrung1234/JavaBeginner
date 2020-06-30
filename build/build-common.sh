cd Common

javac util/logging/CustomLogger.java
javac util/logging/LogFormatter.java

javac util/exception/ExceptionHelper.java

jar cvf common.jar *

echo "Build Common Library Completed!"

echo ""

cd ..