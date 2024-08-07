package com.example.demo4444.demos.web.demo;

import com.example.demo4444.demos.web.demo.DatabaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DatabaseController {

    private final DatabaseHelper databaseHelper;

    @Autowired
    public DatabaseController(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @GetMapping("/insert")
    public boolean insert(@RequestParam String tableName, @RequestParam String data) {
        return databaseHelper.insert(tableName, data);
    }

    @GetMapping("/update")
    public boolean update(@RequestParam String tableName, @RequestParam String data, @RequestParam String condition) {
        return databaseHelper.update(tableName, data, condition);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam String tableName, @RequestParam String condition) {
        return databaseHelper.delete(tableName, condition);
    }

    @GetMapping("/query")
    public List<Map<String, Object>> query(@RequestParam String tableName, @RequestParam String condition) {
        return databaseHelper.query(tableName, condition);
    }

    @GetMapping("/login")
    public boolean login(@RequestParam String username, @RequestParam String password) {
        return databaseHelper.login(username, password);
    }

    @GetMapping("/uploadMac")
    public boolean uploadMac(@RequestParam String username, @RequestParam String mac) {
        return databaseHelper.uploadMac(username, mac);
    }

    @GetMapping("/askMac")
    public String askMAC(@RequestParam String username) {
        return databaseHelper.askMAC(username);
    }

    @GetMapping("/uploadRSSI")
    public void uploadRSSI(@RequestParam String bedName, @RequestParam double RSSI) {
        databaseHelper.uploadRSSI(bedName, RSSI);
    }

    @GetMapping("/askActive")
    public boolean askActive(@RequestParam String bedName) {
        return databaseHelper.askActive(bedName);
    }
}
