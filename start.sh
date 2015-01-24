#!/bin/bash

echo `pwd`

JAVA_HOME=/opt/jdk1.7.0_65

find . -name '*.class' | xargs -I {0} rm {0}

$JAVA_HOME/bin/javac src/carojkov/voltmeter/*.java

args="-in /dev/ttyACM0 -start now -cycle 60 -check 5 -cycles 3 $1 $2"

logging='-Djava.util.logging.config.file=logging.properties'

nohup $JAVA_HOME/bin/java $logging -cp src carojkov.voltmeter.Main $args >>/home/alex/projects/voltmeter/output.log &

sleep 3

tail -f /home/alex/projects/voltmeter/output.log
