package sifen.core.fields.request.event;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectBase;
import sifen.internal.util.ResponseUtil;
import org.w3c.dom.Node;

public class TrGeVeRem extends SifenObjectBase {
    private String Id;

    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        if (value.getLocalName().equals("Id")) {
            this.Id = ResponseUtil.getTextValue(value);
        }
    }

    public String getId() {
        return Id;
    }
}