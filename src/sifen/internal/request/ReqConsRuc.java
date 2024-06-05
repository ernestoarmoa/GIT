package sifen.internal.request;

import sifen.core.SifenConfig;
import sifen.core.exceptions.SifenException;
import sifen.internal.SOAPResponse;
import sifen.internal.helpers.SoapHelper;
import sifen.internal.response.BaseResponse;
import sifen.internal.util.ResponseUtil;
import sifen.internal.util.SifenExceptionUtil;
import sifen.internal.Constants;
import sifen.internal.response.SifenObjectFactory;
import sifen.core.beans.response.RespuestaConsultaRUC;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Clase encargada de la Consulta de RUC.
 */
public class ReqConsRuc extends BaseRequest {
    private String dRUCCons;
    private final static Logger logger = Logger.getLogger(ReqConsRuc.class.toString());

    public ReqConsRuc(long dId, SifenConfig sifenConfig) {
        super(dId, sifenConfig);
    }

    @Override
    SOAPMessage setupSoapMessage() throws SifenException {
        try {
            // Main Element
            SOAPMessage message = SoapHelper.createSoapMessage();
            SOAPBody soapBody = message.getSOAPBody();
            SOAPBodyElement rResEnviConsRUC = soapBody.addBodyElement(new QName(Constants.SIFEN_NS_URI, "rEnviConsRUC"));

            // Elements
            rResEnviConsRUC.addChildElement("dId").setTextContent(String.valueOf(this.getdId()));
            rResEnviConsRUC.addChildElement("dRUCCons").setTextContent(this.dRUCCons);

            return message;
        } catch (SOAPException e) {
            throw SifenExceptionUtil.requestPreparationError("Ocurrio un error al preparar el cuerpo de la peticion SOAP", e);
        }
    }

    @Override
    BaseResponse processResponse(SOAPResponse soapResponse) throws SifenException {
        Node rResEnviConsRuc = null;
        try {
            rResEnviConsRuc = ResponseUtil.getMainNode(soapResponse.getSoapResponse(), "rResEnviConsRuc");
        } catch (SifenException e) {
            logger.warning(e.getMessage());
        }

        RespuestaConsultaRUC respuestaConsultaRUC = new RespuestaConsultaRUC();
        if (rResEnviConsRuc != null) {
            respuestaConsultaRUC = SifenObjectFactory.getFromNode(rResEnviConsRuc, RespuestaConsultaRUC.class);
        }

        respuestaConsultaRUC.setCodigoEstado(soapResponse.getStatus());
        respuestaConsultaRUC.setRespuestaBruta(new String(soapResponse.getRawData(), StandardCharsets.UTF_8));
        return respuestaConsultaRUC;
    }

    public void setdRUCCons(String dRUCCons) {
        this.dRUCCons = dRUCCons;
    }
}