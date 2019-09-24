package me.wuwenbin.noteblogv5.service.interfaces.content;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stuxuhai.jpinyin.PinyinException;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.mapper.DictMapper;
import me.wuwenbin.noteblogv5.mapper.UploadMapper;
import me.wuwenbin.noteblogv5.model.entity.Article;
import me.wuwenbin.noteblogv5.model.entity.Dict;
import me.wuwenbin.noteblogv5.model.entity.Upload;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import me.wuwenbin.noteblogv5.util.NbUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.util.RandomUtil.randomInt;

/**
 * @author wuwen
 */
public interface ArticleService extends IService<Article> {

    /**
     * 创建文章
     *
     * @param article
     * @param cateIds
     * @param tagNames
     * @return
     * @throws PinyinException
     */
    int createArticle(Article article, List<Integer> cateIds, List<String> tagNames) throws PinyinException;

    /**
     * 修改一篇文章
     *
     * @param article
     * @param cateIds
     * @param tagNames
     * @return
     * @throws PinyinException
     */
    int updateArticle(Article article, List<Integer> cateIds, List<String> tagNames) throws PinyinException;

    /**
     * 统计素有文章的总字数
     *
     * @return
     */
    long sumArticleWords();

    /**
     * 随机n篇文章
     *
     * @param limit
     * @return
     */
    List<Article> findRandomArticles(int limit);

    /**
     * 根据文章内容生成文章摘要
     *
     * @param article
     */
    default void setArticleSummaryAndTxt(Article article) {
        ParamService paramService = NbUtils.getBean(ParamService.class);
        int summaryLength = Integer.parseInt(paramService.findByName("summary_length").getValue());
        String clearContent = HtmlUtil.cleanHtmlTag(StrUtil.trim(article.getContent()));
        clearContent = StringUtils.trimAllWhitespace(clearContent);
        clearContent = clearContent.substring(0, Math.min(clearContent.length(), summaryLength));
        int allStandardLength = clearContent.length();
        int fullAngelLength = NbUtils.fullAngelWords(clearContent);
        int finalLength = allStandardLength - fullAngelLength / 2;
        if (StringUtils.isEmpty(article.getSummary())) {
            article.setSummary(clearContent.substring(0, Math.min(finalLength, summaryLength)));
        }
        article.setTextContent(clearContent);
    }

    /**
     * 装饰article的一些空值为默认值
     *
     * @param article
     */
    default void decorateArticle(Article article) {
        String cover = article.getCover();
        if (StrUtil.isNotEmpty(cover)) {
            UploadMapper uploadMapper = NbUtils.getBean(UploadMapper.class);
            Upload upload = uploadMapper.selectOne(Wrappers.<Upload>query().eq("virtual_path", cover));
            String imgSrc = upload.getDiskPath();
            File f = new File(imgSrc);
            ImgUtil.scale(f, f, 500, 312, new Color(238, 238, 238));
        }

        article.setPost(new Date());
        article.setViews(randomInt(666, 1609));
        article.setApproveCnt(randomInt(6, 169));
        if (StringUtils.isEmpty(article.getAppreciable())) {
            article.setAppreciable(false);
        }
        if (StringUtils.isEmpty(article.getCommented())) {
            article.setCommented(false);
        }
        if (StringUtils.isEmpty(article.getReprinted())) {
            article.setCommented(false);
        }
        if (StringUtils.isEmpty(article.getTop())) {
            article.setTop(0);
        }
    }

    /**
     * 处理隐藏标签
     * @param article
     */
    default void handleHideSpan(Article article){
        String contentHtml=article.getContent();

    }

    /**
     * 修改文章的 top 值
     *
     * @param articleId
     * @param top
     * @return
     * @throws Exception
     */
    boolean updateTopById(long articleId, boolean top);

    /**
     * 更新浏览量
     *
     * @param articleId
     */
    void updateViewsById(long articleId);

    /**
     * 更新点赞数
     *
     * @param articleId
     * @return
     */
    int updateApproveCntById(long articleId);

    /**
     * 保存文章的 tags
     *
     * @param articleId
     * @param tagNames
     */
    default void saveArticleTags(long articleId, List<String> tagNames) {
        DictMapper dictMapper = NbUtils.getBean(DictMapper.class);
        JdbcTemplate jdbcTemplate = NbUtils.getBean(JdbcTemplate.class);
        for (String tagName : tagNames) {
            boolean isExist = dictMapper.selectCount(Wrappers.<Dict>query().eq("name", tagName).eq("`group`", DictGroup.GROUP_TAG)) > 0;
            long tagId;
            if (isExist) {
                Dict oldTag = dictMapper.selectOne(Wrappers.<Dict>query().eq("name", tagName).eq("`group`", DictGroup.GROUP_TAG));
                tagId = oldTag.getId();
            } else {
                Dict newTag = Dict.builder().name(tagName).group(DictGroup.GROUP_TAG).build();
                dictMapper.insert(newTag);
                tagId = newTag.getId();
            }
            jdbcTemplate.update("insert into refer_article_tag (article_id, tag_id) values (?,?);", articleId, tagId);
        }
    }
}
