#!/bin/bash

TIME="$[30 * 60]"
nohup java -cp src alex.voltmeter.Voltmeter /dev/ttyACM0 9999999 $TIME >>/home/alex/projects/voltmeter/output.log &

sleep 3

tail -f /home/alex/projects/voltmeter/output.log
