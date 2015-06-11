package com.bluescreen.server;

import java.sql.*;
import java.util.ArrayList;
import java.io.*;

public class SensorDataManager {

        public static void insertSensingData(int user_id, int illumination, int temp, int humidity)
                 throws SQLException, IOException
        {
                Connection con = getConnection();
                try {
                        Statement st = con.createStatement();

                        st.executeUpdate("INSERT INTO SensingData(user_id, illumination, temp, humidity, created_at) "
                                        + "VALUES("+user_id+","
                                        + illumination +","
                                        + temp +","
                                        + humidity+","
                                        + "NOW())");
                }
                finally {
                        con.close();
                }

        }

        public static SensingData findLastSensingData(int user_id)
                        throws SQLException, IOException
        {
                Connection con = getConnection();
                try {
                        Statement st = con.createStatement();

                        ResultSet rs = st.executeQuery("SELECT illumination, temp, humidity FROM SensingData"
                                        + " WHERE user_id="+user_id+" ORDER BY id DESC LIMIT 1");


                        if(rs.next())
                        {
                                SensingData data = new SensingData();
                                data.illumination = rs.getInt("illumination");
                                data.temp = rs.getInt("temp");
                                data.humidity = rs.getInt("humidity");

                                return data;
                        }
                }
                finally {
                        con.close();
                }
                return null;
        }
        
        public static ArrayList<SensingData> getRecentSensingDataList(String user_id, int duration)
        			throws SQLException, IOException
        {
        	Connection con = getConnection();
            try {
                    Statement st = con.createStatement();
                    
                    System.out.println("SELECT illumination, temp, humidity FROM SensingData"
                            + " WHERE user_id="+user_id+" AND created_at > TIMESTAMPADD(MINUTE, -"+duration+", NOW())"
                            + "  ORDER BY created_at DESC LIMIT 300");

                    ResultSet rs = st.executeQuery("SELECT illumination, temp, humidity FROM SensingData"
                                    + " WHERE user_id="+user_id+" AND created_at > TIMESTAMPADD(MINUTE, -"+duration+", NOW())"
                                    + "  ORDER BY created_at DESC LIMIT 300");

                    ArrayList<SensingData> list = new ArrayList<SensingData>();
                    while(rs.next())
                    {
                            SensingData data = new SensingData();
                            data.illumination = rs.getInt("illumination");
                            data.temp = rs.getInt("temp");
                            data.humidity = rs.getInt("humidity");
                            
                            list.add(data);
                    }
                    
                    
                    return list;
            }
            finally {
                    con.close();
            }
        }
        
        

        public static Connection getConnection() throws SQLException {
                System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");

                return DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/bluescreen?useUnicode=true&characterEncoding=UTF-8",
                                "root",
                                "sysuser");
        }
}
