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

Because `MainWithPackage.java` (main class) has specified which package it belong to (`out.learning`). So that `MainWithPackage.java` application can be start with ClassPath `LearnClassPath` and full-qualified name is `com.learning.MainWithPackage`.

Assume Java CLI run at root directory, **JavaBeginner** :

```
$ java -cp LearnClassPath com.learning.MainWithPackage
```

## Start application with main class MainWithoutPackage.java

Because `MainWithoutPackage.java` (main class) hasn't specified which package it belong to. Java Runtime will use default package (empty value) for `MainWithoutPackage.java`.

`MainWithoutPackage.java` use `Subject.java`, they have different ClassPath.

-   `Subject.java` is in `org.researching` package, it can be explore in ClassPath `LearningClassPath`.

-   `MainWithoutPackage.java` is in default package (empty value), it can be explore in ClassPath `LearningClassPath/com/learning`.

So that `MainWithoutPackage.java` application can be start with ClassPath `LearnClassPath/com/learning` and `LearnClassPath` and full-qualified name is `MainWithoutPackage`.

Assume Java CLI run at root directory, **JavaBeginner** :

```
$ java -cp LearnClassPath:LearnClassPath/com/learning MainWithoutPackage
```
