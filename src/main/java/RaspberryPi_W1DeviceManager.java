import java.util.List;
import java.util.stream.Collectors;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;

public class RaspberryPi_W1DeviceManager implements OneWireDeviceManager {

    private W1Master master = new W1Master();

    @Override
    public List<TemperatureSensor> getDevices(final int deviceFamilyId) {
        return master.getDevices(deviceFamilyId).stream()
                .map(TemperatureSensor.class::cast)
                .collect(Collectors.toList());
    }
}
