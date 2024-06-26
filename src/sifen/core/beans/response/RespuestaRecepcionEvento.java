package sifen.core.beans.response;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectFactory;
import sifen.internal.response.BaseResponse;
import sifen.internal.util.ResponseUtil;
import sifen.core.fields.response.event.TgResProcEVe;
import org.w3c.dom.Node;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que contiene la respuesta de Sifen al envío de eventos relacionados a los Documentos Electrónicos.
 */
public class RespuestaRecepcionEvento extends BaseResponse {
    private LocalDateTime dFecProc;
    private final List<TgResProcEVe> gResProcEVe = new ArrayList<>();

    /**
     * Método interno, no usar.
     */
    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        if (value.getLocalName().equals("dFecProc")) {
            dFecProc = ResponseUtil.getDateTimeValue(value);
        } else if (value.getLocalName().equals("gResProcEVe")) {
            gResProcEVe.add(SifenObjectFactory.getFromNode(value, TgResProcEVe.class));
        }
    }

    public LocalDateTime getdFecProc() {
        return dFecProc;
    }

    public List<TgResProcEVe> getgResProcEVe() {
        return gResProcEVe;
    }
}