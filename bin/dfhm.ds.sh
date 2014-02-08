#!/bin/sh

fuser -k -u -n file ../log/dfhm.log

sleep 2

>../log/dfhm.log

/home/fun/jdk1.5/jre/bin/java -Xms500M -Xmx1001M -Dfile.encoding=UTF-8 -cp log4j-1.2.11.jar:mongo-java-driver-2.11.3.jar:gson-2.2.4.jar:dom4j-1.6.1.jar:. com.cambrian.Start dfhm.start.cfg >../log/dfhm.log 2>>../log/dfhm.log &


tail ../log/dfhm.log -f
