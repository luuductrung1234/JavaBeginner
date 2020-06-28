cd LearnAsynchronous

javac com/completionStage/Main.java

javac com/chainMultipleTasks/User.java
javac -cp ".:../Common/common.jar" com/chainMultipleTasks/Main.java

javac com/exceptionInCompletionStagePipeline/Utils.java
javac com/exceptionInCompletionStagePipeline/User.java
javac -cp ".:../Common/common.jar" com/exceptionInCompletionStagePipeline/Main.java

echo "Build Learn Asynchronous Programming Completed!"

cd ..