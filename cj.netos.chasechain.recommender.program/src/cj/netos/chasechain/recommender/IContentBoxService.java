package cj.netos.chasechain.recommender;

import cj.studio.ecm.net.CircuitException;

import java.util.List;

public interface IContentBoxService {
    ContentBox getContentBox(String pool, String box) throws CircuitException;

    List<ContentBox> pageContentBox(String pool, int limit, long offset) throws CircuitException;

    List<ContentBox> pageContentBoxOfProvider(String pool, String provider, int limit, long offset) throws CircuitException;

    List<ContentBoxAssigner> pageContentBoxByAssigner(String provider, int limit, long offset) throws CircuitException;

}
