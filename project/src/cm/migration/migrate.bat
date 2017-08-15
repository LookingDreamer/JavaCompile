set CLASSPATH=.;%JAVA_HOME%/lib/dt.jar%JAVA_HOME%/lib/tools.jar
set CLASSPATH=%CLASSPATH%;cm.jar
set CLASSPATH=%CLASSPATH%;jedis-2.6.1.jar
set CLASSPATH=%CLASSPATH%;spring-context-4.1.6.RELEASE.jar
set CLASSPATH=%CLASSPATH%;spring-core-4.1.6.RELEASE.jar
set CLASSPATH=%CLASSPATH%;spring-beans-4.1.6.RELEASE.jar
set CLASSPATH=%CLASSPATH%;spring-expression-4.1.6.RELEASE.jar
set CLASSPATH=%CLASSPATH%;log4j-1.2.17.jar
set CLASSPATH=%CLASSPATH%;commons-logging-1.2.jar
set CLASSPATH=%CLASSPATH%;spring-aop-4.1.6.RELEASE.jar
set CLASSPATH=%CLASSPATH%;commons-pool2-2.3.jar
set CLASSPATH=%CLASSPATH%;commons-lang3-3.4.jar


java -cp %CLASSPATH% com.common.redis.MigrationTool
