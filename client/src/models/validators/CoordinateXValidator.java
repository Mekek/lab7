package models.validators;

/**
 * Implementation of validator for CoordinateX
 *
 *  @since 2.0
 *  @author mikhail
 */
public class CoordinateXValidator implements Validator<Double> {
    public String getDescr() {
        return "x <= 499";
    }
    @Override
    public boolean validate(Double value) {
        return value <= 499;
    }
}
