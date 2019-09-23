package me.wuwenbin.noteblogv5.service.impl.dict;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.mapper.DictMapper;
import me.wuwenbin.noteblogv5.model.entity.Dict;
import me.wuwenbin.noteblogv5.service.interfaces.dict.DictService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

import java.util.List;

/**
 * @author wuwen
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final JdbcTemplate jdbcTemplate;
    private final DictMapper dictMapper;

    public DictServiceImpl(JdbcTemplate jdbcTemplate, DictMapper dictMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dictMapper = dictMapper;
    }

    @Override
    public int articleCateReferCnt(Long cateId) {
        String sql = "select count(*) as cnt from refer_article_cate where cate_id = ?";
        Integer c = jdbcTemplate.queryForObject(sql, Integer.class, cateId);
        return NumberUtils.parseNumber(String.valueOf(c), Integer.class);
    }

    @Override
    public List<Dict> findCatesByArticleId(Long articleId) {
        String sql = "select * from nb_dict where `group` = ? and id in (select cate_id from refer_article_cate where article_id = ?)";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Dict.class), DictGroup.GROUP_CATE, articleId);
    }


    @Override
    public int articleTagReferCnt(Long tagId) {
        String sql = "select count(*) as cnt from refer_article_tag where tag_id = ?";
        Integer c = jdbcTemplate.queryForObject(sql, Integer.class, tagId);
        return NumberUtils.parseNumber(String.valueOf(c), Integer.class);
    }

    @Override
    public List<Dict> findTagsByArticleId(Long articleId) {
        String sql = "select * from nb_dict where `group`=? and  id in (select tag_id from refer_article_tag where article_id = ?)";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Dict.class), DictGroup.GROUP_TAG, articleId);
    }

    @Override
    public List<Dict> findList(String groupName) {
        return this.list(Wrappers.<Dict>query().eq("`group`", groupName));
    }

    @Override
    public List<Dict> findTagList30() {
        return dictMapper.findTagsTab();
    }

    @Override
    public void deleteArticleRefer(Long articleId) {
        String sql1 = "delete from refer_article_cate where article_id = ?";
        jdbcTemplate.update(sql1, articleId);
        String sql2 = "delete from refer_article_tag where article_id = ?";
        jdbcTemplate.update(sql2, articleId);
    }
}
