package com.zzb.cm.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.CosUtils;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.dao.INSBFilelibraryUploadCosFailDao;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;
import com.zzb.cm.service.INSBFilelibraryUploadCosFailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class INSBFilelibraryUploadCosFailServiceImpl extends BaseServiceImpl<INSBFilelibraryUploadCosFail> implements
        INSBFilelibraryUploadCosFailService {
    @Resource
    private INSBFilelibraryUploadCosFailDao filelibraryUploadCosFail;
    @Resource
    private INSBFilelibraryDao insbFilelibraryDao;


    @Override
    protected BaseDao<INSBFilelibraryUploadCosFail> getBaseDao() {
        return filelibraryUploadCosFail;
    }

    @Override
    public boolean deleteFileLibraryData(String[] ids) {
        return false;
    }

    @Override
    public List<INSBFilelibraryUploadCosFail> selectListByMap(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<INSBFilelibraryUploadCosFail> selectOneTimeReUploadCos() {
        return filelibraryUploadCosFail.selectOneTimeReUploadCos();
    }

    @Override
    public void saveFileInfo(INSBFilelibraryUploadCosFail filelibraryUploadCosFail) {

    }

    @Override
    public List<INSBFilelibraryUploadCosFail> queryAll(int offset, int limit) {
        return null;
    }

    public void uploadCosTask() {
        //保存方法 com.zzb.cm.service.impl.INSBFilelibraryServiceImpl#uploadOneFileByCos
        //每次查询最近 100条
        List<INSBFilelibraryUploadCosFail> list = this.selectOneTimeReUploadCos();
        LogUtil.info("uploadCosTask 任务 开始");
        for (int i = 0; i < list.size(); i++) {
            INSBFilelibraryUploadCosFail item = list.get(i);
            try {
                String filephysicalpath = item.getFilephysicalpath();
                //开始上传Cos
                File tempFile = new File(filephysicalpath);
                if (!tempFile.exists()) {
                    // 删除库记录
                    LogUtil.warn("文件不存在，filelibraryid = " + item.getFilelibraryid() + "  定时任务上传到cos 失败,清除文件上传任务");
                    filelibraryUploadCosFail.deleteById(item.getId());
                    continue;
                } else {
                    Map<String, String> result = CosUtils.uploadCosFile(tempFile.getPath(), "cmbg");
                    if (!StringUtil.isEmpty(result.get("url"))) {
                        if (!result.get("url").startsWith("https")) {
                            result.put("url", result.get("url").replace("http", "https"));
                        }
                    }
                    if ("0".equals(result.get("code"))) {
                        // INSBFilelibrary_selectById INSBFilelibrary_updateById
                        INSBFilelibrary insbFilelibrary = insbFilelibraryDao.selectById(item.getFilelibraryid());
                        if("proto".equals(item.getImagetype())) {
                            insbFilelibrary.setFilepath(result.get("url"));
                        }else if("small".equals(item.getImagetype())) {
                            insbFilelibrary.setSmallfilepath(result.get("url"));
                        }
                        insbFilelibraryDao.updateById(insbFilelibrary);
                        filelibraryUploadCosFail.deleteById(item.getId());
                    } else {
                        LogUtil.warn("文件不存在，filelibraryid = " + item.getFilelibraryid() + "  定时任务上传到cos 失败 "+" ,result.msg = "+result.get("msg"));
                    }
                }
            } catch (Exception ex) {
                LogUtil.warn("文件，filelibraryid = " + item.getFilelibraryid() + " 定时任务上传到cos 失败,上传发生异常");
            }
            LogUtil.info("uploadCosTask 任务 结束");
        }
    }
}
