#!/bin/bash

export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:/lib/*:./*
#export CLASSPATH=$CLASSPATH:cm.jar
#export CLASSPATH=$CLASSPATH:jedis-2.6.1.jar
#export CLASSPATH=$CLASSPATH:spring-context-4.1.6.RELEASE.jar
#export CLASSPATH=$CLASSPATH:spring-core-4.1.6.RELEASE.jar
#export CLASSPATH=$CLASSPATH:spring-beans-4.1.6.RELEASE.jar
#export CLASSPATH=$CLASSPATH:spring-expression-4.1.6.RELEASE.jar
#export CLASSPATH=$CLASSPATH:log4j-1.2.17.jar
#export CLASSPATH=$CLASSPATH:commons-logging-1.2.jar
#export CLASSPATH=$CLASSPATH:spring-aop-4.1.6.RELEASE.jar
#export CLASSPATH=$CLASSPATH:commons-pool2-2.3.jar
#export CLASSPATH=$CLASSPATH:commons-lang3-3.4.jar


export JAVA_OPTS="-Xms3000M -Xmx5000M -server -Xss1M -Dfile.encoding=UTF-8 -XX:ThreadStackSize=1M -XX:PermSize=256M -XX:MaxPermSize=1024M"
java $JAVA_OPTS -cp $CLASSPATH com.common.redis.MigrationTool
