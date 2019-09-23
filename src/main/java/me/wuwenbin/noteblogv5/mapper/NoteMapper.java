package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.Note;

/**
 * @author wuwen
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}
