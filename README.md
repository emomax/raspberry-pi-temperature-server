# Simple temperature rest service for Raspberry Pi's
As a fun home automation project, I wanted to measure how warm it is in my appartment. After getting the components and making sure everything worked locally, I needed a way to get measured room temperature data available over http to let my other services scrape it.

This could be used to scrape via telegraf or prometheus, for example (that's what I have done).

### Pre-requisites
This assumes that you have a DS18B20 temperature sensor with an 4.7kOhm resistor set up properly with a pi, and that the One-Wire protocol is available. I use Raspbian Stretch for this, and the settings can be enabled under raspi-config (from the terminal). There are plenty of tutorials for using DS18B20 sensors with Raspberry Pi's.

## How to use it
### maven
To build this project into an executable file, you will need maven which is installable by most package managers (apt-get for linux, brew for MacOSX etc). Standing in the root of the project, the following command will build a single jar containing everything we need:
```mvn clean compile assembly:single```

Try it by going into the target directory, and execute:
```java -Xmx256m -jar raspberry-pi-temperature-server-1.0-SNAPSHOT-jar-with-dependencies.jar 5005```

If you go into **http://localhost:5005/current_temperature** you should see at least an empty json object, like so: "{}". This means that no devices are found, but that's fine if you run this on something different than your pi. At least the server is working as expected.

**NOTE that the port is specified here. If no port is specified, the server will default to port 1337**

### Syncing the files
The easiest way I find to move things onto the pi is from the commandline via scp:
```scp -r target pi@192.168.1.7:~/temperature-server```

### Making it start on boot / run in background
As a conveniance, I have checked in the systemd manifest I use to make sure the server starts on boot. Modify it to suit your needs - but it should reside in:
**/etc/systemd/system/temperature_server.service** 

To start it, you type: 
```sudo systemctl daemon-reload``` followed by
```sudo systemctl start temperature_server.service```

You should then be able to get the temperature data through the browser, via curl or just shooting an http GET request in your favourite language towards the ip of the raspberry pi, like so:
```http://<ip_of_pi>:1337/current_temperature```

