package sifen.core.beans.response;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectFactory;
import sifen.internal.response.BaseResponse;
import sifen.core.fields.response.TxProtDe;
import org.w3c.dom.Node;

/**
 * Clase principal que contiene la respuesta de Sifen al envío para aprobación de un Documento Electrónico.
 */
public class RespuestaRecepcionDE extends BaseResponse {
    private TxProtDe xProtDE;

    /**
     * Método interno, no usar.
     */
    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        if (value.getLocalName().equals("rProtDe")) {
            xProtDE = SifenObjectFactory.getFromNode(value, TxProtDe.class);
        }
    }

    public TxProtDe getxProtDE() {
        return xProtDE;
    }
}