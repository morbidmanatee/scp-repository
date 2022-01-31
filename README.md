# scp-repository

The distrib directory contains all you need to run the application. 


1) Move the files in distrib to your scp directory (so that the ./scp ... command will execute)
2) Edit the pricewatch.properties file with your api key on livecoinwatch.com (you must create a login). Go to https://www.livecoinwatch.com/tools/api
3) Test execute the app by running '$ sh ./update-rates.sh' manually. If it works you should see something like this as output:

              garrett@pisces:/SCPrimeCurrentVersion$ sh ./update-rates.sh
              Host settings updated.
              could not get host score estimate: [failed to get reader response; GET request error; cannot call /host/estimatescore without the renter module]
              Host settings updated.
              could not get host score estimate: [failed to get reader response; GET request error; cannot call /host/estimatescore without the renter module]
              Host settings updated.
              could not get host score estimate: [failed to get reader response; GET request error; cannot call /host/estimatescore without the renter module]
              Host settings updated.
              could not get host score estimate: [failed to get reader response; GET request error; cannot call /host/estimatescore without the renter module]
              Host settings updated.
              could not get host score estimate: [failed to get reader response; GET request error; cannot call /host/estimatescore without the renter module]
              garrett@pisces:/SCPrimeCurrentVersion$

4) Read crontab-examples.txt to see how to configure a crontab. Here is an example:

              $ crontab -e

              # update scp price targets every 5 minutes
              */5 * * * * cd /SCPrimeCurrentVersion && ./update-rates.sh >/dev/null 2>&1
5) ...
6) Profit!


The code was developed using 
            Eclipse IDE for Enterprise Java Developers (includes Incubating components)
            Version: 2020-12 (4.18.0)
            Build id: 20201210-1552
            OS: Windows 10, v.10.0, x86_64 / win32
            Java version: 15.0.2

It requires JavaSE-1.8 or better
It is a Maven project


DISCLAIMER -

THIS IS FREE OPEN SOURCE CODE. IT IS FREE SHIT AND BE HAPPY YOU GET TO USE IT FOR FREE. IF YOU GOT COMPLAINTS KEEP IT TO YOURSELF,
OR WRITE YOUR OWN DAMNED CODE.


