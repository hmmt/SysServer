package com.bluescreen.server.api;

import com.bluescreen.server.*;

import java.sql.*;
import java.io.*;

import org.json.JSONObject;

public class APISaveSensing implements APIBase {


        @Override
        public JSONObject process(JSONObject request) {

                String id = request.getString("id");
                String data = request.getString("data");

                String[] splited = data.split(" ");

                JSONObject resp = new JSONObject();
                resp.put("msg", "Hello, "+id);
                try
                {
                        SensorDataManager.insertSensingData(
                                Integer.parseInt(id),
                                Integer.parseInt(splited[0]),
                                Integer.parseInt(splited[1]),
                                Integer.parseInt(splited[2]));
                }
                catch(SQLException e)
                {
                        resp.put("error", e.getMessage());
                }
                catch(IOException e)
                {
                        resp.put("IOerror", e.getMessage());
                }
                return resp;
        }

}