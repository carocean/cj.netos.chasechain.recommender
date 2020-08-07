package cj.netos.chasechain.recommender;

import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface ITrafficPoolService {
    TrafficPool getTownTrafficPool(String towncode);

    TrafficPool getPool(String parent);

    List<TrafficPool>  getHierarchyGeospherePools(String towncode);

    List<TrafficPool> getRecommendPools(String towncode);

    TrafficPool getCountryPool();

    List<TrafficPool> pageChildrenPool(String pool, int limit, long offset);

    long countContentProvidersOfPool(String pool) throws CircuitException;

    List<String> pageContentProvider(String pool, int limit, long offset) throws CircuitException;

    TrafficDashboardPointer getTrafficDashboard(String pool) throws CircuitException;

}
