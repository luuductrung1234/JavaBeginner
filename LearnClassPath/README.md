# Learn Java ClassPath

Java Runtime can explore all java classes in directory with the same name of its package.

From **LearnClassPath** directory, Java Runtime can explore all java classes (java class is in `.class` file) in following directories (which is sub-directory of **LearnClassPath**):

-   `com/` : java class has specified
    ```
    package com;
    ```
-   `com/learning/` : java class has specified
    ```
    package com.learning;
    ```
-   `com/teaching/` : java class has specified
    ```
    package com.teaching;
    ```
-   `org/` : java class has specified
    ```
    package org;
    ```
-   `org/researching/` : java class has specified
    ```
    package org.researching;
    ```

## Start application with main class MainWithPackage.java

Because `MainWithPackage.java` (main class) has specified which package it belong to (`out.learning`). So that `MainWithPackage.java` application can be start at ClassPath `LearnClassPath` with full-qualified name is `com.learning.MainWithPackage`.

Assume Java CLI run at root directory, **JavaBeginner** :

```
$ java -cp LearnClassPath com.learning.MainWithPackage
```

## Start application with main class MainWithoutPackage.java

Because `MainWithoutPackage.java` (main class) hasn't specified which package it belong to. Java Runtime will use default package (empty value) for `MainWithoutPackage.java`. So that `MainWithoutPackage.java` application can be start at ClassPath `LearnClassPath/com/learning` with full-qualified name is `MainWithoutPackage`.

Assume Java CLI run at root directory, **JavaBeginner** :

```
$ java -cp LearnClassPath/com/learning MainWithoutPackage
```
