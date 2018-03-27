import java.util.List;

import com.pi4j.component.temperature.TemperatureSensor;

public interface OneWireDeviceManager {
    List<TemperatureSensor> getDevices(int deviceFamilyId);
}
