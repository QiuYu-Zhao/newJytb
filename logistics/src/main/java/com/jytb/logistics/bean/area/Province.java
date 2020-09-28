package com.jytb.logistics.bean.area;

import com.chexiao.base.dao.annotation.Column;
import com.chexiao.base.dao.annotation.Id;
import com.chexiao.base.dao.annotation.NotDBColumn;
import com.chexiao.base.dao.annotation.Table;

import java.io.Serializable;

/**
 * 〈省〉
 *
 * @author hyz
 * @create 2019/6/8
 */
@Table(name = "province")
public class Province implements Serializable{
    @NotDBColumn
    private static final long serialVersionUID = 64370051711230181L;

    @Id(insertable = true)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}