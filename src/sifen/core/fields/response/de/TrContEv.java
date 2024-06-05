package sifen.core.fields.response.de;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectBase;
import sifen.internal.response.SifenObjectFactory;
import sifen.core.beans.EventosDE;
import sifen.core.beans.response.RespuestaRecepcionEvento;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TrContEv extends SifenObjectBase {
    private EventosDE xEvento;
    private RespuestaRecepcionEvento rResEnviEventoDe;

    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        switch (value.getLocalName()) {
            case "xEvento":
                xEvento = SifenObjectFactory.getFromNode(value, EventosDE.class);
                break;
            case "rResEnviEventoDe":
                NodeList childNodes = value.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if (node.getLocalName().equals("rRetEnviEventoDe")) {
                        rResEnviEventoDe = SifenObjectFactory.getFromNode(node, RespuestaRecepcionEvento.class);
                    }
                }
                break;
        }
    }

    public EventosDE getxEvento() {
        return xEvento;
    }

    public RespuestaRecepcionEvento getrResEnviEventoDe() {
        return rResEnviEventoDe;
    }
}