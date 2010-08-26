package org.geoserver.monitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.geoserver.monitor.MonitorQuery.Comparison;
import org.geoserver.monitor.MonitorQuery.SortOrder;
import org.geoserver.monitor.RequestData.Status;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class JDBCMonitorDAO implements MonitorDAO {

    PipeliningTaskQueue<Long> tasks = new PipeliningTaskQueue<Long>();
    JdbcTemplate jdbc;
    boolean asynchronous;
    
    public JDBCMonitorDAO(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        try {
            jdbc.execute("SELECT * FROM requests LIMIT 0");
        }
        catch(Exception e) {
            jdbc.execute("CREATE TABLE requests (id BIGINT AUTO_INCREMENT PRIMARY KEY, " + 
                    " status VARCHAR, path VARCHAR, body VARCHAR, query_string VARCHAR, server_host VARCHAR, "
                      + "http_method VARCHAR, start_time TIMESTAMP, end_time TIMESTAMP, total_time INT, "
                      + "remote_address VARCHAR, remote_host VARCHAR, remote_user VARCHAR, ows_service VARCHAR, "
                      + "ows_version VARCHAR, ows_operation VARCHAR, content_type VARCHAR, response_length BIGINT, "
                      + "error_message VARCHAR)");    
        }
        
        
        //TODO: indexes
        tasks.start();
    }
    
    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }
    
    public boolean isAsynchronous() {
        return asynchronous;
    }
    
    public RequestData add(RequestData data) {
        new Insert(data).run();
        return data;
    }

    public void update(RequestData data) {
        if (isAsynchronous()) {
            tasks.execute(data.getId(), new Update(data));    
        }
        else {
            new Update(data).run();  
        }
    }
    
    public void save(RequestData data) {
        update(data);
    }
   
    public List<RequestData> getOwsRequests() {
        return jdbc.query("SELECT * FROM requests WHERE ows_service is not null", new RequestDataMapper());
    }

    public List<RequestData> getOwsRequests(String service, String operation, String version) {
        return null;
    }

    public RequestData getRequest(long id) {
        return (RequestData) jdbc.queryForObject("SELECT * FROM requests WHERE id = ?", 
            new Object[]{id}, new RequestDataMapper());
    }

    public List<RequestData> getRequests() {
        return getRequests(new MonitorQuery().sort("startTime", SortOrder.DESC));
    }
    
    public List<RequestData> getRequests(MonitorQuery q) {
        List<Object> objs = new ArrayList();
        
        StringBuffer sql = new StringBuffer("SELECT * FROM requests");
        if (q.getFilterProperty() != null) {
            sql.append(" WHERE ").append(column(q.getFilterProperty()));
            if (q.getFilterValue() == null) {
                sql.append(" IS");
                if (q.getFilterCompare() != Comparison.EQ) {
                    sql.append( " NOT");
                }
                sql.append(" NULL");
            }
            else {
                sql.append(" ").append(q.getFilterCompare());
                if (q.getFilterCompare() == Comparison.IN) {
                    sql.append(" (");
                    for (Object o : (List)q.getFilterValue()) {
                        sql.append("?, ");
                        objs.add(prepare(o));
                    }
                    sql.setLength(sql.length()-2);
                    sql.append(")");
                }
                else {
                    sql.append(" ?");
                    objs.add(prepare(q.getFilterValue()));
                }
            }
        }
        
        if (q.getFromDate() != null || q.getToDate() != null) {
            if (q.getFilterProperty() != null) {
                sql.append(" AND");
            }
            else {
                sql.append(" WHERE");
            }
            
            sql.append(" ").append(column("startTime"));
            if (q.getFromDate() != null && q.getToDate() != null) {
                sql.append(" BETWEEN ? AND ?");
                 objs.add(prepare(q.getFromDate()));
                 objs.add(prepare(q.getToDate()));
            }
            else if (q.getFromDate() != null) {
                sql.append(" >= ?");
                objs.add(prepare(q.getFromDate()));
            }
            else {
                sql.append(" <= ?"); 
                objs.add(prepare(q.getToDate()));
            }
        }
        
        if (q.getSortBy() != null) {
            sql.append(" ORDER BY ").append(column(q.getSortBy())).append(" ").append(q.getSortOrder());
        }
        else if (q.getFromDate() != null || q.getToDate() != null) {
            //by default sort dates descending
            sql.append(" ORDER BY ").append(column("startTime")).append(" ").append(SortOrder.DESC);
        }
        
        //TODO: databases that don't do limit offset
        if (q.getCount() != null) {
            sql.append(" LIMIT " + q.getCount());
        }
        
        if (q.getOffset() != null) {
            sql.append(" OFFSET " + q.getOffset());
        }
        
        
        return jdbc.query(sql.toString(), objs.toArray(), new RequestDataMapper());
    }
    
    public List<RequestData> getHistory() {
        return null;
    }

    public List<RequestData> getLive() {
        return null;
    }
    
    public void clear() {
        jdbc.update("DELETE FROM requests");
    }

    public void dispose() {
        tasks.shutdown();
    }
    
    class Insert implements Runnable {
        RequestData r;
        
        Insert(RequestData data) {
            this.r = data;
        }
        
        public void run() {
            GeneratedKeyHolder key = new GeneratedKeyHolder();
            jdbc.update(new PreparedStatementCreator() {
                
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO requests (status, path, body, query_string, server_host, "
                        + "http_method, start_time, end_time, total_time, remote_address, remote_host, "
                        + "remote_user, ows_service, ows_version, ows_operation, content_type, "
                        + "response_length, error_message) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    
                    ps.setString(1, r.getStatus().toString());
                    ps.setString(2, r.getPath());
                    ps.setBytes(3, r.getBody());
                    ps.setString(4, r.getQueryString());
                    ps.setString(5, r.getHost());
                    ps.setString(6, r.getHttpMethod());
                    if (r.getStartTime() != null) {
                        ps.setTimestamp(7, new Timestamp(r.getStartTime().getTime()));    
                    }
                    else {
                        ps.setNull(7, Types.DATE);
                    }
                    
                    if (r.getEndTime() != null) {
                        ps.setTimestamp(8, new Timestamp(r.getEndTime().getTime()));
                    }
                    else {
                        ps.setNull(8, Types.DATE);
                    }
                    
                    ps.setLong(9, r.getTotalTime());
                    ps.setString(10, r.getRemoteAddr());
                    ps.setString(11, r.getRemoteHost());
                    ps.setString(12, r.getRemoteUser());
                    ps.setString(13, r.getOwsService());
                    ps.setString(14, r.getOwsVersion());
                    ps.setString(15, r.getOwsOperation());
                    ps.setString(16, r.getResponseContentType());
                    ps.setLong(17, r.getResponseLength());
                    ps.setString(18, r.getErrorMessage());
                    
                    return ps;
                }
            }, key);
            
            r.setId(key.getKey().longValue());
                    
//                    sql, new Object[]{r.getStatus().toString(), r.getPath(), r.getBody(), 
//                r.getQueryString(), r.getHost(), r.getHttpMethod(), r.getStartTime(),
//                r.getEndTime(), r.getTotalTime(), r.getRemoteAddr(), r.getRemoteHost(),
//                r.getUserName(), r.getOwsService(), r.getOwsVersion(), r.getOwsOperation(), 
//                r.getResponseContentType(), r.getResponseLength(), r.getErrorMessage()});
            
            
//              <id column="ID" name="id">
//            <generator class="native"/>
//          </id>
//          <property column="STATUS" name="status" type="Status"/>
//          <property column="PATH" name="path"/>
//          <property column="BODY" name="body" />
//          <property column="QUERY_STRING" name="queryString" length="1024"/>
//          <property column="SERVER_HOST" name="host"/>
//          <property column="HTTP_METHOD" name="httpMethod"/>
//          <property column="START_TIME" name="startTime" index="REQ_STRT_TIME_IDX"/>
//          <property column="END_TIME" name="endTime" index="REQ_END_TIME_IDX"/>
//          <property column="TOTAL_TIME" name="totalTime"/>
//          
//          <!--property column="REMOTE_ADDRESS" name="remoteAddr" length="11" /-->
//          <property column="REMOTE_ADDRESS" name="remoteAddr" />
//          <property column="REMOTE_HOST" name="remoteHost"/>
//          <property column="USER" name="userName"/>
//          
//          <property column="OWS_SERVICE" name="owsService"/>
//          <property column="OWS_VERSION" name="owsVersion"/>
//          <property column="OWS_OPERATION" name="owsOperation"/>
//          
//          <property column="CONTENT_TYPE" name="responseContentType"/>
//          <property column="RESPONSE_LENGTH" name="responseLength"/>
//          
//          <property column="ERROR_MESSAGE" name="errorMessage"/>
//          <property column="EXCEPTION_STACK_TRACE" name="error" type="Error"/>
        }
    }
    
    class Update implements Runnable {
        RequestData r;
        
        Update(RequestData data) {
            r = data;
        }
        
        public void run() {
            String sql = "UPDATE requests SET status = ?, path = ?, body = ?, query_string = ?, "
                + "server_host = ?, http_method = ?, start_time = ?, end_time = ?, total_time = ?, "
                + "remote_address = ?, remote_host = ?, remote_user = ?, ows_service = ?, ows_version = ?, "
                + "ows_operation = ?, content_type = ?, response_length = ?, error_message  = ? "
                + "WHERE id = ?";
              
              jdbc.update(sql, new Object[]{r.getStatus().toString(), r.getPath(), r.getBody(), 
                  r.getQueryString(), r.getHost(), r.getHttpMethod(), r.getStartTime(),
                  r.getEndTime(), r.getTotalTime(), r.getRemoteAddr(), r.getRemoteHost(),
                  r.getRemoteUser(), r.getOwsService(), r.getOwsVersion(), r.getOwsOperation(), 
                  r.getResponseContentType(), r.getResponseLength(), r.getErrorMessage(), r.getId()});
        }
        
    }
    
    String column(String property) {
        StringBuffer col = new StringBuffer();
        for (int i = 0; i < property.length(); i++) {
            char c = property.charAt(i);
            if (Character.isUpperCase(c)) {
                col.append("_");
            }
            col.append(Character.toUpperCase(c));
        }
        return col.toString();
    }
    
    Object prepare(Object value) {
        if (value instanceof Date) {
            return new Timestamp(((Date)value).getTime());
        }
        if (value instanceof Status) {
            return value.toString();
        }
        
        return value;
    }
    
    static class RequestDataMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            RequestData data = new RequestData();
            data.setId(rs.getLong(1));
            data.setStatus(Status.valueOf(rs.getString(2)));
            data.setPath(rs.getString(3));
            data.setBody(rs.getBytes(4));
            data.setQueryString(rs.getString(5));
            data.setHost(rs.getString(6));
            data.setHttpMethod(rs.getString(7));
            data.setStartTime(rs.getDate(8));
            data.setEndTime(rs.getDate(9));
            data.setTotalTime(rs.getLong(10));
            data.setRemoteAddr(rs.getString(11));
            data.setRemoteHost(rs.getString(12));
            data.setRemoteUser(rs.getString(13));
            data.setOwsService(rs.getString(14));
            data.setOwsVersion(rs.getString(15));
            data.setOwsOperation(rs.getString(16));
            data.setResponseContentType(rs.getString(17));
            data.setResponseLength(rs.getLong(18));
            data.setErrorMessage(rs.getString(19));
            
            return data;
        }
        
    }
}
