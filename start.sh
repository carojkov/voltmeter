#!/bin/bash

echo `pwd`

JAVA_HOME=/opt/jdk1.7.0_65

find . -name '*.class' | xargs -I {0} rm {0}

$JAVA_HOME/bin/javac src/carojkov/voltmeter/Voltmeter.java

TIME="$[1 * 60]"

nohup $JAVA_HOME/bin/java -cp src carojkov.voltmeter.Voltmeter /dev/ttyACM0 9999999 $TIME >>/home/carojkov/projects/voltmeter/output.log &

sleep 3

tail -f /home/carojkov/projects/voltmeter/output.log
