package com.pixshow.custom.grid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;

@Service
public class GridDataService extends BaseService {
    @Autowired
    private GridDataDao gridDataDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getData(String code, String order) {
        return gridDataDao.getData(code, order);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insert(String table, List<String> names, List<Object> values) {
        StringBuilder sql = new StringBuilder("(");
        StringBuilder tt = new StringBuilder(" values (");
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) {
                sql.append(",");
                tt.append(",");
            }
            sql.append(names.get(i));
            tt.append("?");
        }
        sql.append(")").append(tt.append(")"));
        gridDataDao.insert(table, sql.toString(), values.toArray());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String table, int id) {
        gridDataDao.delete(table, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(String table, String update, int id) {
        gridDataDao.update(table, update, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> gridData(String grid) {
        return gridDataDao.gridData(grid);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> customGridData(String grid, String filters) {
        return gridDataDao.customGridData(grid, filters);
    }

}
