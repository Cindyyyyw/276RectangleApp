/*
 * Copyright 2002-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }
  
  public String handleRectangleSubmit(Rectangle rectangle)throws Exception{
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rectangle (id serial, name varchar(20), color varchar(20), width smallint, height smallint");
      String sql = "INSERT INTO rectangle (color,width,height) VALUES ('" + rectangle.getName() + "," + rectangle.getColor() + "','" + rectangle.getWidth() + "','" + rectangle.getHeight() + "')";
      stmt.executeUpdate(sql);
      System.out.println(rectangle.getName() + " " +rectangle.getColor() + " " + rectangle.getWidth() + " " + rectangle.getHeight()); // print rectangle on console
      return "redirect:/rectangle/success";
    } catch (Exception e) {
      // model.put("message",e.getMessage());
      return "error";
    }
  }

  @GetMapping("/rectangle/success")
  public String getRectangleSuccess(Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM rectangle");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        String name = rs.getString("name");
        String color = rs.getString("color");
        String id = rs.getString("id");
        
        output.add(id + "," + name + "," + color);
      }

      model.put("records", output);
      return "success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    
  }

  // @GetMapping("/rectangle/display/{rid}")
  // public String getSpecificRectangle(Map<String, Object> model, @PathVariable String rid){ 
  //   System.out.println(rid);
  //   // 
  //   // query DB : SELECT * FROM people WHERE id={pid}
  //   model.put("id", rid);
  //   return "readrectangle";


  // }


  @RequestMapping("/")
  String index() {
    return "index";
  }
  @GetMapping(path = "/Rectangle")

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  
  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
