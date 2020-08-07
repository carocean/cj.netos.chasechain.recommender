package cj.netos.chasechain.recommender;

import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface IRecommenderService {
    List<ContentItem> pullItem(String person, String towncode) throws CircuitException;

    void configGlobal(RecommenderConfig config) throws CircuitException;

    RecommenderConfig getGlobalConfig() throws CircuitException;

    RecommenderConfig getPersonConfig(String principal) throws CircuitException;

    void configPerson(String principal, RecommenderConfig config) throws CircuitException;

}
