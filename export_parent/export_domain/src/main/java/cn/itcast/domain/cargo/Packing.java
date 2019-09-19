package cn.itcast.domain.cargo;

import java.io.Serializable;
import java.util.Date;

public class Packing implements Serializable {
    private String packingListId;

    private String seller;

    private String buyer;

    private String invoiceNo;

    private Date invoiceDate;

    private String marks;

    private String descriptions;

    private String exportIds;

    private String exportNos;

    private Integer state;

    private String createBy;

    private String createDept;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getPackingListId() {
        return packingListId;
    }

    public void setPackingListId(String packingListId) {
        this.packingListId = packingListId == null ? null : packingListId.trim();
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller == null ? null : seller.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks == null ? null : marks.trim();
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions == null ? null : descriptions.trim();
    }

    public String getExportIds() {
        return exportIds;
    }

    public void setExportIds(String exportIds) {
        this.exportIds = exportIds == null ? null : exportIds.trim();
    }

    public String getExportNos() {
        return exportNos;
    }

    public void setExportNos(String exportNos) {
        this.exportNos = exportNos == null ? null : exportNos.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getCreateDept() {
        return createDept;
    }

    public void setCreateDept(String createDept) {
        this.createDept = createDept == null ? null : createDept.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}