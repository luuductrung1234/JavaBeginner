#!/bin/bash

cd LearnMultithreadingAndConcurrency

javac com/learning/Main.java
javac com/learning/Adder.java
javac com/learning/CallableAdder.java

javac org/researching/BankAccount.java
javac org/researching/Worker.java
javac org/researching/TxWorker.java
javac org/researching/TxPromoWorker.java
javac org/researching/MainConcurrency.java

javac useThreadAndRunnable/Main.java
javac useThreadAndRunnable/MyThread.java

javac useWaitAndNotify/Main.java
javac useWaitAndNotify/TickTock.java
javac useWaitAndNotify/MyThread.java

javac suspendResumeAndStop/Main.java
javac suspendResumeAndStop/MyThread.java

echo "Build Learn Multithreading and Concurrency Completed!"

cd ..