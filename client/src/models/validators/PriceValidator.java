package models.validators;

/**
 * Implementation of validator for Area
 *
 *  @since 2.0
 *  @author mikhail
 */
public class PriceValidator implements Validator<Float> {
    public String getDescr() {
        return "Should be greater than 0";
    }
    @Override
    public boolean validate(Float value) {
        return value > 0 && value < Float.MAX_VALUE;
    }
}
