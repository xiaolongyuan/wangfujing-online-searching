package com.wfj.search.online.web.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wfj.search.online.common.RuntimeTrackingException;
import com.wfj.search.online.common.TrackingException;
import com.wfj.search.online.web.common.pojo.CategoryDisplayPojo;
import com.wfj.search.online.web.common.pojo.SearchParams;
import com.wfj.search.online.web.common.pojo.SearchResult;
import com.wfj.search.online.web.common.pojo.SpuDisplayPojo;
import com.wfj.search.online.web.handler.AbstractUrlHandler;
import com.wfj.search.online.web.param.SortParamRestorer;
import com.wfj.search.online.web.pojo.PagedResult;
import com.wfj.search.online.web.query.BaseMainQueryDecorator;
import com.wfj.search.online.web.searchTask.*;
import com.wfj.search.online.web.service.ISearchConfigService;
import com.wfj.search.online.web.service.ISearchService;
import com.wfj.search.utils.concurrent.BatchRunnables;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.wfj.search.online.common.constant.CacheableAware.VALUE_KEY_SEARCH_WEB_DO_SEARCH;

/**
 * <p>create at 15-11-20</p>
 *
 * @author liufl
 * @author liuxh
 * @since 1.0.0
 */
@Component("searchService")
public class SearchServiceImpl implements ISearchService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final DecimalFormat NO_FORMAT = new DecimalFormat("000000000000");
    private final AtomicLong searchRequestNo = new AtomicLong();
    private final List<SearchTask> searchTasks = Collections.synchronizedList(Lists.newArrayList());
    private final List<SearchTask> listTasks = Collections.synchronizedList(Lists.newArrayList());
    private final List<SearchTask> brandListTasks = Collections.synchronizedList(Lists.newArrayList());
    private final List<SearchTask> newProductsListTasks = Collections.synchronizedList(Lists.newArrayList());
    private final List<SearchTask> gpTasks = Collections.synchronizedList(Lists.newArrayList());
    private final List<SearchTask> listAllCategoriesTasks = Collections.synchronizedList(Lists.newArrayList());
    private final List<SearchTask> listAvailableBrandsTasks = Collections.synchronizedList(Lists.newArrayList());
    @Autowired
    private SortParamRestorer sortParamRestorer;
    @Autowired
    private ISearchConfigService searchConfigService;
    @Autowired
    private BaseMainQueryDecorator baseMainQueryDecorator;
    @Autowired
    private JustQFacetCategoryTreeSearchTask justQFacetCategoryTreeSearchTask;
    @Autowired
    private FacetCategoryTreeSearchTask facetCategoryTreeSearchTask;
    @Autowired
    private BrandFacetCategoryTreeSearchTask brandFacetCategoryTreeSearchTask;
    @Autowired
    private RootCategoryFacetCategoryTreeSearchTask rootCategoryFacetCategoryTreeSearchTask;
    @Autowired
    private NewProductsFacetCategoryTreeSearchTask newProductsFacetCategoryTreeSearchTask;
    @Autowired
    private SpellCheckSearchTask spellCheckSearchTask;
    @Autowired
    private FacetPriceRangeSearchTask facetPriceRangeSearchTask;
    @Autowired
    private FacetBrandSearchTask facetBrandSearchTask;
    @Autowired
    private FacetStandardSearchTask facetStandardSearchTask;
    @Autowired
    private FacetColorSearchTask facetColorSearchTask;
    @Autowired
    private FacetAttrsSearchTask facetAttrsSearchTask;
    @Autowired
    private FacetTagSearchTask facetTagSearchTask;
    @Autowired
    private MainQuerySearchTask mainQuerySearchTask;
    @Autowired
    private ListMainQuerySearchTask listMainQuerySearchTask;
    @Autowired
    private NewProductsAlwaysFacetBrandSearchTask newProductsAlwaysFacetBrandSearchTask;
    @Autowired
    private AlwaysFacetBrandSearchTask alwaysFacetBrandSearchTask;
    @Autowired
    private BrandAlwaysFacetPriceRangeSearchTask brandAlwaysFacetPriceRangeSearchTask;
    @Autowired
    private NewProductsAlwaysFacetPriceRangeSearchTask newProductsAlwaysFacetPriceRangeSearchTask;
    @Autowired
    private AlwaysFacetPriceRangeSearchTask alwaysFacetPriceRangeSearchTask;
    @Autowired
    private BrandAlwaysFacetStandardSearchTask brandAlwaysFacetStandardSearchTask;
    @Autowired
    private NewProductsAlwaysFacetStandardSearchTask newProductsAlwaysFacetStandardSearchTask;
    @Autowired
    private AlwaysFacetStandardSearchTask alwaysFacetStandardSearchTask;
    @Autowired
    private BrandAlwaysFacetColorSearchTask brandAlwaysFacetColorSearchTask;
    @Autowired
    private NewProductsAlwaysFacetColorSearchTask newProductsAlwaysFacetColorSearchTask;
    @Autowired
    private AlwaysFacetColorSearchTask alwaysFacetColorSearchTask;
    @Autowired
    private BrandAlwaysFacetAttrsSearchTask brandAlwaysFacetAttrsSearchTask;
    @Autowired
    private NewProductsAlwaysFacetAttrsSearchTask newProductsAlwaysFacetAttrsSearchTask;
    @Autowired
    private AlwaysFacetAttrsSearchTask alwaysFacetAttrsSearchTask;
    @Autowired
    private BrandAlwaysFacetTagSearchTask brandAlwaysFacetTagSearchTask;
    @Autowired
    private NewProductsAlwaysFacetTagSearchTask newProductsAlwaysFacetTagSearchTask;
    @Autowired
    private AlwaysFacetTagSearchTask alwaysFacetTagSearchTask;
    @Autowired
    private BrandListMainQuerySearchTask brandListMainQuerySearchTask;
    @Autowired
    private AvailableBrandsSearchTask availableBrandsSearchTask;
    @Autowired
    private SortsSearchTask sortsSearchTask;
    @Autowired
    private AbstractUrlHandler urlHandler;

    @PostConstruct
    public void doInit() {
        // search tasks
        this.searchTasks.add(this.justQFacetCategoryTreeSearchTask);
        this.searchTasks.add(this.facetCategoryTreeSearchTask);
        this.searchTasks.add(this.spellCheckSearchTask);
        this.searchTasks.add(this.facetPriceRangeSearchTask);
        this.searchTasks.add(this.facetBrandSearchTask);
        this.searchTasks.add(this.facetStandardSearchTask);
        this.searchTasks.add(this.facetColorSearchTask);
        this.searchTasks.add(this.facetAttrsSearchTask);
        this.searchTasks.add(this.facetTagSearchTask);
        this.searchTasks.add(this.mainQuerySearchTask);
        this.searchTasks.add(this.sortsSearchTask);
        // list tasks
        this.listTasks.add(this.rootCategoryFacetCategoryTreeSearchTask);
        this.listTasks.add(this.facetCategoryTreeSearchTask);
        this.listTasks.add(this.facetPriceRangeSearchTask);
        this.listTasks.add(this.facetBrandSearchTask);
        this.listTasks.add(this.facetStandardSearchTask);
        this.listTasks.add(this.facetColorSearchTask);
        this.listTasks.add(this.facetAttrsSearchTask);
        this.listTasks.add(this.facetTagSearchTask);
        this.listTasks.add(this.listMainQuerySearchTask);
        this.listTasks.add(this.sortsSearchTask);
        // brandList tasks
        this.brandListTasks.add(this.brandFacetCategoryTreeSearchTask);
        this.brandListTasks.add(this.facetCategoryTreeSearchTask);
        this.brandListTasks.add(this.brandAlwaysFacetPriceRangeSearchTask);
        this.brandListTasks.add(this.brandAlwaysFacetStandardSearchTask);
        this.brandListTasks.add(this.brandAlwaysFacetColorSearchTask);
        this.brandListTasks.add(this.brandAlwaysFacetAttrsSearchTask);
        this.brandListTasks.add(this.brandAlwaysFacetTagSearchTask);
        this.brandListTasks.add(this.brandListMainQuerySearchTask);
        this.brandListTasks.add(this.sortsSearchTask);
        // new-products tasks
        this.newProductsListTasks.add(this.newProductsFacetCategoryTreeSearchTask);
        this.newProductsListTasks.add(this.facetCategoryTreeSearchTask);
        this.newProductsListTasks.add(this.newProductsAlwaysFacetBrandSearchTask);
        this.newProductsListTasks.add(this.newProductsAlwaysFacetPriceRangeSearchTask);
        this.newProductsListTasks.add(this.newProductsAlwaysFacetStandardSearchTask);
        this.newProductsListTasks.add(this.newProductsAlwaysFacetColorSearchTask);
        this.newProductsListTasks.add(this.newProductsAlwaysFacetAttrsSearchTask);
        this.newProductsListTasks.add(this.newProductsAlwaysFacetTagSearchTask);
        this.newProductsListTasks.add(this.mainQuerySearchTask);
        this.newProductsListTasks.add(this.sortsSearchTask);
        // gp tasks
        this.gpTasks.add(this.justQFacetCategoryTreeSearchTask);
        this.gpTasks.add(this.alwaysFacetBrandSearchTask);
        this.gpTasks.add(this.alwaysFacetPriceRangeSearchTask);
        this.gpTasks.add(this.alwaysFacetStandardSearchTask);
        this.gpTasks.add(this.alwaysFacetColorSearchTask);
        this.gpTasks.add(this.alwaysFacetAttrsSearchTask);
        this.gpTasks.add(this.alwaysFacetTagSearchTask);
        this.gpTasks.add(this.mainQuerySearchTask);
        this.gpTasks.add(this.sortsSearchTask);
        // list all categories tasks
        this.listAllCategoriesTasks.add(this.justQFacetCategoryTreeSearchTask);
        // list available brands tasks
        this.listAvailableBrandsTasks.add(this.availableBrandsSearchTask);
    }

    private SearchResult searchTemplate(SearchParams searchParams, String urlPrefix,
            TaskListProvider taskListProvider) throws TrackingException {
        SolrQuery baseMainQuery = new SolrQuery();
        this.baseMainQueryDecorator.decorator(baseMainQuery, searchParams);
        final SearchResult searchResult = new SearchResult(searchParams);
        final Thread masterThread = Thread.currentThread();
        final AtomicReference<Throwable> atomicException = new AtomicReference<>();
        //noinspection Duplicates
        ExecutorService pool = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "SearchThreads-" + NO_FORMAT.format(searchRequestNo.addAndGet(1L)));
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((t, e) -> {
                atomicException.set(e);
                masterThread.interrupt();
            });
            return thread;
        });
        BatchRunnables batchRunnables = new BatchRunnables(pool);
        taskListProvider.getTaskList()
                .forEach(task -> batchRunnables.addRunnable(() -> task.doSearch(searchResult, baseMainQuery)));
        try {
            batchRunnables.execute();
        } catch (InterruptedException e) {
            Throwable throwable = atomicException.get();
            if (throwable instanceof RuntimeTrackingException) {
                throw ((RuntimeTrackingException) throwable).getTrackingException();
            }
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable.getMessage(), throwable);
            }
        } finally {
            pool.shutdown();
        }
        try {
            searchResult.getSuccessList().forEach(spu -> {
                Set<String> categoryIds = Sets.newConcurrentHashSet(spu.getCategoryIds());
                spu.setCategoryIds(null);
                searchResult.getFilters().getQueryMatchedCategoryTree() // 1st level
                        .stream()
                        .filter(cat1 -> categoryIds.contains(cat1.getId()))
                        .forEach(cat1 -> {
                            CategoryDisplayPojo _cat1 = new CategoryDisplayPojo();
                            _cat1.setId(cat1.getId());
                            cat1.getChildren() // 2nd level
                                    .stream()
                                    .filter(cat2 -> categoryIds.contains(cat2.getId()))
                                    .forEach(cat2 -> {
                                        CategoryDisplayPojo _cat2 = new CategoryDisplayPojo();
                                        _cat2.setId(cat2.getId());
                                        _cat2.setParent(_cat1);
                                        cat2.getChildren() // 3rd level
                                                .stream()
                                                .filter(cat3 -> categoryIds.contains(cat3.getId()))
                                                .forEach(cat3 -> {
                                                    CategoryDisplayPojo _cat3 = new CategoryDisplayPojo();
                                                    _cat3.setId(cat3.getId());
                                                    _cat3.setParent(_cat2);
                                                    _cat3.setDisplay(cat3.getDisplay());
                                                    spu.getLeafCategories().add(_cat3);
                                                });
                                    });
                        });
            });
        } catch (Exception e) {
            logger.error("填充SPU三级分类信息出错, 0x530011", e);
            throw new TrackingException(e, "0x530011");
        }
        urlHandler.handle(urlPrefix, searchResult, searchParams);
        return searchResult;
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_WEB_DO_SEARCH, unless = "#result.successList.size() == 0")
    public SearchResult doSearch(SearchParams searchParams, String urlPrefix) throws TrackingException {
        return this.searchTemplate(searchParams, urlPrefix + '/' + searchParams.getInputQuery(),
                () -> SearchServiceImpl.this.searchTasks);
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_WEB_DO_SEARCH, unless = "#result.successList.size() == 0")
    public SearchResult doList(SearchParams searchParams, String urlPrefix) throws TrackingException {
        return this.searchTemplate(searchParams, urlPrefix, () -> SearchServiceImpl.this.listTasks);
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_WEB_DO_SEARCH, unless = "#result.successList.size() == 0")
    public SearchResult doBrandList(SearchParams searchParams, String urlPrefix) throws TrackingException {
        return this.searchTemplate(searchParams, urlPrefix, () -> SearchServiceImpl.this.brandListTasks);
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_WEB_DO_SEARCH, unless = "#result.successList.size() == 0")
    public SearchResult doNewProductsList(SearchParams searchParams) throws TrackingException {
        return this.searchTemplate(searchParams, "/new-products", () -> SearchServiceImpl.this.newProductsListTasks);
    }

    @Override
    @Cacheable(value = VALUE_KEY_SEARCH_WEB_DO_SEARCH, unless = "#result.successList.size() == 0")
    public SearchResult doGpSearch(SearchParams searchParams) throws TrackingException {
        return this.searchTemplate(searchParams, "/gpDisplay", () -> SearchServiceImpl.this.gpTasks);
    }

    @Override
    public PagedResult<SpuDisplayPojo> simpleSearch(String value, int fetch, String channel) throws TrackingException {
        SearchParams params = new SearchParams();
        params.setChannel(StringUtils.isBlank(channel) ? searchConfigService.getChannel() : channel);
        params.setQ(value);
        try {
            this.sortParamRestorer.restore(params, "0");
        } catch (Exception e) {
            logger.error("恢复排序条件失败", e);
            throw new TrackingException(e, "0x530009");
        }
        SolrQuery query = new SolrQuery(value);
        this.baseMainQueryDecorator.decorator(query, params);
        query.setRows(fetch);
        SearchResult searchResult = new SearchResult(params);
        try {
            this.mainQuerySearchTask.doSearch(searchResult, query);
        } catch (Exception e) {
            logger.error("执行检索失败", e);
            throw new TrackingException(e, "0x530010");
        }
        PagedResult<SpuDisplayPojo> pagedResult = new PagedResult<>();
        pagedResult.setFetch(fetch);
        pagedResult.setTotal(searchResult.getPagination().getTotalResults());
        pagedResult.setList(searchResult.getSuccessList());
        return pagedResult;
    }

    @Override
    public SearchResult doListAllCategories(SearchParams params) throws TrackingException {
        return this.searchTemplate(params, "", () -> SearchServiceImpl.this.listAllCategoriesTasks);
    }

    @Override
    public SearchResult doListAvailableBrands(SearchParams params) throws TrackingException {
        return this.searchTemplate(params, "", () -> SearchServiceImpl.this.listAvailableBrandsTasks);
    }

    private interface TaskListProvider {
        List<SearchTask> getTaskList();
    }
}
