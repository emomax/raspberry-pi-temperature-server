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

import java.util.logging.Logger;

public class EntryPoint {
    private final static Logger logger = Logger.getAnonymousLogger();

    public static void main(final String... arguments) {
        int serverPort;

        if (arguments.length == 0) {
            logger.info("No port specified. Defaulting to port 1337.");
            serverPort = 1337;
        }
        else {
            serverPort = parsePortFromVararg(arguments[0]);
        }

        final OneWireDeviceManager devicemanager = new RaspberryPi_W1DeviceManager();

        new TemperatureServer(serverPort, devicemanager).start();
    }

    private static int parsePortFromVararg(final String desiredPort) {
        try {
            return Integer.parseInt(desiredPort);
        }
        catch (final NumberFormatException exception) {
            throw new RuntimeException("Specified port must be an integer, preferably in range (1024, 65535)", exception);
        }
    }
}
