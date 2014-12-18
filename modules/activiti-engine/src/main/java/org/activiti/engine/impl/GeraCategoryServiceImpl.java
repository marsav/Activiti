package org.activiti.engine.impl;

import org.activiti.engine.GeraCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by martynas on 27/11/14.
 */
public class GeraCategoryServiceImpl implements GeraCategoryService {
    private static Logger log = LoggerFactory.getLogger(GeraCategoryServiceImpl.class);

    private final static String TABLE = "GERA_PROC_STRUCT";
        Connection con;
        PreparedStatement selectStatement = null;
        String selectSQL = "SELECT * FROM "+TABLE+" ORDER BY inserted DESC";

        PreparedStatement insertStatement = null;
        String insertSQL =  "INSERT INTO " + TABLE + "(key, parentKey, title, inserted) VALUES(?, ?, ?, ?)";


    long lastRead = 0L;
        Calendar cal = Calendar.getInstance();

        public void setDataSource(DataSource dataSource) {
            try {
                con = dataSource.getConnection();
                con.setAutoCommit(true);

            } catch (SQLException e) {
                e.printStackTrace();
                log.error("connection error", e);
            }
        }

        public static final String ROOT_KEY = "0";

        private static LinkedHashMap<String, Category> CATEGORIES = null;

        public Map<String, Category> getMap() {
            cal = Calendar.getInstance();
            if (CATEGORIES==null || (cal.getTimeInMillis() - lastRead)>3000 ) {
                // expired, re-read categories
                loadLatestCategories();
                lastRead = cal.getTimeInMillis();
            }


            if (CATEGORIES==null || CATEGORIES.isEmpty()) {

//                put("pagalbiniai", new Category("pagalbiniai", "Pagalbiniai procesai", "gera"));
//                put("bendrieji-sprendimai", new Category("bendrieji-sprendimai", "Bendrieji sprendimų procesai", "gera"));
//                put("nuosavybes", new Category("nuosavybes", "Nuosavybės sprendimų procesai", "gera"));
//                put("patikejimai", new Category("patikejimai", "Patikėjimų sprendimų procesai", "gera"));
//                put("isnuomojimas", new Category("isnuomojimas", "Naudojimo sprendimų prcesai", "gera"));
//                put("duomenys", new Category("duomenys", "Duomenų pateikimo procesai", "gera"));
//                put("sutartys", new Category("sutartys", "Sutarčių procesai", "gera"));
//
//                put("pakrovimas", new Category("pakrovimas", "Duomenų pakrovimo procesai", "gera"));
                loadLatestCategories();
            }

            if (CATEGORIES==null) CATEGORIES = new LinkedHashMap<String, Category>();
            return CATEGORIES;
        }

    private void loadLatestCategories() {
        // execute select SQL stetement
        ResultSet rs = null;
        try {
            if (selectStatement==null) {
                selectStatement = con.prepareStatement(selectSQL);
            }
            rs = selectStatement.executeQuery();
            if (CATEGORIES==null) {
                CATEGORIES = new LinkedHashMap<String, Category>();
                CATEGORIES.put("root", new Category("root", "ROOT", null));
            }
            while (rs.next()) {

                String key = rs.getString("key");
                String parentKey = rs.getString("parentKey");
                String title = rs.getString("title");

//                log.info("key : " + key);
//                log.info("parentKey : " + parentKey);
//                log.info("title : " + title);
                if (!CATEGORIES.containsKey(key)) {
                    CATEGORIES.put(key, new Category(key, title, parentKey));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("sql error", e);
        }
        finally {
            if (selectStatement!=null) {
                try {
                    rs.close();
                    selectStatement.close();
                    selectStatement = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  Category get(String key) {
            return getMap().get(key);
        }

    public void put(String key, Category cat) {
        // store in db, but
        // first remove if any
        Statement stmt = null;
        String queryDelete = "DELETE FROM " + TABLE + " WHERE key LIKE '" + key+"'";

//        String queryInsert = "INSERT INTO " + TABLE + "(key, parentKey, title, inserted) VALUES('"+cat.key+"', '"+cat.parentKey+"', '"+cat.title+"', "+cal.getTimeInMillis()+")";
        try {
            if (insertStatement==null) {
                insertStatement = con.prepareStatement(insertSQL);
            }
            insertStatement.setString(1, cat.key);
            insertStatement.setString(2, cat.parentKey);
            insertStatement.setString(3, cat.title);
            insertStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            stmt = con.createStatement();
            log.debug(queryDelete);
            stmt.executeUpdate(queryDelete);

            log.debug(insertStatement.toString());
            insertStatement.executeQuery();
//            stmt.executeUpdate(queryInsert);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("conncection error", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insertStatement != null) {
                try {
                    insertStatement.close();
                    insertStatement=null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getTitle(String key) {
            return get(key).title;
        }

}
