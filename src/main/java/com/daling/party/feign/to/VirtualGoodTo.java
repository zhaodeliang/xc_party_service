package com.daling.party.feign.to;

public class VirtualGoodTo implements java.io.Serializable {

    private static final long serialVersionUID = -6544652001529030601L;

    /**
     * 商品id
     */
    private Long id;

    /**
     * sku
     */
    private String sku;

    private String channel;
    private String channelSkuCode;
    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannelSkuCode() {
        return channelSkuCode;
    }

    public void setChannelSkuCode(String channelSkuCode) {
        this.channelSkuCode = channelSkuCode;
    }
}
