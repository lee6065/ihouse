package cn.itcast.stat.service;

import java.util.List;
import java.util.Map;

public interface StatService {
    List<Map> factoryCharts(String companyId);

    List<Map> sellCharts(String companyId);

    List<Map> onlineCharts(String companyId);
}
