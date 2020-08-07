package cj.netos.chasechain.recommender;

import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface IItemBehaviorService {
    void update(String trafficPool, String item, long likes, long comments, long recommends) throws CircuitException;

    void updateLikes(String trafficPool, String item, long likes) throws CircuitException;

    void updateComments(String trafficPool, String item, long comments) throws CircuitException;

    void updateRecommends(String trafficPool, String item, long recommends) throws CircuitException;

    void addBehave(String trafficPool, String item, String person, String behave, String attachment) throws CircuitException;

    void removeBehave(String trafficPool, String item, String person, String behave) throws CircuitException;

    List<BehaviorDetails> pageBehave(String trafficPool, String item, String behave, int limit, long offset) throws CircuitException;

    List<ItemBehavior> pageBehavior(String trafficPool, int limit, long offset) throws CircuitException;

    ItemBehavior getItemBehavior(String trafficPool, String item) throws CircuitException;

    ItemBehavior getItemInnateBehavior(String pool, String item) throws CircuitException;

    long hasBehave(String principal, String pool, String item, String behave) throws CircuitException;

    void addRecommendBehaveToItems(String person, List<ContentItem> items) throws CircuitException;

}
