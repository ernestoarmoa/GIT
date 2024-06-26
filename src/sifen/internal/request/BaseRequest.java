package sifen.internal.request;

import sifen.core.SifenConfig;
import sifen.core.exceptions.SifenException;
import sifen.internal.SOAPResponse;
import sifen.internal.helpers.SoapHelper;
import sifen.internal.response.BaseResponse;
import sifen.internal.util.SifenExceptionUtil;
import sifen.internal.util.SifenUtil;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.logging.Logger;

abstract class BaseRequest {
    private final long dId;
    private final SifenConfig sifenConfig;
    private final static Logger logger = Logger.getLogger(BaseRequest.class.toString());

    BaseRequest(long dId, SifenConfig sifenConfig) {
        this.dId = dId;
        this.sifenConfig = sifenConfig;
    }

    abstract SOAPMessage setupSoapMessage() throws SifenException;

    abstract BaseResponse processResponse(SOAPResponse soapResponse) throws SifenException;

    public BaseResponse makeRequest(String url) throws SifenException {
        try {
            // Preparamos el mensaje
            SOAPMessage message = this.setupSoapMessage();
            message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            logger.info("XML generado, se realiza la petición");

            // Para obtener el xml
            /*final StringWriter sw = new StringWriter();

            try {
                TransformerFactory.newInstance().newTransformer().transform(
                        new DOMSource(message.getSOAPPart()),
                        new StreamResult(sw));
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }

            String xml = sw.toString();*/

            // Realizamos la consulta
            String requestUrl = SifenUtil.coalesce(sifenConfig.getUrlBase(), sifenConfig.getUrlBaseLocal()) + url;
            BaseResponse response = this.processResponse(SoapHelper.makeSoapRequest(sifenConfig, requestUrl, message));
            logger.info("Peticion realizada, se formatea la respuesta");
            return response;
        } catch (SOAPException e) {
            String msg = "Ocurrio un error al realizan la peticion a: " + url + ". Mensaje: " + e.getLocalizedMessage();
            throw SifenExceptionUtil.invalidSOAPRequest(msg, e);
        }
    }

    long getdId() {
        return dId;
    }

    SifenConfig getSifenConfig() {
        return sifenConfig;
    }
}