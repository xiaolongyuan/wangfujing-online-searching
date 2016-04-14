package com.wfj.search.online.index.pojo;

import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * <br/>create at 15-11-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CommentIndexPojo {
    @Field("commentId")
    private String commentId;
    @Field("itemId")
    private String itemId;
    @Field("skuId")
    private String skuId;
    @Field("spuId")
    private String spuId;
    @Field("memberId")
    private String memberId;
    @Field("memberName")
    private String memberName;
    @Field("memberLevel")
    private String memberLevel;
    @Field("memberImg")
    private String memberImg;
    @Field("title")
    private String title;
    @Field("content")
    private String content;
    @Field("userScore")
    private int userScore;
    @Field("createTime")
    private String createTime;
    @Field("orderNo")
    private String orderNo;
    @Field("productName")
    private String productName;
    @Field("orderCreatedTime")
    private String orderCreatedTime;
    @Field("isAnonymity")
    private int isAnonymity;
    @Field("mind")
    private String mind;
    @Field("pics")
    private List<String> pics = Lists.newArrayList();
    @Field("operationSid")
    private Long operationSid;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    @SuppressWarnings("unused")
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @SuppressWarnings("unused")
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @SuppressWarnings("unused")
    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    @SuppressWarnings("unused")
    public String getMemberImg() {
        return memberImg;
    }

    public void setMemberImg(String memberImg) {
        this.memberImg = memberImg;
    }

    @SuppressWarnings("unused")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("unused")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @SuppressWarnings("unused")
    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    @SuppressWarnings("unused")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getOperationSid() {
        return operationSid;
    }

    public void setOperationSid(Long operationSid) {
        this.operationSid = operationSid;
    }

    @SuppressWarnings("unused")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @SuppressWarnings("unused")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @SuppressWarnings("unused")
    public String getOrderCreatedTime() {
        return orderCreatedTime;
    }

    public void setOrderCreatedTime(String orderCreatedTime) {
        this.orderCreatedTime = orderCreatedTime;
    }

    @SuppressWarnings("unused")
    public int getIsAnonymity() {
        return isAnonymity;
    }

    public void setIsAnonymity(int isAnonymity) {
        this.isAnonymity = isAnonymity;
    }

    @SuppressWarnings("unused")
    public String getMind() {
        return mind;
    }

    public void setMind(String mind) {
        this.mind = mind;
    }

    public List<String> getPics() {
        return pics;
    }

    @SuppressWarnings("unused")
    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    @Override
    public String toString() {
        return "CommentIndexPojo{" +
                "commentId='" + commentId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", spuId='" + spuId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userScore=" + userScore +
                ", createTime=" + createTime +
                ", orderNo='" + orderNo + '\'' +
                ", productName='" + productName + '\'' +
                ", orderCreatedTime=" + orderCreatedTime +
                ", isAnonymity=" + isAnonymity +
                ", mind='" + mind + '\'' +
                ", pics=" + pics +
                ", operationSid=" + operationSid +
                '}';
    }
}
