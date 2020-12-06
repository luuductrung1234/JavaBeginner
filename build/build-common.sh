cd Common

echo "Common Data..."
javac data/model/Person.java

echo "Common Util..."
javac util/collection/ArrayExtensions.java

javac util/logging/CustomLogger.java
javac util/logging/LogFormatter.java

javac util/exception/ExceptionExtensions.java
javac util/exception/ExceptionHelper.java

jar cvf common.jar *

echo "Build Common Library Completed!"

echo ""

cd ..