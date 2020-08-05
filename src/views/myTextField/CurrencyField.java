package views.myTextField;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyField extends AbstractTextField {

	public CurrencyField() {

		super(TypeEnum.CURRENCY);
	}

	@Override
	public Integer decimalScale() {

		Locale locale = Locale.getDefault();
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		return formatter.getCurrency().getDefaultFractionDigits();
	}

	@Override
	public BigDecimal getValue() {

		return getCurrencyValue();
	}
}