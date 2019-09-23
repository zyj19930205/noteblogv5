package me.wuwenbin.noteblogv5.model.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * created by Wuwenbin on 2018/7/15 at 12:11
 *
 * @author wuwenbin
 */
@Data
@Builder
public class Upload implements Serializable {

    private Long id;
    private String diskPath;
    private String virtualPath;
    private Date upload;
    private String type;
    private Long userId;
}
