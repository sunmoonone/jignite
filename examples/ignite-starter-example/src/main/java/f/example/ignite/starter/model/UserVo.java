package f.example.ignite.starter.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * @author sunmoonone
 * @version 2019/01/07
 */
public class UserVo {

    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField
    private String name;

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

    public UserVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
