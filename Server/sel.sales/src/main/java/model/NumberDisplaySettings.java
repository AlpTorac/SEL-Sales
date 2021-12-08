package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class NumberDisplaySettings {
	private MathContext mc;
	
	NumberDisplaySettings() {
		this.mc = this.initMathContext();
	}

	protected MathContext initMathContext() {
		return new MathContext(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal format(BigDecimal number) {
		return number.round(mc);
	}
}
