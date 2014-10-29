package knorxx.framework.generator.web.generator.testclass;

/**
 *
 * @author sj
 */
public enum TestEnumWithValues {
	
	FIRST("Test", 42, false),
	SECOND("Bla", 17, true);
	
	private final String text;
	private final int number;
	private final boolean bool;

	private TestEnumWithValues(String text, int number, boolean bool) {
		this.text = text;
		this.number = number;
		this.bool = bool;
	}

	public String getText() {
		return text;
	}

	public int getNumber() {
		return number;
	}
	
	public boolean getBool() {
		return bool;
	}
}
