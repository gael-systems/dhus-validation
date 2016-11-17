#! /bin/sh
cd /home/dhus/go-dhus-environment \
&& rm -rf ./dhus \
&& mkdir ./dhus \
&& unzip -o dhus-software-distribution.zip -d ./dhus \
&& rm dhus-software-distribution.zip \
&& sed -ie "s|local_dhus|/home/dhus/go-dhus-environment/dhus/local_dhus|" dhus/etc/dhus.xml \
&& mkdir ./dhus/local_dhus \
&& cp -r /data/ivv/dhus-test-backup/database ./dhus/local_dhus \
&& mkdir -p ./dhus/local_dhus/solr/dhus/data \
&& cp -r /data/ivv/dhus-test-backup/index ./dhus/local_dhus/solr/dhus/data \
&& cp start.sh ./dhus/start.sh \
&& chmod +x ./dhus/start.sh \
&& chmod +x ./dhus/stop.sh \
&& chmod +x ./start-listen.sh