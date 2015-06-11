package com.bluescreen.server.api;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONObject;

import com.bluescreen.server.SensingData;
import com.bluescreen.server.SensorDataManager;
import com.bluescreen.server.SysSession;

public class APIGetSensing implements APIBase {

        @Override
        public JSONObject process(JSONObject request) {

                String id = request.getString("id");


                JSONObject resp = new JSONObject();
                try
                {
                        SensingData data = SensorDataManager.findLastSensingData(
                                Integer.parseInt(id));

                        if(data != null)
                        {
                                resp.put("state", "ok");
                                resp.put("temp", data.temp);
                                resp.put("illumination", data.illumination);
                                resp.put("humidity", data.humidity);
                        }
                        else
                        {
                                resp.put("state", "empty");
                        }
                }
                catch(SQLException e)
                {
                        resp = new JSONObject();
                        resp.put("error", e.getMessage());
                }
                catch(IOException e)
                {
                        resp = new JSONObject();
                        resp.put("IOerror", e.getMessage());
                }
                catch(Exception e)
                {
                        resp = new JSONObject();
                        resp.put("error", e.getMessage());
                }
                return resp;
        }

}
