package com.wfj.search.online.index.pojo.failure;

/**
 * <br/>create at 15-12-29
 *
 * @author liuxh
 * @author liufl
 * @since 1.0.0
 */
public enum FailureType {
    initial("初始化"),
    unknown("未知错误"),
    requestError("请求出错"),
    invalidData("无效数据(请求无效或被筛选)"),
    buildData("构造数据错误"),
    save2DB("向数据库保存数据出错"),
    retrieveFromDB("从数据库查询数据出错"),
    save2ES("向ES保存数据出错"),
    updateES("修改ES中的数据出错"),
    deleteFromES("从ES中删除数据错误"),
    retrieveFromES("从ES中查询数据错误"),
    save2Index("向索引中保存数据错误"),
    deleteFromIndex("从索引中删除数据错误");

    private String name;

    FailureType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
