package me.wuwenbin.noteblogv5.controller.management.upload;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.entity.Upload;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.util.NbUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * created by Wuwenbin on 2018/8/3 at 22:06
 *
 * @author wuwenbin
 */
@Slf4j
@RestController
@RequestMapping("/management")
public class UploadController extends BaseController {

    private final HttpServletRequest request;

    private final String EDITOR_MD_FILE_NAME = "editormd-image-file";
    private final String LAY_UPLOADER_FILE_NAME = "file";

    public UploadController(HttpServletRequest request) {
        this.request = request;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(@RequestParam(value = LAY_UPLOADER_FILE_NAME, required = false) MultipartFile file, @RequestParam("reqType") String reqType) {
        String base64 = request.getParameter("base64");
        String GRAFFITI = "1";
        if (StrUtil.isNotEmpty(base64) && GRAFFITI.equals(base64)) {
            String base64Str = request.getParameter("img_base64_data");
            file = NbUtils.base64ToMultipartFile(base64Str);
        }
        User su = getSessionUser(request);
        return Objects.requireNonNull(NbUtils.getUploadServiceByConfig()).upload(su.getId(), file, reqType, (v) -> {
        }, null);
    }


    @RequestMapping(value = "/upload/editorMD", method = RequestMethod.POST)
    public Object uploadEditorMd(@RequestParam(value = EDITOR_MD_FILE_NAME) MultipartFile file) {
        try {
            User su = getSessionUser(request);
            Upload upload = Objects.requireNonNull(NbUtils.getUploadServiceByConfig()).uploadIt(su.getId(), file, (v) -> {
            }, null);
            String visitUrl = upload.getVirtualPath();
            return MapUtil.of(new Object[][]{
                    {"success", 1}, {"message", "上传成功！"}, {"url", visitUrl}
            });
        } catch (IOException e) {
            log.error("editormd编辑器上传图片失败，错误信息：{}", e.getMessage());
            return MapUtil.of("success", 0);
        }
    }
}
