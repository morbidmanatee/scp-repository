# scp-repository

The distrib directory contains all you need to run the application. 


1) Move the files in distrib to your scp directory (so that the ./scp ... command will execute)
2) Edit the pricewatch.properties file with your api key on livecoinwatch.com (you must create a login). Go to https://www.livecoinwatch.com/tools/api
3) Test execute the app by running '$ sh ./update-rates.sh' manually. If it works you should see something like this as output:

              morbidmanatee@pisces:/SCPrimeCurrentVersion$ sh ./update-rates.sh
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
              morbidmanatee@pisces:/SCPrimeCurrentVersion$

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

*************************
DISCLAIMER -

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

THE AUTHOR, AKA MORBIDMANATEE, DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR, AKA MORBIDMANATEE, BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
**************************

**** TRANSLATION!!! IN OTHER WORDS, THIS IS FREE OPEN SOURCE CODE. BE HAPPY THAT I AM PROVIDING IT AS-IS FOR YOU SO THAT YOU GET TO USE IT FOR FREE. IF YOU HAVE COMPLAINTS ABOUT THIS KEEP IT TO YOURSELF, OR WRITE YOUR OWN DAMNED CODE. ****


