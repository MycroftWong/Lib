package com.mycroft.sample.service;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * 文件管理实现类
 *
 * @author wangqiang
 */
public final class FileServiceImpl implements IFileService {

    private static class Holder {
        private static final FileServiceImpl INSTANCE = new FileServiceImpl();
    }

    public static FileServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private FileServiceImpl() {
    }

    private static final String DIR_NET_CACHE = "net";

    @Override
    public File getNetCacheDir() {
        return new File(Utils.getApp().getCacheDir(), DIR_NET_CACHE);
    }

}
