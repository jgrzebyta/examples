package org.yournamehere.server.utils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
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

        String sql = "select t.id as tokenid, t.expired, t.useragent, u.id, u.username, u.password from tokens t left join users as u on u.id = t.userid where t.id = ?";

        try {
            Token toReturn = templete.queryForObject(sql, new RowMapper<Token>() {

                @Override
                public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LocalUser user = LocalUser.getInstance(rs.getString("id"), rs.getString("username"), rs.getString("password"));
                    Token t = new Token(user, rs.getString("tokenid"), rs.getTimestamp("expired"), rs.getString("useragent"));
                    return t;
                }
            }, tokenId);

            return toReturn;
        } catch (EmptyResultDataAccessException e) {
            // do nothing
        }
        return null;
    }

    /**
     * Get instance of {@link LocalUser} by username.
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public LocalUser getLocalUser(String username) throws UsernameNotFoundException {
        log.debug("return user details");

        String sql = "select id, username, password from users where username = ?";
        try {
            LocalUser user = templete.queryForObject(sql, new RowMapper<LocalUser>() {

                @Override
                public LocalUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return LocalUser.getInstance(rs.getString(1), rs.getString(2), rs.getString(3));
                }

            }, username);

            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException(String.format("user %s not found ", username));
        }
    }
    
    /**
     * Get instance of {@link LocalUser} by User ID.
     * @param userId
     * @return
     * @throws UsernameNotFoundException 
     */
    public LocalUser getLocalUserById(String userId) throws AuthenticationException {
        String sql = "select id, username, password from users where id = ?";
        
        try{
            LocalUser user = templete.queryForObject(sql, new RowMapper<LocalUser>() {

                @Override
                public LocalUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return LocalUser.getInstance(rs.getString(1), rs.getString(2), rs.getString(3));
                }
            }, userId);
            
            return user;
        } catch (EmptyResultDataAccessException empty) {
            log.error("{}", empty.getMessage());
            throw new BadCredentialsException("user id is not valid");
        }
    }

    // replace double SQL by 
    /*
     public boolean isUserExist(String username) {
     log.debug("check if user exists: {}", username);
     String sql = "select count(*) from users where username = ?";

     Long out = templete.queryForObject(sql, Long.class, username);

     return out != 0;
     }*/
    /**
     * Get list of all {@link User} instances.
     *
     * @return
     * @throws UsernameNotFoundException
     */
    public List<User> getAuthorityUsers() {
        log.debug("return user details");

        String sql = "select id, username, password from users";
        List<User> users = templete.query(sql, new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                LocalUser lu = LocalUser.getInstance(rs.getString(1), rs.getString(2), rs.getString(3));
                return new User(lu.getUsername(), lu.getPassword(), GrantedAuthoritiesUtils.getAuthorities());
            }

        }, null);

        return users;
    }

    public boolean saveToken(final Token t) {
        String sql = "insert into tokens(id, userid, expired, useragent) values (?,?,?,?)";

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

        String sql = "delete from tokens where id = ?";
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
