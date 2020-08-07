package cj.netos.chasechain.recommender;

import cj.studio.ecm.net.CircuitException;

import java.util.List;
import java.util.Set;

public interface IContentItemService {

    List<ContentItem> listContentItem(String trafficPool, List<String> itemIds) throws CircuitException;

    long countContentItemsOfBox(String pool, String box) throws CircuitException;

    long countContentItemsOfPool(String pool) throws CircuitException;

    ContentItem getContentItem(String pool, String item) throws CircuitException;

    List<ContentItem> pageContentItemOfBox(String pool, String box, int limit, long offset) throws CircuitException;

    List<ContentItem> pageContentItem(String pool, int limit, long offset) throws CircuitException;

    List<ContentItem> pageContentItemOfProvider(String pool, String provider, int limit, long offset) throws CircuitException;

}
