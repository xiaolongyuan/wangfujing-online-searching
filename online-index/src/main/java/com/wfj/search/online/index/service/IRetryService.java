package com.wfj.search.online.index.service;

import com.wfj.search.online.index.pojo.RetryResultCollector;
import com.wfj.search.online.index.pojo.failure.Failure;

import java.util.Optional;

import static com.wfj.search.online.common.pojo.OnlineRetryNotePojo.*;

/**
 * <br/>create at 15-11-2
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IRetryService {
    /**
     * 将保存失败item重新写入前置存储
     *
     * @param version 版本号
     * @return 错误信息
     */
    Optional<RetryResultCollector<Failure>> retrySaveItem(long version);

    /**
     * 重建专柜商品索引
     *
     * @param version 版本号
     * @return 错误信息
     */
    Optional<RetryResultCollector<String>> retryIndexItem(long version);

    /**
     * 将保存失败评论重新写入前置存储
     *
     * @param version 版本号
     * @return 错误信息
     */
    Optional<RetryResultCollector<String>> retrySaveComment(long version);

    /**
     * 重建评论索引
     *
     * @param version 版本号
     * @return 错误信息
     */
    Optional<RetryResultCollector<String>> retryIndexComment(long version);

    /**
     * 添加未解决专柜商品索引记录
     *
     * @param code   编号
     * @param step   出错位置
     * @param type   重试类别
     * @param action 操作类型
     * @return 添加条数，正常情况返回1，code为空返回-1，未正常添加返回0
     */
    int addUnresolvedRetryNote(String code, Step step, Type type, Action action);

    /**
     * 修改未解决索引记录
     *
     * @param code   编号
     * @param step   出错位置
     * @param type   重试类别
     * @param action 操作类型
     * @return 修改条数，正常情况返回1，code为空返回-1，未正常添加返回0
     */
    int updateUnresolvedRetryNote(String code, Step step, Type type, Action action);

    /**
     * 重写未解决索引记录
     *
     * @param code     编号
     * @param resolved 解决情况，true表示成功解决，false表示失败
     * @param step     出错位置
     * @param type     重试类别
     * @param action   操作类型
     * @param version  版本号，乐观锁标识
     * @return 修改条数，正常情况返回1，code为空返回-1。version版本号不等于当前版本（数据过期），返回0.
     */
    int rewriteRetryNote(String code, boolean resolved, Step step, Type type, Action action, long version);

    /**
     * 清理无效记录
     *
     * @param code    编号
     * @param comment 备注
     * @param type    类型
     * @return 修改条数，正常情况返回1，code为空返回-1，无对应数据返回0。
     */
    int cleanRetryNode(String code, String comment, Type type);
}
