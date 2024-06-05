package sifen.internal.response;

import sifen.core.exceptions.SifenException;
import org.w3c.dom.Node;

/**
 * Clase abstracta heredada por las clases utilizadas como parte de las respuestas a las peticiones.
 */
public abstract class SifenObjectBase {
    public abstract void setValueFromChildNode(Node value) throws SifenException;
}