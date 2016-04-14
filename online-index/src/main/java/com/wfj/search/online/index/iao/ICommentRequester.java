package com.wfj.search.online.index.iao;

import com.wfj.search.online.common.pojo.CommentPojo;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Optional;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_COMMENT;

/**
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ICommentRequester {
    /**
     * 分页查询评论列表
     *
     * @param start 记录开始位置
     * @param fetch 请求取回数量
     * @return 评论列表
     * @throws RequestException
     * @see <a href="http://172.17.254.246:8090/pages/viewpage.action?pageId=3834189">HTTP-01-分页列出评论信息</a>
     */
    List<CommentPojo> listCommentsWithPage(int start, int fetch) throws RequestException;

    /**
     * 根据条件分页查询评论列表
     *
     * @param memberId 会员id
     * @param spuId    spu id
     * @param skuId    sku id
     * @param itemId   专柜商品id
     * @param orderNo  订单号
     * @param start    记录开始位置
     * @param fetch    请求取回数量
     * @return 根据条件查询的评论列表
     * @see <a href="http://172.17.254.246:8090/pages/viewpage.action?pageId=3834189">HTTP-01-分页列出评论信息</a>
     */
    List<CommentPojo> listCommentsWithPage(String memberId, String spuId, String skuId, String itemId, String orderNo,
            int start, int fetch) throws RequestException;

    /**
     * 查询评论总数
     *
     * @return 评论总数
     * @throws RequestException
     * @see <a href="http://172.17.254.246:8090/pages/viewpage.action?pageId=3834189">HTTP-01-分页列出评论信息</a>
     */
    int count() throws RequestException;

    /**
     * 根据条件过滤的评论总数
     *
     * @param memberId 会员id
     * @param spuId    spu id
     * @param skuId    sku id
     * @param itemId   专柜商品id
     * @param orderNo  订单号
     * @return 根据条件过滤的评论总数
     * @see <a href="http://172.17.254.246:8090/pages/viewpage.action?pageId=3834189">HTTP-01-分页列出评论信息</a>
     */
    int count(String memberId, String spuId, String skuId, String itemId, String orderNo) throws RequestException;

    /**
     * 根据评论id数组查询评论信息
     *
     * @param commentIds 评论id数组
     * @return 评论信息，如果某评论id错误，对应评论信息不返回。
     */
    List<CommentPojo> listCommentsByIds(List<String> commentIds) throws RequestException;

    /**
     * 根据评论id查询评论信息
     *
     * @param commentId 评论id
     * @return 评论信息
     * @throws RequestException
     */
    Optional<CommentPojo> getCommentById(String commentId) throws RequestException;

    @CacheEvict(value = VALUE_KEY_COMMENT, allEntries = true)
    default void clearCache() {
    }
}
