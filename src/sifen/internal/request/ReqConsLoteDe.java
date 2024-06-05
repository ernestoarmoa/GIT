package sifen.internal.request;

import sifen.core.SifenConfig;
import sifen.core.beans.response.RespuestaConsultaLoteDE;
import sifen.core.exceptions.SifenException;
import sifen.internal.Constants;
import sifen.internal.SOAPResponse;
import sifen.internal.helpers.SoapHelper;
import sifen.internal.response.BaseResponse;
import sifen.internal.response.SifenObjectFactory;
import sifen.internal.util.ResponseUtil;
import sifen.internal.util.SifenExceptionUtil;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Clase encargada de la Consulta del Resultado de Lote de Documentos Electrónicos.
 */
public class ReqConsLoteDe extends BaseRequest {
    private String dProtConsLote;
    private final static Logger logger = Logger.getLogger(ReqConsLoteDe.class.toString());

    public ReqConsLoteDe(long dId, SifenConfig sifenConfig) {
        super(dId, sifenConfig);
    }

    @Override
    SOAPMessage setupSoapMessage() throws SifenException {
        try {
            // Main Element
            SOAPMessage message = SoapHelper.createSoapMessage();
            SOAPBody soapBody = message.getSOAPBody();
            SOAPBodyElement rEnviConsLoteDe = soapBody.addBodyElement(new QName(Constants.SIFEN_NS_URI, "rEnviConsLoteDe"));

            // Elements
            rEnviConsLoteDe.addChildElement("dId").setTextContent(String.valueOf(this.getdId()));
            rEnviConsLoteDe.addChildElement("dProtConsLote").setTextContent(this.dProtConsLote);

            return message;
        } catch (SOAPException e) {
            throw SifenExceptionUtil.requestPreparationError("Ocurrió un error al preparar el cuerpo de la petición SOAP", e);
        }
    }

    @Override
    BaseResponse processResponse(SOAPResponse soapResponse) throws SifenException {
        Node rResEnviConsLoteDe = null;
        try {
            SOAPMessage soapMessage = ResponseUtil.parseSoapMessage(soapResponse.getSoapResponse());
            rResEnviConsLoteDe = ResponseUtil.getMainNode(soapMessage, "rResEnviConsLoteDe");
        } catch (SifenException e) {
            logger.warning(e.getMessage());
        }

        RespuestaConsultaLoteDE respuestaConsultaLoteDE = new RespuestaConsultaLoteDE();
        if (rResEnviConsLoteDe != null) {
            respuestaConsultaLoteDE = SifenObjectFactory.getFromNode(rResEnviConsLoteDe, RespuestaConsultaLoteDE.class);
        }

        respuestaConsultaLoteDE.setCodigoEstado(soapResponse.getStatus());
        respuestaConsultaLoteDE.setRespuestaBruta(new String(soapResponse.getRawData(), StandardCharsets.UTF_8));
        return respuestaConsultaLoteDE;
    }

    public void setdProtConsLote(String dProtConsLote) {
        this.dProtConsLote = dProtConsLote;
    }
}