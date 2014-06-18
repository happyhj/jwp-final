package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import next.model.Question;
import next.support.db.ConnectionManager;

public class QuestionDao {
	private Connection conn;
	
	public QuestionDao(Connection conn) {
		this.conn = conn;
	}
	
	public void insert(Question question) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, question.getWriter());
			pstmt.setString(2, question.getTitle());
			pstmt.setString(3, question.getContents());
			pstmt.setTimestamp(4, new Timestamp(question.getTimeFromCreateDate()));
			pstmt.setInt(5, question.getCountOfComment());

			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}

			if (con != null) {
				con.close();
			}
		}		
	}

	public List<Question> findAll() throws SQLException {
	    SelectJdbcTemplate template = new SelectJdbcTemplate(conn) {
	      void setValues(PreparedStatement pstmt) throws SQLException {
	    	  return;
		  }
	      Object mapRow(ResultSet rs) throws SQLException {
	    	  return mapQuestionRow(rs);
		  }	     
	    };
		String query = "SELECT questionId, writer, title, contents, createdDate, countOfComment FROM QUESTIONS " + 
				"order by questionId desc";
	    return (List<Question>)template.selectAll(query);
	}

	
	public Question findById(final long questionId) throws SQLException {
	    SelectJdbcTemplate template = new SelectJdbcTemplate(conn) {
	      void setValues(PreparedStatement pstmt) throws SQLException {
	    	  pstmt.setLong(1, questionId);
		  }
	      Object mapRow(ResultSet rs) throws SQLException {
	    	  return mapQuestionRow(rs);
	      }	    
	    };
		String query = "SELECT questionId, writer, title, contents, createdDate, countOfComment FROM QUESTIONS " + 
				"WHERE questionId = ?";
	    return (Question)template.selectById(query);
	}
	
	private Question mapQuestionRow(ResultSet rs) throws SQLException {
		return new Question(
	              rs.getLong("questionId"), 
	              rs.getString("writer"), 
	              rs.getString("title"), 
	              rs.getString("contents"), 
	              rs.getDate("createdDate"), 
	              rs.getInt("countOfComment"));		
	}
}
