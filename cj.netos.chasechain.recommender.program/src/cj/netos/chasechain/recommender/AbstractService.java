package cj.netos.chasechain.recommender;

import cj.lns.chip.sos.cube.framework.CubeConfig;
import cj.lns.chip.sos.cube.framework.ICube;
import cj.lns.chip.sos.disk.INetDisk;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import org.bson.Document;

public class AbstractService {
    private static transient final String _COL_NAME_dashboard = "traffic.dashboard.pointers";
    @CjServiceRef(refByName = "mongodb.netos")
    INetDisk disk;

    protected ICube cubePool(String pool) throws CircuitException {
        String col = String.format("%s.%s", TrafficPool._COL_NAME, pool);
        if (!disk.existsCube(col)) {
            CubeConfig cubeConfig = new CubeConfig();
            cubeConfig.alias(col);
            ICube cube = disk.createCube(col, cubeConfig);
            cube.createIndex(_COL_NAME_dashboard, Document.parse(String.format("{'tuple.lastCacheTime':-1}")));
            return cube;
        }
        return disk.cube(col);
    }

    protected ICube cubePerson(String person) throws CircuitException {
        String cubeName = person;
        if (!disk.existsCube(cubeName)) {
            CubeConfig cubeConfig = new CubeConfig();
            cubeConfig.alias(cubeName);
            ICube cube = disk.createCube(cubeName, cubeConfig);
            cube.createIndex(RecommendedItem._COL_NAME, Document.parse(String.format("{'tuple.item':-1}")));
            return cube;
        }
        return disk.cube(cubeName);
    }

    protected ICube home() throws CircuitException {
        return disk.home();
    }
}
