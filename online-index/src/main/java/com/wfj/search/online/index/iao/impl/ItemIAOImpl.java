package com.wfj.search.online.index.iao.impl;

import com.wfj.search.online.common.constant.CollectionNames;
import com.wfj.search.online.index.iao.IItemIAO;
import com.wfj.search.online.index.iao.IndexException;
import com.wfj.search.online.index.iao.QueryException;
import com.wfj.search.online.index.iao.impl.boostStrategy.BoostStrategy;
import com.wfj.search.online.index.pojo.ItemIndexPojo;
import com.wfj.search.online.index.service.IEdismaxConfigService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.common.params.SpellingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <br/>create at 15-7-21
 *
 * @author liufl
 * @since 1.0.0
 */
@Component("itemIAO")
public class ItemIAOImpl implements IItemIAO {
    private static final String COLLECTION_NAME = CollectionNames.ITEM_COLLECTION_NAME;
    @Autowired
    private SolrClient solrClient;
    @Qualifier("boostStrategy")
    @Autowired
    private BoostStrategy boostStrategy;
    @Autowired
    private IEdismaxConfigService edismaxConfigService;

    @Override
    public void saveItems(Collection<ItemIndexPojo> items) throws IndexException {
        List<SolrInputDocument> docs = items.stream().map(item -> this.boostStrategy.boost(this.solrClient, item))
                .collect(Collectors.toList());
        try {
            if (!docs.isEmpty()) {
                this.solrClient.add(COLLECTION_NAME, docs);
            }
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpired(long versionNo) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "-operationSid:[" + versionNo + " TO *]");
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeItem(String itemId) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "itemId:" + itemId);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeItemsOfSku(String skuId) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "skuId:" + skuId);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpiredOfBrand(String brandId, long version) throws IndexException {
        try {
            this.solrClient
                    .deleteByQuery(COLLECTION_NAME, "brandId:" + brandId + " AND -operationSid:[" + version + " TO *]");
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeItemsOfBrand(String brandId) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "brandId:" + brandId);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpiredOfCategory(String categoryId, String channel, long version) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME,
                    "allLevelCategoryIds_" + channel + ":" + categoryId + " AND -operationSid:[" + version + " TO *]");
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpiredOfSku(String skuId, long version) throws IndexException {
        try {
            this.solrClient
                    .deleteByQuery(COLLECTION_NAME, "skuId:" + skuId + " AND -operationSid:[" + version + " TO *]");
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeExpiredOfSpu(String spuId, long version) throws IndexException {
        try {
            this.solrClient
                    .deleteByQuery(COLLECTION_NAME, "spuId:" + spuId + " AND -operationSid:[" + version + " TO *]");
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void removeItemsOfSpu(String spuId) throws IndexException {
        try {
            this.solrClient.deleteByQuery(COLLECTION_NAME, "spuId:" + spuId);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public void rebuildSpell() throws IndexException {
        SolrQuery query = new SolrQuery();
        query.set("spellcheck", true);
        query.set(SpellingParams.SPELLCHECK_BUILD, true);
        try {
            this.solrClient.query(COLLECTION_NAME, query);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }

    @Override
    public List<FacetField.Count> facetChannels() throws QueryException {
        String field = "channels";
        SolrQuery myQuery = new SolrQuery("*:*").setRows(0).setFacet(true).addFacetField(field).setFacetMinCount(1)
                .setFacetLimit(Integer.MAX_VALUE);
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, myQuery);
        } catch (SolrServerException | IOException e) {
            throw new QueryException(e);
        }
        FacetField ff = response.getFacetField(field);
        return ff.getValues();
    }

    @Override
    public List<FacetField.Count> facetField(String channel, String field) throws QueryException {
        SolrQuery myQuery = new SolrQuery("*:*").addFilterQuery("channels:" + channel).addFilterQuery(
                "allLevelCategoryIds_" + channel + ":*").setRows(0).setFacet(true).addFacetField(field)
                .setFacetMinCount(1).setFacetLimit(
                        Integer.MAX_VALUE);
        myQuery.set(GroupParams.GROUP, true)
                .set(GroupParams.GROUP_FIELD, "spuId")
                .set(GroupParams.GROUP_TOTAL_COUNT, true)
                .set(GroupParams.GROUP_FACET, true)
                .set(GroupParams.GROUP_TRUNCATE, true);
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, myQuery);
        } catch (SolrServerException | IOException e) {
            throw new QueryException(e);
        }
        FacetField ff = response.getFacetField(field);
        return ff.getValues();
    }

    @Override
    public Long queryMatchCount(String keyword, String channel) throws QueryException {
        SolrQuery myQuery = new SolrQuery("searchAll:" + keyword)
                .setStart(0).setRows(0)
                .addFilterQuery("channels:" + channel)
                .addFilterQuery("allLevelCategoryIds_" + channel + ":*");
        myQuery.set(GroupParams.GROUP, true)
                .set(GroupParams.GROUP_FIELD, "spuId")
                .set(GroupParams.GROUP_TOTAL_COUNT, true)
                .set(GroupParams.GROUP_FACET, true)
                .set(GroupParams.GROUP_TRUNCATE, true);
        myQuery
                .set("defType", "edismax")
                .set(DisMaxParams.QF, this.edismaxConfigService.getQf())
                .set(DisMaxParams.MM, this.edismaxConfigService.getMm())
                .set(DisMaxParams.QS, this.edismaxConfigService.getQs())
                .set(DisMaxParams.TIE, this.edismaxConfigService.getTie())
                .set(DisMaxParams.BQ, this.edismaxConfigService.getBq());
        QueryResponse response;
        try {
            response = solrClient.query(COLLECTION_NAME, myQuery);
        } catch (SolrServerException | IOException e) {
            throw new QueryException(e);
        }
        return response.getGroupResponse().getValues().get(0).getNGroups().longValue();
    }

    @Override
    public void commit() throws IndexException {
        try {
            this.solrClient.commit(COLLECTION_NAME, false, true);
        } catch (SolrServerException | IOException e) {
            throw new IndexException(e);
        }
    }
}
