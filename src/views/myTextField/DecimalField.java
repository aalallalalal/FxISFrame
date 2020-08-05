package views.myTextField;

import java.math.BigDecimal;

public class DecimalField extends AbstractTextField {

    private Integer scale = 4;

    public DecimalField() {

        super(TypeEnum.DECIMAL);
    }

    public DecimalField(final Integer scale) {

        this();
        if (scale < 0) {
            throw new NumberFormatException("Scale must great than equals to 0.");
        }
        this.scale = scale;
    }

    @Override
    public Integer decimalScale() {

        return this.scale;
    }

    @Override
    public BigDecimal getValue() {

        return getDecimalValue();
    }
}