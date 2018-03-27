import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;

import spark.utils.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TemperatureServerTest {

    final static String EXPECTED_JSON_NO_DEVICES = "{}";
    final static String EXPECTED_JSON_SINGLE_DEVICE = "{\"current_temperature_1\": 22.3}";
    final static String EXPECTED_JSON_MULTIPLE_DEVICES = "{\"current_temperature_1\": 22.3, \"current_temperature_2\": 20.0}";

    @Test
    public void shouldReturnFormattedJson_whenNoDevicePresent() {
        final OneWireDeviceManager manager = mock(OneWireDeviceManager.class);
        when(manager.getDevices(TmpDS18B20DeviceType.FAMILY_CODE)).thenReturn(Collections.emptyList());

        final TemperatureServer server = new TemperatureServer(0, manager);

        final String temperaturesFromServer = server.getCurrentTemperatureAsJson("localhost");
        final boolean jsonIsValid = temperaturesFromServer.equals(EXPECTED_JSON_NO_DEVICES);

        Assert.isTrue(jsonIsValid, String.format("Expected json failed validation, found '%s'", temperaturesFromServer));
    }

    @Test
    public void shouldReturnFormattedJson_whenSingleDevicePresent() {
        final OneWireDeviceManager manager = mock(OneWireDeviceManager.class);
        final TemperatureSensor sensor1 = mock(TemperatureSensor.class);

        when(manager.getDevices(TmpDS18B20DeviceType.FAMILY_CODE)).thenReturn(Arrays.asList(sensor1));
        when(sensor1.getTemperature()).thenReturn(22.3d);

        final TemperatureServer server = new TemperatureServer(0, manager);

        final String temperaturesFromServer = server.getCurrentTemperatureAsJson("localhost");
        final boolean jsonIsValid = temperaturesFromServer.equals(EXPECTED_JSON_SINGLE_DEVICE);

        Assert.isTrue(jsonIsValid, String.format("Expected json failed validation, found '%s'", temperaturesFromServer));
    }

    @Test
    public void shouldReturnFormattedJson_whenMultipleDevicesPresent() {
        final OneWireDeviceManager manager = mock(OneWireDeviceManager.class);

        final TemperatureSensor sensor1 = mock(TemperatureSensor.class);
        final TemperatureSensor sensor2 = mock(TemperatureSensor.class);

        when(manager.getDevices(TmpDS18B20DeviceType.FAMILY_CODE)).thenReturn(Arrays.asList(sensor1, sensor2));

        when(sensor1.getTemperature()).thenReturn(22.3d);
        when(sensor2.getTemperature()).thenReturn(20.0d);

        TemperatureServer server = new TemperatureServer(0, manager);

        final String temperaturesFromServer = server.getCurrentTemperatureAsJson("localhost");
        final boolean jsonIsValid = temperaturesFromServer.equals(EXPECTED_JSON_MULTIPLE_DEVICES);

        Assert.isTrue(jsonIsValid, String.format("Expected json failed validation, found '%s'", temperaturesFromServer));
    }
}