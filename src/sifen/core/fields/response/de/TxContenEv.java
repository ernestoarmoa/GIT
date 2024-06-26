package sifen.core.fields.response.de;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectBase;
import sifen.internal.response.SifenObjectFactory;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class TxContenEv extends SifenObjectBase {
    private final List<TrContEv> rContEvList = new ArrayList<>();

    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        if (value.getLocalName().equals("rContEv")) {
            rContEvList.add(SifenObjectFactory.getFromNode(value, TrContEv.class));
        }
    }

    public List<TrContEv> getrContEvList() {
        return rContEvList;
    }
}