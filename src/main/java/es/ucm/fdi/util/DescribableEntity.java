package es.ucm.fdi.util;

import java.util.Map;

public abstract class DescribableEntity implements Describable{
	public abstract void describe(Map<String, String> out);
}
