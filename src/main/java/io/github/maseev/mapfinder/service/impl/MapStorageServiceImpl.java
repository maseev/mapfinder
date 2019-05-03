package io.github.maseev.mapfinder.service.impl;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import io.github.maseev.mapfinder.model.GeoMap;
import io.github.maseev.mapfinder.model.Point;
import io.github.maseev.mapfinder.service.MapStorageService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MapStorageServiceImpl implements MapStorageService {

  private static final String MAP_NAME = "GEO-MAPS";
  private static final String EXECUTOR_NAME = "EXECUTOR";

  private final Map<Integer, List<Point>> maps;
  private final HazelcastInstance instance;

  private final int processingTimeout;

  private static class ContainsTask implements Callable<List<Integer>>, Serializable,
    HazelcastInstanceAware {

    private final double latitude;
    private final double longitude;
    private transient HazelcastInstance instance;

    ContainsTask(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance instance) {
      this.instance = instance;
    }

    @Override
    public List<Integer> call() throws Exception {
      IMap<Integer, List<Point>> map = instance.getMap(MAP_NAME);
      List<Integer> mapIds = new ArrayList<>();

      for (Integer mapId : map.localKeySet() ) {
        if (MapFinderUtil.contains(map.get(mapId), latitude, longitude)) {
          mapIds.add(mapId);
        }
      }

      return mapIds;
    }
  }

  @Autowired
  public MapStorageServiceImpl(@Value("${processing.timeout.min}") int processingTimeout) {
    this.processingTimeout = processingTimeout;

    Config config = new Config();
    MapConfig mapConfig = config.getMapConfig(MAP_NAME);
    mapConfig.setBackupCount(0).setAsyncBackupCount(0);

    instance = Hazelcast.newHazelcastInstance(config);
    maps = instance.getMap(MAP_NAME);
  }

  @Override
  public void add(GeoMap map) {
    maps.put(map.getId(), map.getPolygon());
  }

  @Override
  public List<Integer> contains(double latitude, double longitude) throws InterruptedException,
    ExecutionException, TimeoutException {
    IExecutorService executorService = instance.getExecutorService( EXECUTOR_NAME );
    Map<Member, Future<List<Integer>>> futures =
      executorService.submitToAllMembers(new ContainsTask(latitude, longitude));

    List<Integer> mapIds = new ArrayList<>();

    for (Future<List<Integer>> future : futures.values()) {
      mapIds.addAll(future.get(processingTimeout, TimeUnit.MINUTES));
    }

    return mapIds;
  }
}
