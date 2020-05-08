package f.jhandy.ignite.cache.store;

import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sunmoonone
 * @version 2018/12/26
 */
public class OrmConfig {

    @NotBlank
    private String schema;
    @NotBlank
    private String table;
    @NotBlank
    private String model;
    @NotEmpty
    private Map<String, OrmFieldConfig> fields;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, OrmFieldConfig> getFields() {
        return fields;
    }

    public void setFields(Map<String, OrmFieldConfig> fields) {
        this.fields = fields;
    }

    public JdbcType buildJdbcType(String cacheName){
        JdbcType jt = new JdbcType();
        jt.setCacheName(cacheName);
        jt.setDatabaseSchema(schema);
        jt.setDatabaseTable(table);
        jt.setValueType(model);

        JdbcTypeField[] keyFields = buildKeyFields();
        jt.setKeyFields(keyFields);
        jt.setKeyType(keyFields[0].getJavaFieldType());

        jt.setValueFields(buildValueFields());

        return jt;
    }

    /**
     * only build the first pk field
     * @return
     */
    private JdbcTypeField[] buildKeyFields() {
        JdbcTypeField[] keyFields = new JdbcTypeField[1];

        for(Map.Entry<String, OrmFieldConfig> item: fields.entrySet()){

            if(item.getValue().isPk()) {

                keyFields[0] = item.getValue().buildJdbcTypeField(item.getKey());
                break;
            }

        }
        if(keyFields[0]==null){
            throw new IllegalStateException("pk field required");
        }
        return keyFields;
    }

    private JdbcTypeField[] buildValueFields(){
        List<JdbcTypeField> valueFields = new ArrayList<>();

        for(Map.Entry<String, OrmFieldConfig> item: fields.entrySet()){

//            if(item.getValue().isPk()) {
//                continue;
//            }

            valueFields.add(item.getValue().buildJdbcTypeField(item.getKey()));
        }

        if(valueFields.size()==0){
            throw new IllegalStateException("at least one value field required");
        }
        return valueFields.toArray(new JdbcTypeField[0]);
    }
}
