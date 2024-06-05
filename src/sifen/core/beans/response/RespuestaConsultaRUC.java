package sifen.core.beans.response;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectFactory;
import sifen.internal.response.BaseResponse;
import sifen.core.fields.response.ruc.TxContRuc;
import org.w3c.dom.Node;

/**
 * Clase principal que contiene la respuesta de Sifen a la consulta de un RUC de un contribuyente.
 */
public class RespuestaConsultaRUC extends BaseResponse {
    private TxContRuc xContRUC;

    /**
     * Método interno, no usar.
     */
    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        if (value.getLocalName().equals("xContRUC")) {
            xContRUC = SifenObjectFactory.getFromNode(value, TxContRuc.class);
        } else {
            super.setValueFromChildNode(value);
        }
    }

    public TxContRuc getxContRUC() {
        return xContRUC;
    }
}