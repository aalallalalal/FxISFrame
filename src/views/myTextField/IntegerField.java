package views.myTextField;

public class IntegerField extends AbstractTextField {

	public IntegerField() {

		super(TypeEnum.INTEGER);
	}

	@Override
	public Integer decimalScale() {

		return Integer.valueOf(0);
	}

	@Override
	public Integer getValue() {

		return getIntegerValue();
	}
}