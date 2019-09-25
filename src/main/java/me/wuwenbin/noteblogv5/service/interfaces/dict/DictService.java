package me.wuwenbin.noteblogv5.service.interfaces.dict;

import com.baomidou.mybatisplus.extension.service.IService;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.Dict;

import java.util.List;

/**
 * @author wuwen
 */
@Mapper
public interface DictService extends IService<Dict> {

    /**
     * 查询关联文章的分类数量有多少
     *
     * @param cateId
     * @return
     */
    int articleCateReferCnt(Long cateId);

    /**
     * 根据文章查找对应的标签
     *
     * @param articleId
     * @return
     */
    List<Dict> findCatesByArticleId(String articleId);


    /**
     * 查询有关联的标签有多少
     *
     * @param tagId
     * @return
     */
    int articleTagReferCnt(Long tagId);

    /**
     * 根据文章查找对应的标签
     *
     * @param articleId
     * @return
     */
    List<Dict> findTagsByArticleId(String articleId);

    /**
     * 查找某种类型的l数据列表
     *
     * @param groupName
     * @return
     */
    List<Dict> findList(String groupName);

    /**
     * 查找数量最多的tag前30个
     *
     * @return
     */
    List<Dict> findTagList30();

    /**
     * 删除文章相关的关联
     *
     * @param articleId
     */
    void deleteArticleRefer(String articleId);
}
