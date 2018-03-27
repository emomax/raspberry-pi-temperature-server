/* Copyright 2018 https://github.com/emomax

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;

import spark.Spark;

/**
 * A lightweight server for raspberry pi to
 * expose a simple web api that displays the
 * current temperatures of sensors of type
 * DS18B20 over One-Wire protocol.
 * <p>
 * It maps <ip_of_pi>:serverPort/current_temperature
 * to display the readings in json format.
 * <p>
 * Example: A raspberry pi with the sensor(s) working
 * with the ip of 192.168.1.200 with no specified
 * port would by default be accessed by entering
 * 'http://192.168.1.200:1337/current_temperature'
 * in a browser.
 */
class TemperatureServer {
    private final static Logger logger = Logger.getAnonymousLogger();

    private final OneWireDeviceManager deviceManager;
    private final int                  serverPort;

    /**
     * Starts a server that exposes readings from a
     * DS18B20 temperature sensor on given port.
     */
    TemperatureServer(final int serverPort, final OneWireDeviceManager oneWireDeviceManager) {
        this.deviceManager = oneWireDeviceManager;
        this.serverPort = serverPort;
    }

    void start() {
        Spark.port(serverPort);
        Spark.get("/current_temperature", (req, res) -> getCurrentTemperatureAsJson(req.ip()));

        logger.info("Server started at port '" + Spark.port() + "'");
    }

    public String getCurrentTemperatureAsJson(String requesterIp) {
        return formatJson(newReading(requesterIp));
    }
    private List<Double> newReading(final String requesterIp) {
        logger.info("New reading from: " + requesterIp);

        final List<TemperatureSensor> w1Devices = deviceManager.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
        logger.info("Found " + w1Devices.size() + " devices.");

        return w1Devices.stream()
                .map(TemperatureSensor::getTemperature)
                .collect(Collectors.toList());
    }

    private String formatJson(final List<Double> values) {
        final String body = IntStream.range(0, values.size())
                .mapToObj(valueIndex -> "\"current_temperature_" + (valueIndex + 1) + "\": " + values.get(valueIndex))
                .collect(Collectors.joining(", "));

        return "{" + body + "}";
    }
}
