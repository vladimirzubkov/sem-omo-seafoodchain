package cz.cvut.omo.sem.scm.seafood.model.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TemperatureRecord {
    private LocalDateTime timestamp;
    private double temperature;
}