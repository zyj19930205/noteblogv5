package me.wuwenbin.noteblogv5.service.impl.content;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import me.wuwenbin.noteblogv5.exception.AppRunningException;
import me.wuwenbin.noteblogv5.mapper.ArticleMapper;
import me.wuwenbin.noteblogv5.model.entity.Article;
import me.wuwenbin.noteblogv5.service.interfaces.content.ArticleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wuwen
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;
    private final JdbcTemplate jdbcTemplate;

    public ArticleServiceImpl(ArticleMapper articleMapper, JdbcTemplate jdbcTemplate) {
        this.articleMapper = articleMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createArticle(Article article, List<Integer> cateIds, List<String> tagNames) throws PinyinException {
        if (CollectionUtils.isEmpty(cateIds)) {
            throw new AppRunningException("文章至少得有一个分类归属！");
        }
        if (StrUtil.isNotEmpty(article.getUrlSeq())) {
            article.setUrlSeq(PinyinHelper.convertToPinyinString(article.getUrlSeq(), "", PinyinFormat.WITHOUT_TONE));
            int cnt = articleMapper.selectCount(Wrappers.<Article>query().eq("url_seq", article.getUrlSeq()));
            boolean isExistUrl = cnt > 0;
            if (isExistUrl) {
                throw new AppRunningException("已存在 url：" + article.getUrlSeq());
            }
        }
        setArticleSummaryAndTxt(article);
        decorateArticle(article);
        int affect = articleMapper.insert(article);
        Long articleId = article.getId();
        cateIds.forEach(cateId -> jdbcTemplate.update("insert into refer_article_cate values (?,?)", articleId, cateId));
        if (!CollectionUtils.isEmpty(tagNames)) {
            saveArticleTags(articleId, tagNames);
        }
        return affect;
    }

    @Override
    public int updateArticle(Article article, List<Integer> cateIds, List<String> tagNames) throws PinyinException {
        if (CollectionUtils.isEmpty(cateIds)) {
            throw new AppRunningException("文章至少得有一个分类归属！");
        }
        if (StringUtils.isEmpty(article.getId())) {
            throw new AppRunningException("未指定修改文章的ID");
        }
        if (StrUtil.isNotEmpty(article.getUrlSeq())) {
            article.setUrlSeq(PinyinHelper.convertToPinyinString(article.getUrlSeq(), "", PinyinFormat.WITHOUT_TONE));
            int cnt = articleMapper.selectCount(Wrappers.<Article>query().eq("url_seq", article.getUrlSeq()));
            if (cnt > 0) {
                throw new AppRunningException("已存在 url：" + article.getUrlSeq());
            }
        }
        setArticleSummaryAndTxt(article);
        decorateArticle(article);
        int affect = articleMapper.updateById(article);
        long articleId = article.getId();
        jdbcTemplate.update("delete from refer_article_cate where article_id = ?", articleId);
        cateIds.forEach(cateId -> jdbcTemplate.update("insert into refer_article_cate values (?,?)", articleId, cateId));
        if (!CollectionUtils.isEmpty(tagNames)) {
            jdbcTemplate.update("delete from refer_article_tag where article_id = ?", articleId);
            saveArticleTags(articleId, tagNames);
        }
        return affect;
    }

    @Override
    public long sumArticleWords() {
        return articleMapper.sumArticleWords();
    }

    @Override
    public List<Article> findRandomArticles(int limit) {
        return articleMapper.findRandomArticles(limit);
    }

    @Override
    public boolean updateTopById(long articleId, boolean top) {
        if (top) {
            int maxTop = articleMapper.selectMaxTop();
            return update(Wrappers.<Article>update().set("top", maxTop + 1).eq("id", articleId));
        } else {
            int currentTop = articleMapper.selectById(articleId).getTop();
            articleMapper.updateTopsByTop(currentTop);
            return update(Wrappers.<Article>update().set("top", 0).eq("id", articleId));
        }
    }

    @Override
    public void updateViewsById(long articleId) {
        articleMapper.updateViewsById(articleId);
    }

    @Override
    public int updateApproveCntById(long articleId) {
        return articleMapper.updateApproveCntById(articleId);
    }
}
