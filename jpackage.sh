#!/bin/zsh
# 编译class
# javac -d bin src/main/java/module-info.java src/main/java/com/jaiz/desktop/*.java
# 打jar包
# jar --create --file helper-of-zj.jar --main-class com.jaiz.desktop.App -C bin .

# mv src/main/java/module-info.java src/main/java/module-info.txt
mvn clean package
# 打mod
# jmod create --class-path target/helper-of-zj-1.0.0-SNAPSHOT-jar-with-dependencies.jar helper-of-zj.jmod
# jpackage -n helper-of-zj -t pkg --module-path target/helper-of-zj-1.0.0-SNAPSHOT-jar-with-dependencies.jar -m helper.of.zj/com.jaiz.desktop.App
jpackage -n helper-of-zj --input target \
                --main-class com.jaiz.desktop.App --main-jar helper-of-zj-1.0.0-SNAPSHOT-jar-with-dependencies.jar

# mv src/main/java/module-info.txt src/main/java/module-info.java
