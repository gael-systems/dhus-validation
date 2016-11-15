#! /bin/sh
cd /home/dhus/go-dhus-environment/dhus \
&& ./start.sh \
& java -jar /home/dhus/go-dhus-environment/dhus-listener.jar 180 /home/dhus/go-dhus-environment/dhus/dhus.log