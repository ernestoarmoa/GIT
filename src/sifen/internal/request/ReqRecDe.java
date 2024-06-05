package sifen.internal.request;

import sifen.core.SifenConfig;
import sifen.core.exceptions.SifenException;
import sifen.internal.SOAPResponse;
import sifen.internal.response.BaseResponse;
import sifen.internal.util.ResponseUtil;
import sifen.internal.util.SifenExceptionUtil;
import sifen.internal.response.SifenObjectFactory;
import sifen.core.beans.response.RespuestaRecepcionDE;
import sifen.core.beans.DocumentoElectronico;
import org.w3c.dom.Node;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Clase encargada de la petición de Recepción de Documentos Electrónicos.
 */
public class ReqRecDe extends BaseRequest {
    private DocumentoElectronico DE;
    private final static Logger logger = Logger.getLogger(ReqRecDe.class.toString());

    public ReqRecDe(long dId, SifenConfig sifenConfig) {
        super(dId, sifenConfig);
    }

    @Override
    SOAPMessage setupSoapMessage() throws SifenException {
        try {
            return this.DE.setupSOAPElements(this.getdId(), this.getSifenConfig());
        } catch (SOAPException e) {
            throw SifenExceptionUtil.requestPreparationError("Ocurrió un error al preparar el cuerpo de la petición SOAP", e);
        }
    }

    @Override
    BaseResponse processResponse(SOAPResponse soapResponse) throws SifenException {
        Node rRetEnviDe = null;
        try {
            rRetEnviDe = ResponseUtil.getMainNode(soapResponse.getSoapResponse(), "rRetEnviDe");
        } catch (SifenException e) {
            logger.warning(e.getMessage());
        }

        RespuestaRecepcionDE respuestaRecepcionDE = new RespuestaRecepcionDE();
        if (rRetEnviDe != null) {
            respuestaRecepcionDE = SifenObjectFactory.getFromNode(rRetEnviDe, RespuestaRecepcionDE.class);
        }

        respuestaRecepcionDE.setCodigoEstado(soapResponse.getStatus());
        respuestaRecepcionDE.setRespuestaBruta(new String(soapResponse.getRawData(), StandardCharsets.UTF_8));
        return respuestaRecepcionDE;
    }

    public void setDE(DocumentoElectronico DE) {
        this.DE = DE;
    }
}