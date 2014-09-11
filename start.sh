#!/bin/bash

echo `pwd`

JAVA_HOME=/opt/jdk1.7.0_65

find . -name '*.class' | xargs -I {0} rm {0}

$JAVA_HOME/bin/javac src/alex/voltmeter/Voltmeter.java

TIME="$[1 * 60]"

nohup $JAVA_HOME/bin/java -cp src alex.voltmeter.Voltmeter /dev/ttyACM0 9999999 $TIME >>/home/alex/projects/voltmeter/output.log &

sleep 3

tail -f /home/alex/projects/voltmeter/output.log
