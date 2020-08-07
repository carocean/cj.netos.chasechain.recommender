package cj.netos.chasechain.recommender;

import cj.studio.ecm.net.CircuitException;

import java.util.List;
import java.util.Set;

public interface IRecommendedItemService {
    List<RecommendedItem> listRecommended(String person, Set<String> itemIds) throws CircuitException;

    void addRecommendItems(String person, Set<String> itemIds) throws CircuitException;
}
