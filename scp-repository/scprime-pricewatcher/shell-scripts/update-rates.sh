#

rm ./rates.sh
touch ./rates.sh
java -jar ./pricewatcher-0.0.1-SNAPSHOT.jar >> ./rates.sh
chmod a+x ./rates.sh
sh ./rates.sh
