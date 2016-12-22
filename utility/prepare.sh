#! /bin/sh
cd /home/dhus/go-dhus-environment \
&& rm -rf ./dhus \
&& mkdir ./dhus \
&& unzip -o dhus-software-distribution.zip -d ./dhus \
&& sed -ie "s|local_dhus|/home/dhus/go-dhus-environment/dhus/local_dhus|" dhus/etc/dhus.xml \
&& cp -r /data/ivv/dhus-test-backup/local_dhus ./dhus \
&& cp start.sh ./dhus/start.sh \
&& chmod +x ./dhus/start.sh ./dhus/stop.sh ./start-listen.sh