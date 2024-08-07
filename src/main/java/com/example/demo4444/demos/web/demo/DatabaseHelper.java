package com.example.demo4444.demos.web.demo;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Service
public class DatabaseHelper {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseHelper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 插入数据
    public boolean insert(String tableName, String data) {
        System.out.println("Inserting data into table " + tableName + ": " + data);
        String[] keyValuePairs = data.split(",");
        System.out.println("Key-value pairs: " + keyValuePairs[0]+keyValuePairs[1]+keyValuePairs[2]);
        StringBuilder fieldKeys = new StringBuilder();
        StringBuilder fieldValues = new StringBuilder();
        List<Object> valueList = new ArrayList<>();

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");

            if (keyValue.length == 2) {
                fieldKeys.append(keyValue[0]).append(",");
                fieldValues.append("?,");
                valueList.add(keyValue[1]);
            }
        }
        System.out.println("Field keys: " + fieldKeys);

        fieldKeys.setLength(fieldKeys.length() - 1);
        fieldValues.setLength(fieldValues.length() - 1);

        String sql = "INSERT INTO " + tableName + " (" + fieldKeys + ") VALUES (" + fieldValues + ")";
        System.out.println("Constructed SQL: " + sql);

        return jdbcTemplate.update(sql, valueList.toArray()) > 0;
    }

    // 更新数据
    public boolean update(String tableName, String data, String condition) {
        System.out.println("Updating data in table " + tableName + ": " + data+" where "+condition);
        String replacedString = condition.replace(":", "=");
        String[] keyValuePairs = data.split(",");
        StringBuilder setClause = new StringBuilder();
        List<Object> valueList = new ArrayList<>();

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                setClause.append(keyValue[0]).append("=?,");
                valueList.add(keyValue[1]);
            }
        }

        setClause.setLength(setClause.length() - 1);

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + replacedString;
        System.out.println("Constructed SQL: " + sql);

        return jdbcTemplate.update(sql, valueList.toArray()) > 0;
    }

    // 删除数据
    public boolean delete(String tableName, String condition) {
        String replacedString = condition.replace(":", "=");

        String sql = "DELETE FROM " + tableName + " WHERE " + replacedString;
        System.out.println("Constructed SQL: " + sql);

        return jdbcTemplate.update(sql) > 0;
    }

    // 查询数据
    public List<Map<String, Object>> query(String tableName, String condition) {
        String replacedString = condition.replace(":", "=");
        String sql = "SELECT * FROM " + tableName + " WHERE " + replacedString;
        System.out.println("Constructed SQL: " + sql);

        return jdbcTemplate.queryForList(sql);
    }

    public String cout(String e){
        System.out.println(e);
        return e;
    }

    public boolean login(String username, String password) {
        String tableName = "medicalstaff";
        String condition = "MID:" + username + " AND password:" + password;

        String replacedString = condition.replace(":", "=");
        String sql = "SELECT * FROM " + tableName + " WHERE " + replacedString;
        System.out.println("Constructed SQL: " + sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        System.out.println("Result: " + !result.isEmpty());
        return !result.isEmpty();
    }

    public boolean uploadMac(String username, String mac) {
        System.out.println("updateMAC");
        String tableName = "nurse";
        String replacedMAC = mac.replace(":", "*");
        return update(tableName, "mac:" + replacedMAC, "MID:" + username);
    }

    public String askMAC(String username) {
        String tableName = "nurse";
        String condition = "MID:" + username;
        String replacedString = condition.replace(":", "=");
        String sql = "SELECT * FROM " + tableName + " WHERE " + replacedString;
        System.out.println("Constructed SQL: " + sql);
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public class DeviceRecord {
        String BedName;
        double RSSI;
        Timer timer;

        DeviceRecord(String BedName, double RSSI) {
            this.BedName = BedName;
            this.RSSI = RSSI;
            this.timer = new Timer();
            resetTimer();
        }

        synchronized void resetTimer() {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    RSSI = -200;
                    System.out.println("kill!!!!\ndeviceRecord1.RSSI:" + deviceRecord1.RSSI + " deviceRecord2.RSSI:" + deviceRecord2.RSSI);
                }
            }, 4000);
        }
    }

    DeviceRecord deviceRecord1 = new DeviceRecord("Bed1", -200);
    DeviceRecord deviceRecord2 = new DeviceRecord("Bed2", -200);

    public synchronized void uploadRSSI(String BedName, double RSSI) {
        System.out.println("BedName:" + BedName + " RSSI:" + RSSI);
        if (BedName.equals(deviceRecord1.BedName)) {
            deviceRecord1.RSSI = RSSI;
            deviceRecord1.resetTimer();
        } else if (BedName.equals(deviceRecord2.BedName)) {
            deviceRecord2.RSSI = RSSI;
            deviceRecord2.resetTimer();
        }
        System.out.println("deviceRecord1.RSSI:" + deviceRecord1.RSSI + " deviceRecord2.RSSI:" + deviceRecord2.RSSI);
    }

    public boolean askActive(String BedName) {
        System.out.println("askActive" + BedName);
        System.out.println("deviceRecord1.RSSI:" + deviceRecord1.RSSI + " deviceRecord2.RSSI:" + deviceRecord2.RSSI);

        if (BedName.equals(deviceRecord1.BedName)) {
            if (deviceRecord1.RSSI == -200) {
                return false;
            }
            return deviceRecord1.RSSI > deviceRecord2.RSSI;
        } else if (BedName.equals(deviceRecord2.BedName)) {
            if (deviceRecord2.RSSI == -200) {
                return false;
            }
            return deviceRecord2.RSSI > deviceRecord1.RSSI;
        }
        return false;
    }
}
