package sifen.internal.request;

import sifen.core.SifenConfig;
import sifen.core.exceptions.SifenException;
import sifen.internal.SOAPResponse;
import sifen.internal.helpers.SoapHelper;
import sifen.internal.response.BaseResponse;
import sifen.internal.util.ResponseUtil;
import sifen.internal.Constants;
import sifen.internal.response.SifenObjectFactory;
import sifen.core.beans.response.RespuestaRecepcionEvento;
import sifen.core.beans.EventosDE;
import sifen.internal.util.SifenExceptionUtil;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Clase encargada de la petición de Recepción de Eventos de Documentos Electrónicos.
 */
public class ReqRecEventoDe extends BaseRequest {
    private EventosDE eventosDE;
    private final static Logger logger = Logger.getLogger(ReqRecEventoDe.class.toString());

    public ReqRecEventoDe(long dId, SifenConfig sifenConfig) {
        super(dId, sifenConfig);
    }

    @Override
    SOAPMessage setupSoapMessage() throws SifenException {
        try {
            // Main Element
            SOAPMessage message = SoapHelper.createSoapMessage();
            SOAPBody soapBody = message.getSOAPBody();

            SOAPBodyElement rEnviEventoDe = soapBody.addBodyElement(new QName(Constants.SIFEN_NS_URI, "rEnviEventoDe"));
            rEnviEventoDe.addChildElement("dId").setTextContent(String.valueOf(this.getdId()));

            SOAPElement gGroupGesEve = rEnviEventoDe.addChildElement("dEvReg").addChildElement("gGroupGesEve");

            gGroupGesEve.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            gGroupGesEve.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", Constants.SIFEN_NS_URI_RECEP_EVENTO);

            // Se completa con los demás elementos del XML
            this.eventosDE.setupSOAPElements(gGroupGesEve, this.getSifenConfig());

            return message;
        } catch (SOAPException e) {
            throw SifenExceptionUtil.requestPreparationError("Ocurrió un error al preparar el cuerpo de la petición SOAP", e);
        }
    }

    @Override
    BaseResponse processResponse(SOAPResponse soapResponse) throws SifenException {
        Node rRetEnviEventoDe = null;
        try {
            rRetEnviEventoDe = ResponseUtil.getMainNode(soapResponse.getSoapResponse(), "rRetEnviEventoDe");
        } catch (SifenException e) {
            logger.warning(e.getMessage());
        }

        RespuestaRecepcionEvento respuestaRecepcionEvento = new RespuestaRecepcionEvento();
        if (rRetEnviEventoDe != null) {
            respuestaRecepcionEvento = SifenObjectFactory.getFromNode(rRetEnviEventoDe, RespuestaRecepcionEvento.class);
        }

        respuestaRecepcionEvento.setCodigoEstado(soapResponse.getStatus());
        respuestaRecepcionEvento.setRespuestaBruta(new String(soapResponse.getRawData(), StandardCharsets.UTF_8));
        return respuestaRecepcionEvento;
    }

    public void setEventoDE(EventosDE eventosDE) {
        this.eventosDE = eventosDE;
    }
}