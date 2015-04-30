package org.yournamehere.server.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.yournamehere.shared.exceptions.OtherAuthenticationException;
import org.yournamehere.shared.schema.LocalUser;
import org.yournamehere.shared.schema.Token;

/**
 *
 * @author Jacek Grzebyta
 */
@Repository
public class SimpleDAO implements Serializable {

    private static final long serialVersionUID = -4278946852354526291L;

    @Resource
    private JdbcTemplate templete;

    private Logger log = LoggerFactory.getLogger(getClass());

    public Token validateToken(String tokenId) throws OtherAuthenticationException {
        log.debug("validate token: {}", tokenId);
        
        if (!StringUtils.hasText(tokenId)) {
            log.warn("token is not a text");
            throw new OtherAuthenticationException();
        }

        String sql = "select t.token, t.expired, t.useragent, u.id, u.username, u.password from tokens t left join users as u on u.id = t.userid where t.token = 'fdffef'";

        Token toReturn = templete.queryForObject(sql, new RowMapper<Token>() {

            @Override
            public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
                LocalUser user = LocalUser.getInstance(rs.getString("id"), rs.getString("username"), rs.getString("password"));
                Token t = new Token(user, rs.getString("token"), rs.getTimestamp("expired"), rs.getString("useragent"));
                return t;
            }
        }, tokenId);

        if (toReturn == null) {
            log.warn("token is not valid");
            throw new OtherAuthenticationException();
        }

        return toReturn;
    }

    public boolean isUserExist(String username) {
        log.debug("check if user exists: {}", username);
        String sql = "select count(*) from users where username = %";

        Long out = templete.queryForObject(sql, Long.class, username);

        return out != 0;
    }

    /**
     * Get instance of {@link LocalUser}.
     * @param username
     * @return
     * @throws UsernameNotFoundException 
     */
    public LocalUser getLocalUser(String username) throws UsernameNotFoundException {
        log.debug("return user details");

        String sql = "select id, username, password from users where username = %";

        if (!isUserExist(username)) {
            throw new UsernameNotFoundException("user " + username + "not found");
        }

        LocalUser user = templete.queryForObject(sql, new RowMapper<LocalUser>() {

            @Override
            public LocalUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                return LocalUser.getInstance(rs.getString(1), rs.getString(2), rs.getString(3));
            }

        }, username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("user %s not found", username));
        }

        return user;
    }
    
    /**
     * Get instance of {@link UserDetails} for Spring Security.
     * @param username
     * @return
     * @throws UsernameNotFoundException 
     */
    public UserDetails getUserDetails(String username) throws UsernameNotFoundException {
        LocalUser user = getLocalUser(username);
        User toReturn = new User(user.getUsername(), user.getPassword(), GrantedAuthoritiesUtils.getAuthorities());
        return toReturn;
    }

    public boolean saveToken(final Token t) {
        String sql = "insert into token(id, user_id, expired, user_agent) values (?,?,?,?)";

        final Timestamp expiringTime = new Timestamp(t.getExpired().getTime());

        int out = templete.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, t.getToken());
                ps.setString(2, t.getUser().getId());
                ps.setTimestamp(3, expiringTime);
                ps.setString(4, t.getUserAgent());
            }
        });

        if (out == 0) {
            return false;
        }

        return true;
    }

    public boolean removeToken(String token) {

        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("wrong token");
        }

        String sql = "delete from tokens where token_id = ?";
        int out = templete.update(sql, token);
        return out != 0;
    }
    
    public boolean addUser(final LocalUser u) {
        String sql = "insert into users(id, username, password) values (?,?,?)";
        
        int out = templete.update(sql, new PreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, u.getId());
                ps.setString(2, u.getUsername());
                ps.setString(3, u.getPassword());
            }
        });
        
        return out > 0;
    }
}
