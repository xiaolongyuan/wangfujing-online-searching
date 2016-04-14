package com.wfj.search.online.index.pojo;

import java.util.*;

/**
 * 补偿结果收集器
 * <br/>create at 15-12-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RetryResultCollector<T> {
    private int total = 0;
    private final Set<T> successList = Collections.synchronizedSet(new HashSet<>());
    private final Map<RequestError, Set<T>> requestErrorMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<ESError, Set<T>> esErrorMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<IndexError, Set<T>> indexErrorMap = Collections.synchronizedMap(new HashMap<>());
    private final Set<T> failList = Collections.synchronizedSet(new HashSet<>());
    private final Set<T> overdueList = Collections.synchronizedSet(new HashSet<>());

    public RetryResultCollector() {
        for (RequestError e : RequestError.values()) {
            requestErrorMap.put(e, Collections.synchronizedSet(new HashSet<>()));
        }
        for (ESError e : ESError.values()) {
            esErrorMap.put(e, Collections.synchronizedSet(new HashSet<>()));
        }
        for (IndexError e : IndexError.values()) {
            indexErrorMap.put(e, Collections.synchronizedSet(new HashSet<>()));
        }
    }

    public boolean isFail() {
        return !requestErrorMap.isEmpty() || !esErrorMap.isEmpty() || !indexErrorMap.isEmpty()
                || !failList.isEmpty() || !overdueList.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder("重试结果：");
        msg.append("需要重建").append(total).append("条数据\n");
        msg.append("成功保存").append(successList.size()).append("条\n");
        for (RequestError e : RequestError.values()) {
            Set<T> el = requestErrorMap.get(e);
            if (!el.isEmpty()) {
                msg.append(e.getName()).append(el.size()).append("条，失败编码：").append(el).append("\n");
            }
        }
        for (ESError e : ESError.values()) {
            Set<T> el = esErrorMap.get(e);
            if (!el.isEmpty()) {
                msg.append(e.getName()).append(el.size()).append("条，失败编码：").append(el).append("\n");
            }
        }
        for (IndexError e : IndexError.values()) {
            Set<T> el = indexErrorMap.get(e);
            if (!el.isEmpty()) {
                msg.append(e.getName()).append(el.size()).append("条，失败编码：").append(el).append("\n");
            }
        }
        if (!failList.isEmpty()) {
            msg.append("操作失败").append(failList.size()).append("条，失败编码：").append(failList)
                    .append("\n");
        }
        if (!overdueList.isEmpty()) {
            msg.append("过期数据").append(overdueList.size()).append("条，失败编码：").append(overdueList).append("\n");
        }
        return msg.toString();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Set<T> getSuccessList() {
        return successList;
    }

    public Set<T> getFailList() {
        return failList;
    }

    public Set<T> getOverdueList() {
        return overdueList;
    }

    public Map<RequestError, Set<T>> getRequestErrorMap() {
        return requestErrorMap;
    }

    public Map<ESError, Set<T>> getEsErrorMap() {
        return esErrorMap;
    }

    public Map<IndexError, Set<T>> getIndexErrorMap() {
        return indexErrorMap;
    }

    public static enum RequestError {
        unknown("请求出现未知错误"),// 未知错误
        requestError("请求失败"),// 无响应
        invalidData("请求数据为失效数据"),// 失效数据：请求参数为无效数据
        buildData("拼装数据错误");// 拼装数据错误

        private String name;

        RequestError(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum ESError {
        unknown("操作ES未知错误"),
        save("向ES中保存数据错误"),
        delete("从ES中删除数据错误"),
        retrieve("从ES中查询数据错误");

        private String name;

        ESError(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum IndexError {
        unknown("操作索引未知错误"),
        save("向索引中保存数据错误"),
        delete("从索引中删除数据错误");

        private String name;

        IndexError(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
