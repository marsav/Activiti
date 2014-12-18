package org.activiti.engine;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by martynas on 14/10/14.
 */
public interface GeraCategoryService {

    public static final String ROOT_KEY = "0";

    public static class Category {
        public String key;
        public String title;
        public String parentKey;

        public Category(String key, String title, String parentKey) {
            this.key=key;
            this.title=title;
            this.parentKey=parentKey;
        }

        public boolean isTopRoot() {
            return (parentKey==null || ROOT_KEY.equals(parentKey.toLowerCase()));
        }
    }


    public Map<String, Category> getMap();

    public Category get(String key);
    public void put(String key, Category cat);
    public String getTitle(String key);

}
