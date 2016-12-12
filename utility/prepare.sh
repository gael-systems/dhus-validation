#! /bin/sh
# clean dhus environment
cd /home/dhus/go-dhus-environment \
&& rm -rf ./dhus \
&& mkdir ./dhus \
# extract dhus distribution
&& unzip -o dhus-software-distribution.zip -d ./dhus \
&& rm dhus-software-distribution.zip \
# fetch database
&& mkdir ./dhus/local_dhus \
&& cp -r /data/ivv/dhus-test-backup/database ./dhus/local_dhus \
# fetch solr index
&& mkdir -p ./dhus/local_dhus/solr/dhus/data \
&& cp -r /data/ivv/dhus-test-backup/index ./dhus/local_dhus/solr/dhus/data \
# fetch hfs data
&& cp -r /data/ivv/dhus-test-backup/incoming ./dhus/local_dhus \
# get custom start.sh and add execution right
&& cp start.sh ./dhus/start.sh \
&& chmod +x ./dhus/start.sh \
&& chmod +x ./dhus/stop.sh \
&& chmod +x ./start-listen.sh \
# get custom dhus.xml
&& cp dhus.xml ./dhus/etc/dhus.xml