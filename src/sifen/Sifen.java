package sifen;

import sifen.internal.request.ReqRecDe;
import sifen.internal.request.ReqConsLoteDe;
import sifen.internal.request.ReqConsDe;
import sifen.internal.request.ReqRecEventoDe;
import sifen.internal.request.ReqConsRuc;
import sifen.internal.request.ReqRecLoteDe;
import sifen.core.beans.response.RespuestaRecepcionEvento;
import sifen.core.beans.response.RespuestaConsultaRUC;
import sifen.core.beans.response.RespuestaRecepcionLoteDE;
import sifen.core.beans.response.RespuestaRecepcionDE;
import sifen.core.beans.response.RespuestaConsultaLoteDE;
import sifen.core.beans.response.RespuestaConsultaDE;
import sifen.core.SifenConfig;
import sifen.core.beans.DocumentoElectronico;
import sifen.core.beans.EventosDE;
import sifen.core.beans.ValidezFirmaDigital;
import sifen.core.exceptions.SifenException;
import sifen.internal.helpers.SignatureHelper;
import sifen.internal.util.SifenExceptionUtil;
import sifen.internal.util.SifenUtil;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * Clase principal de la librería desde la cuál se realizan todas las operaciones de Sifen.
 */
public class Sifen {
    private final static Logger logger = Logger.getLogger(Sifen.class.toString());
    private static SifenConfig sifenConfig = null;
    private static long dId = 1;

    /**
     * Establece la configuración necesaria para el funcionamiento correcto de todas las funcionalidades. Solo
     * debe realizarse una vez al principio, antes de ejecutar alguna acción. Si la configuración necesita ser
     * actualizada, simplemente invocar de vuelta.
     *
     * @param newSifenConfig El objeto de configuración que será utilizado.
     * @throws SifenException Si la configuración es nula o, si existe algún error en los valores de la configuración.
     */
    public static void setSifenConfig(SifenConfig newSifenConfig) throws SifenException {
        if (newSifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("La configuración de Sifen no debe ser nula.");
        }

        validateConfiguration(newSifenConfig);
        sifenConfig = newSifenConfig;
        logger.info("Configuración de Sifen guardada correctamente");
    }

    /**
     * @return El objeto de configuración previamente establecido.
     */
    public static SifenConfig getSifenConfig() {
        return sifenConfig;
    }

    /**
     * Realiza una consulta a Sifen y devuelve como resultado los datos y el estado del RUC de un contribuyente.
     *
     * @param ruc RUC de un contribuyente a ser consultado en Sifen, sin el DV.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la consulta no pudo ser realizada.
     */
    public static RespuestaConsultaRUC consultaRUC(String ruc) throws SifenException {
        return consultaRUC(ruc, sifenConfig);
    }

    /**
     * Realiza una consulta a Sifen y devuelve como resultado los datos y el estado del RUC de un contribuyente.
     *
     * @param ruc         RUC de un contribuyente a ser consultado en Sifen, sin el DV.
     * @param sifenConfig Configuración de Sifen a ser utilizada en esta petición.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la consulta no pudo ser realizada.
     */
    public static RespuestaConsultaRUC consultaRUC(String ruc, SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("Falta establecer la configuraci�n de Sifen.");
        }

        validateConfiguration(sifenConfig);
        logger.info("Preparando petici�n 'Consulta de RUC'");
        ReqConsRuc reqConsRuc = new ReqConsRuc(dId++, sifenConfig);
        reqConsRuc.setdRUCCons(ruc);

        return (RespuestaConsultaRUC) reqConsRuc.makeRequest(sifenConfig.getPathConsultaRUC());
    }

    /**
     * Realiza una consulta a Sifen y devuelve como resultado el Documento Electrónico encontrado y todos sus eventos asociados.
     *
     * @param cdc Código de Control, que es el identificador único de un Documento Electrónico.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la consulta no pudo ser realizada.
     */
    public static RespuestaConsultaDE consultaDE(String cdc) throws SifenException {
        return consultaDE(cdc, sifenConfig);
    }

    /**
     * Realiza una consulta a Sifen y devuelve como resultado el Documento Electrónico encontrado y todos sus eventos asociados.
     *
     * @param cdc         Código de Control, que es el identificador único de un Documento Electrónico.
     * @param sifenConfig Configuración de Sifen a ser utilizada en esta petición.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la consulta no pudo ser realizada.
     */
    public static RespuestaConsultaDE consultaDE(String cdc, SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("Falta establecer la configuración de Sifen.");
        }

        validateConfiguration(sifenConfig);
        logger.info("Preparando peticion 'Consulta de DE'");
        ReqConsDe reqConsDe = new ReqConsDe(dId++, sifenConfig);
        reqConsDe.setdCDC(cdc);

        return (RespuestaConsultaDE) reqConsDe.makeRequest(sifenConfig.getPathConsulta());
    }

    /**
     * Realiza una consulta a Sifen y devuelve como resultado el estado del lote consultado.
     *
     * @param nroLote Número de Lote recibido como respuesta en el envío del mismo.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la consulta no pudo ser realizada.
     */
    public static RespuestaConsultaLoteDE consultaLoteDE(String nroLote) throws SifenException {
        return consultaLoteDE(nroLote, sifenConfig);
    }

    /**
     * Realiza una consulta a Sifen y devuelve como resultado el estado del lote consultado.
     *
     * @param nroLote     Número de Lote recibido como respuesta en el envío del mismo.
     * @param sifenConfig Configuración de Sifen a ser utilizada en esta petición.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la consulta no pudo ser realizada.
     */
    public static RespuestaConsultaLoteDE consultaLoteDE(String nroLote, SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("Falta establecer la configuración de Sifen.");
        }

        validateConfiguration(sifenConfig);
        logger.info("Preparando petición 'Consulta de Resultado de Lote de DE'");
        ReqConsLoteDe reqConsLoteDe = new ReqConsLoteDe(dId++, sifenConfig);
        reqConsLoteDe.setdProtConsLote(nroLote);

        return (RespuestaConsultaLoteDE) reqConsLoteDe.makeRequest(sifenConfig.getPathConsultaLote());
    }

    /**
     * Realiza un envío del Documento Electrónico a Sifen para su correspondiente aprobación.
     *
     * @param de Objeto que hace referencia a un Documento Electrónico, con todos sus datos.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la firma digital del DE falla o, si la consulta no pudo ser realizada.
     */
    public static RespuestaRecepcionDE recepcionDE(DocumentoElectronico de) throws SifenException {
        return recepcionDE(de, sifenConfig);
    }

    /**
     * Realiza un envío del Documento Electrónico a Sifen para su correspondiente aprobación.
     *
     * @param de          Objeto que hace referencia a un Documento Electrónico, con todos sus datos.
     * @param sifenConfig Configuración de Sifen a ser utilizada en esta petición.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la firma digital del DE falla o, si la consulta no pudo ser realizada.
     */
    public static RespuestaRecepcionDE recepcionDE(DocumentoElectronico de, SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("Falta establecer la configuración de Sifen.");
        }

        validateConfiguration(sifenConfig);
        logger.info("Preparando petición 'Recepción de DE'");
        ReqRecDe reqRecDe = new ReqRecDe(dId++, sifenConfig);
        reqRecDe.setDE(de);

        return (RespuestaRecepcionDE) reqRecDe.makeRequest(sifenConfig.getPathRecibe());
    }

    /**
     * Realiza un envío de un lote de Documentos Electrónicos a Sifen para su correspondiente aprobación. La respuesta
     * de la aprobación o rechazo de cada DE es asíncrono, es decir, no se encuentra en la respuesta de esta petición.
     *
     * @param deList Listado de los objetos que hacen referencia a los Documentos Electrónicos, con todos los datos.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario de algún DE
     *                        no pudo ser encontrado o, si la forma digital de algún DE falla o, si la consulta no pudo ser realizada.
     */
    public static RespuestaRecepcionLoteDE recepcionLoteDE(List<DocumentoElectronico> deList) throws SifenException {
        return recepcionLoteDE(deList, sifenConfig);
    }

    /**
     * Realiza un envío de un lote de Documentos Electrónicos a Sifen para su correspondiente aprobación. La respuesta
     * de la aprobación o rechazo de cada DE es asíncrono, es decir, no se encuentra en la respuesta de esta petición.
     *
     * @param deList      Listado de los objetos que hacen referencia a los Documentos Electrónicos, con todos los datos.
     * @param sifenConfig Configuración de Sifen a ser utilizada en esta petición.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario de algún DE
     *                        no pudo ser encontrado o, si la forma digital de algún DE falla o, si la consulta no pudo ser realizada.
     */
    public static RespuestaRecepcionLoteDE recepcionLoteDE(List<DocumentoElectronico> deList, SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("Falta establecer la configuración de Sifen.");
        }

        validateConfiguration(sifenConfig);
        logger.info("Preparando petición 'Recepción de Lote de DE'");
        ReqRecLoteDe reqRecLoteDe = new ReqRecLoteDe(dId++, sifenConfig);
        reqRecLoteDe.setDEList(deList);

        return (RespuestaRecepcionLoteDE) reqRecLoteDe.makeRequest(sifenConfig.getPathRecibeLote());
    }

    /**
     * Realiza un envío a Sifen de los eventos agregados en el objeto recibido como argumento.
     *
     * @param eventosDE Objeto que contiene el listado de eventos a ser enviados a Sifen.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la firma digital de algún evento falla o, si la consulta no pudo ser
     *                        realizada.
     */
    public static RespuestaRecepcionEvento recepcionEvento(EventosDE eventosDE) throws SifenException {
        return recepcionEvento(eventosDE, sifenConfig);
    }

    /**
     * Realiza un envío a Sifen de los eventos agregados en el objeto recibido como argumento.
     *
     * @param eventosDE   Objeto que contiene el listado de eventos a ser enviados a Sifen.
     * @param sifenConfig Configuración de Sifen a ser utilizada en esta petición.
     * @return La respuesta a la consulta proveída por Sifen, en forma de clase.
     * @throws SifenException Si la configuración de Sifen no fue establecida o, si algún dato necesario para la
     *                        consulta no pudo ser encontrado o, si la firma digital de algún evento falla o, si la consulta no pudo ser
     *                        realizada.
     */
    public static RespuestaRecepcionEvento recepcionEvento(EventosDE eventosDE, SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig == null) {
            throw SifenExceptionUtil.invalidConfiguration("Falta establecer la configuración de Sifen.");
        }

        validateConfiguration(sifenConfig);
        logger.info("Preparando petición 'Recepción de Eventos'");
        ReqRecEventoDe reqRecEventoDe = new ReqRecEventoDe(dId++, sifenConfig);
        reqRecEventoDe.setEventoDE(eventosDE);

        return (RespuestaRecepcionEvento) reqRecEventoDe.makeRequest(sifenConfig.getPathEvento());
    }

    /**
     * Verifica si la firma digital del Documento Electrónico recibido como argumento es válida.
     *
     * @param xml Cadena de texto correspondiente al Documento Electrónico a validar, en formato XML.
     * @return Instancia del objeto <i>ValidezFirmaDigital</i>, especificando si la firma es válida, el
     * motivo en caso de que no lo sea, y los datos del sujeto encontrados en el certificado.
     */
    public static ValidezFirmaDigital validarFirmaDEDesdeXml(String xml) {
        return SignatureHelper.validateSignature(xml, "XML");
    }

    /**
     * Verifica si la firma digital del Documento Electrónico recibido como argumento es válida.
     *
     * @param rutaArchivo Ruta del archivo correspondiente al Documento Electrónico a validar, en formato XML.
     * @return Instancia del objeto <i>ValidezFirmaDigital</i>, especificando si la firma es válida, el
     * motivo en caso de que no lo sea, y los datos del sujeto encontrados en el certificado.
     */
    public static ValidezFirmaDigital validarFirmaDE(String rutaArchivo) {
        return SignatureHelper.validateSignature(rutaArchivo, "PATH");
    }

    /**
     * Verifica si la firma digital del Documento Electrónico recibido como argumento es válida.
     *
     * @param archivoXml Instancia de un archivo correspondiente al Documento Electrónico a validar, en formato XML.
     * @return Instancia del objeto <i>ValidezFirmaDigital</i>, especificando si la firma es válida, el
     * motivo en caso de que no lo sea, y los datos del sujeto encontrados en el certificado.
     */
    public static ValidezFirmaDigital validarFirmaDE(File archivoXml) {
        return SignatureHelper.validateSignature(archivoXml);
    }

    private static void validateConfiguration(SifenConfig sifenConfig) throws SifenException {
        if (sifenConfig.getAmbiente() == null) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: Tipo de ambiente no establecido.");
        }

        if (SifenUtil.isBlank(sifenConfig.getUrlBaseLocal())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL Base no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getUrlConsultaQr())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL de consulta QR no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getPathRecibe())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL 'Recepción de DE' no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getPathRecibeLote())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL 'Recepción de Lote de DE' no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getPathEvento())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL 'Recepción de Eventos' no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getPathConsultaLote())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL 'Consulta de Lote' no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getPathConsultaRUC())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL 'Consulta de RUC' no establecida.");
        }

        if (SifenUtil.isBlank(sifenConfig.getPathConsulta())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: URL 'Consulta de DE' no establecida.");
        }

        if (sifenConfig.isUsarCertificadoCliente()) {
            if (SifenUtil.isBlank(sifenConfig.getCertificadoCliente())) {
                throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: Certificado digital no establecido.");
            }

            if (sifenConfig.getTipoCertificadoCliente() == null) {
                throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: Tipo de certificado digital no establecido.");
            }

            if (sifenConfig.getTipoCertificadoCliente().equals(SifenConfig.TipoCertificadoCliente.PFX)) {
                if (SifenUtil.isBlank(sifenConfig.getContrasenaCertificadoCliente())) {
                    throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: Contraseña del certificado digital no establecida.");
                }
            }
        }

        if (SifenUtil.isBlank(sifenConfig.getIdCSC())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: ID del CSC no establecido.");
        }

        if (SifenUtil.isBlank(sifenConfig.getCSC())) {
            throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: CSC no establecido.");
        }

        if (sifenConfig.getAmbiente().equals(SifenConfig.TipoAmbiente.PROD)) {
            if (sifenConfig.getIdCSC().equals("0001") && sifenConfig.getCSC().equals("ABCD0000000000000000000000000000") ||
                    sifenConfig.getIdCSC().equals("0002") && sifenConfig.getCSC().equals("EFGH0000000000000000000000000000")) {
                throw SifenExceptionUtil.invalidConfiguration("Error en la configuración de Sifen: El CSC establecido solo " +
                        "es utilizable en el ambiente de desarrollo. Solicitar a la SET el correspondiente a producción, " +
                        "en caso de no poseerlo.");
            }
        }
    }
}
