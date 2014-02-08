#!/bin/sh

fuser -k -u -n file ../log/dfhm.cc.log

sleep 2

>../log/dfhm.cc.log

/usr/bin/java -Dfile.encoding=UTF-8 -cp log4j-1.2.11.jar:mongo-java-driver-2.11.3.jar:gson-2.2.4.jar:dom4j-1.6.1.jar:. com.cambrian.Start dfhm.cc.start.cfg >../log/dfhm.cc.log 2>>../log/dfhm.cc.log &

tail ../log/dfhm.cc.log -f
