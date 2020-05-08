package f.example.ignite.cache.starter.user.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * @author sunmoonone
 * @version 2018/12/29
 */
public class UserVo {
    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField
    private String name;

    public UserVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
