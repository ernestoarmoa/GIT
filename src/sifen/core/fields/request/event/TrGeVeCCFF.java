package sifen.core.fields.request.event;

import sifen.core.exceptions.SifenException;
import sifen.internal.response.SifenObjectBase;
import sifen.internal.util.ResponseUtil;
import org.w3c.dom.Node;

import java.time.LocalDateTime;

public class TrGeVeCCFF extends SifenObjectBase {
    private String Id;
    private String dNumTraCCFF;
    private LocalDateTime dFeAceTraCCFF;

    @Override
    public void setValueFromChildNode(Node value) throws SifenException {
        switch (value.getLocalName()) {
            case "Id":
                this.Id = ResponseUtil.getTextValue(value);
                break;
            case "dNumTraCCFF":
                this.dNumTraCCFF = ResponseUtil.getTextValue(value);
                break;
            case "dFeAceTraCCFF":
                this.dFeAceTraCCFF = ResponseUtil.getDateTimeValue(value);
                break;
        }
    }

    public String getId() {
        return Id;
    }

    public String getdNumTraCCFF() {
        return dNumTraCCFF;
    }

    public LocalDateTime getdFeAceTraCCFF() {
        return dFeAceTraCCFF;
    }
}