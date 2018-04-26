package es.ucm.fdi.util;

import java.util.Map;

/*
 * Agrupa eventos con objetos de la simulación
 */
public abstract class DescribableEntity implements Describable{
	/*
	 * (non-Javadoc)
	 * @see es.ucm.fdi.util.Describable#describe(java.util.Map)
	 */
	public abstract void describe(Map<String, String> out);
}
