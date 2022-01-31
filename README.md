# scp-repository

The distrib directory contains all you need to run the application. 


1) Move the files in ditrib to your scp directory (so that the ./scp ... command will execute)
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
7)
