package com.zzb.warranty.service.async;


import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by patri on 2017/2/19.
 */
public class AsyncTaskHelper {
    private static ExecutorService pool;

    private static AsyncTaskHelper asyncTaskHelper;

    private AsyncTaskHelper() {
        this.pool = Executors.newFixedThreadPool(10);
    }

    public static synchronized AsyncTaskHelper getInstance() {
        if (asyncTaskHelper == null) {
            asyncTaskHelper = new AsyncTaskHelper();
        }
        return asyncTaskHelper;
    }

    public void asyncInsert(final Identifiable entity, final BaseDao dao) {
        pool.submit(new Runnable() {
            public void run() {
                dao.insert(entity);
            }
        });
    }

    public void asyncUpdate(final Identifiable entity, final BaseDao dao) {
        pool.submit(new Runnable() {
            public void run() {
                dao.updateById(entity);
            }
        });
    }

    public void asyncDelete(final Identifiable entity, final BaseDao dao) {
        pool.submit(new Runnable() {
            public void run() {
                dao.delete(entity);
            }
        });
    }
}
