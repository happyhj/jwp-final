package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.model.Question;
 
public abstract class SelectJdbcTemplate {
  private Connection conn;
 
  public SelectJdbcTemplate(Connection conn) {
    this.conn = conn;
  }
   
  public Object selectById(String query) throws SQLException {
	  PreparedStatement pstmt = conn.prepareStatement(query);
	  setValues(pstmt);
	  
	  ResultSet rs = pstmt.executeQuery();
	  if(rs.next()) {
		  return mapRow(rs);
	  }	  
	  return null;
  }

  public Object selectAll(String query) throws SQLException {
	  PreparedStatement pstmt = conn.prepareStatement(query);
	  
	  ResultSet rs = pstmt.executeQuery();
	  List<Object> result = new ArrayList<Object>();
	  
	  while(rs.next()) {
		  result.add(mapRow(rs));
	  }	  
	  if(result.size() > 0) {
		  return result;
	  }
	  return null;
  }  
  
  
  abstract void setValues(PreparedStatement pstmt) throws SQLException;

  abstract Object mapRow(ResultSet rs) throws SQLException;
  
}

