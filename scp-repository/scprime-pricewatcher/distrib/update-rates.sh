#
rm rates.sh
touch rates.sh
java -jar pricewatcher.jar >> rates.sh
chmod a+x rates.sh
sh rates.sh
