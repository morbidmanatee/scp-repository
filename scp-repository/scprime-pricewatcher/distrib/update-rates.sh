#
rm rates.sh
touch rates.sh
# properties file is optional argument
java -jar pricewatcher.jar ${1} >> rates.sh
chmod a+x rates.sh
sh rates.sh
