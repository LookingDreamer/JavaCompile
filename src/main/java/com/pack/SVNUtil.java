package com.pack;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNLogEntry;
import java.lang.*;



public class SVNUtil {

    private static SVNRepository repository = null;

    private static Logger logger = Logger.getLogger(SVNUtil.class);

    /**
     * 通过不同的协议初始化版本库
     */
    public static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    /**
     * 验证登录svn
     */
    public static SVNClientManager authSvn(String svnRoot, String username,
                                           String password) {
        // 初始化版本库
        setupLibrary();

        // 创建库连接
//        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL
                    .parseURIEncoded(svnRoot));
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
            return null;
        }

        // 身份验证
        ISVNAuthenticationManager authManager = SVNWCUtil

                .createDefaultAuthenticationManager(username, password);

        // 创建身份验证管理器
        repository.setAuthenticationManager(authManager);

        DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        SVNClientManager clientManager = SVNClientManager.newInstance(options,
                authManager);
        return clientManager;
    }

    /**
     * Make directory in svn repository
     * @param clientManager
     * @param url
     * 			eg: http://svn.ambow.com/wlpt/bsp/trunk
     * @param commitMessage
     * @return
     * @throws SVNException
     */
    public static SVNCommitInfo makeDirectory(SVNClientManager clientManager,
                                              SVNURL url, String commitMessage) {
        try {
            return clientManager.getCommitClient().doMkDir(
                    new SVNURL[] { url }, commitMessage);
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return null;
    }

    /**
     * Imports an unversioned directory into a repository location denoted by a
     * 	destination URL
     * @param clientManager
     * @param localPath
     * 			a local unversioned directory or singal file that will be imported into a
     * 			repository;
     * @param dstURL
     * 			a repository location where the local unversioned directory/file will be
     * 			imported into
     * @param commitMessage
     * @param isRecursive 递归
     * @return
     */
    public static SVNCommitInfo importDirectory(SVNClientManager clientManager,
                                                File localPath, SVNURL dstURL, String commitMessage,
                                                boolean isRecursive) {
        try {
            return clientManager.getCommitClient().doImport(localPath, dstURL,
                    commitMessage, null, true, true,
                    SVNDepth.fromRecurse(isRecursive));
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return null;
    }

    /**
     * Puts directories and files under version control
     * @param clientManager
     * 			SVNClientManager
     * @param wcPath
     * 			work copy path
     */
    public static void addEntry(SVNClientManager clientManager, File wcPath) {
        try {
            clientManager.getWCClient().doAdd(new File[] { wcPath }, false,
                    false, false, SVNDepth.fromRecurse(true), false, false,
                    true);
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
    }

    /**
     * Collects status information on a single Working Copy item
     * @param clientManager
     * @param wcPath
     * 			local item's path
     * @param remote
     * 			true to check up the status of the item in the repository,
     *			that will tell if the local item is out-of-date (like '-u' option in the SVN client's
     *			'svn status' command), otherwise false
     * @return
     * @throws SVNException
     */
    public static SVNStatus showStatus(SVNClientManager clientManager,
                                       File wcPath, boolean remote) {
        SVNStatus status = null;
        try {
            status = clientManager.getStatusClient().doStatus(wcPath, remote);
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return status;
    }

    /**
     * Commit work copy's change to svn
     * @param clientManager
     * @param wcPath
     *			working copy paths which changes are to be committed
     * @param keepLocks
     *			whether to unlock or not files in the repository
     * @param commitMessage
     *			commit log message
     * @return
     * @throws SVNException
     */
    public static SVNCommitInfo commit(SVNClientManager clientManager,
                                       File wcPath, boolean keepLocks, String commitMessage) {
        try {
            return clientManager.getCommitClient().doCommit(
                    new File[] { wcPath }, keepLocks, commitMessage, null,
                    null, false, false, SVNDepth.fromRecurse(true));
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return null;
    }

    /**
     * Updates a working copy (brings changes from the repository into the working copy).
     * @param clientManager
     * @param wcPath
     * 			working copy path
     * @param updateToRevision
     * 			revision to update to
     * @param depth
     * 			update的深度：目录、子目录、文件
     * @return
     * @throws SVNException
     */
    public static long update(SVNClientManager clientManager, File wcPath,
                              SVNRevision updateToRevision, SVNDepth depth) {
        SVNUpdateClient updateClient = clientManager.getUpdateClient();

		/*
		 * sets externals not to be ignored during the update
		 */
        updateClient.setIgnoreExternals(false);

		/*
		 * returns the number of the revision wcPath was updated to
		 */
        try {
            return updateClient.doUpdate(wcPath, updateToRevision,depth, false, false);
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return 0;
    }

    /**
     * recursively checks out a working copy from url into wcDir
     * @param clientManager
     * @param url
     * 			a repository location from where a Working Copy will be checked out
     * @param revision
     * 			the desired revision of the Working Copy to be checked out
     * @param destPath
     * 			the local path where the Working Copy will be placed
     * @param depth
     * 			checkout的深度，目录、子目录、文件
     * @return
     * @throws SVNException
     */
    public static long checkout(SVNClientManager clientManager, SVNURL url,
                                SVNRevision revision, File destPath, SVNDepth depth) {

        SVNUpdateClient updateClient = clientManager.getUpdateClient();
		/*
		 * sets externals not to be ignored during the checkout
		 */
        updateClient.setIgnoreExternals(false);
		/*
		 * returns the number of the revision at which the working copy is
		 */
        try {
            return updateClient.doCheckout(url, destPath, revision, revision,depth, false);
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return 0;
    }

    /**
     * 确定path是否是一个工作空间
     * @param path
     * @return
     */
    public static boolean isWorkingCopy(File path){
        if(!path.exists()){
            logger.warn("'" + path + "' not exist!");
            return false;
        }
        try {
            if(null == SVNWCUtil.getWorkingCopyRoot(path, false)){
                return false;
            }
        } catch (SVNException e) {
            logger.error(e.getErrorMessage(), e);
        }
        return true;
    }


    public static  List<String> filterCommitHistory(PackBean packBean, final List<Map<String, String>> commitList,String svnIntervalDays) throws Exception {

        // 过滤条件
        final List<String> res = new ArrayList<String>();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = format.format(new Date());
        final String startDate = packBean.getStartDate() ;
        final String endDate = packBean.getEndDate() ;
        String svnstartRevision = packBean.getStartRevision() ;
        String svnendRevision = packBean.getEndRevision() ;
        logger.info("获取配置间隔天数: "+ svnIntervalDays);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, - Integer.parseInt(svnIntervalDays) );
        Date monday = c.getTime();
        String preMonday = format.format(monday);
        logger.info("获取当前日期:" + nowDate +" "+svnIntervalDays+ "天前的日期:"+preMonday);
        final Date begin;
        final Date end;
        final String startDateString ;
        final String endDateString ;
        if (startDate == null || startDate.equals("")){
            logger.info("开始日期:"+preMonday);
            begin = format.parse(preMonday);
            startDateString = preMonday;
        }else {
            logger.info("从请求参数获取开始日期:"+startDate);
            startDateString = startDate;
            if ( ! PackUtils.isValidDate(startDate)){
                logger.error("开始日期格式不正确");
                res.add("2");
                return  res;
            }else{
                begin = format.parse(startDate);

            }

        }
        if (endDate == null || endDate.equals("")){
            endDateString = nowDate;
            logger.info("结束日期:"+nowDate);
            end = format.parse(nowDate);
        }else{
            logger.info("从请求参数获取结束日期:"+endDate);
            endDateString = endDate ;
            if (!PackUtils.isValidDate(endDate)){
                logger.error("结束日期格式不正确");
                res.add("3");
                return  res;
            }else{
                end = format.parse(endDate);

            }

        }

        final String author = "";
        long startRevision ;
        long endRevision ;//表示最后一个版本
        if (svnstartRevision == null || svnstartRevision.equals("")){
            startRevision = 0;
        }else{
            startRevision = Long.parseLong(svnstartRevision);
        }
        if (svnendRevision == null || svnendRevision.equals("")){
            endRevision = -1;//表示最后一个版本
        }else{
            endRevision = Long.parseLong(svnstartRevision);
        }
        //String[] 为过滤的文件路径前缀，为空表示不进行过滤
        repository.log(new String[]{""},
                startRevision,
                endRevision,
                true,
                true,
                new ISVNLogEntryHandler() {
                    @Override
                    public void handleLogEntry(SVNLogEntry svnlogentry)
                            throws SVNException {
                        //依据提交时间进行过滤
                        if (svnlogentry.getDate().after(begin)
                                && svnlogentry.getDate().before(end)) {
                            // 依据提交人过滤
                            if (!"".equals(author)) {
                                if (author.equals(svnlogentry.getAuthor())) {
                                    fillResult(svnlogentry);
                                }
                            } else {
                                fillResult(svnlogentry);
                            }
                        }
                    }

                    public void fillResult(SVNLogEntry svnlogentry) {
                        //getChangedPaths为提交的历史记录MAP key为文件名，value为文件详情
                        HashMap<String,String> commit = new HashMap<String,String>();
                        logger.info("getAuthor "+svnlogentry.getAuthor().toString());
                        logger.info("getRevision "+svnlogentry.getRevision());
                        logger.info("getRevisionProperties "+svnlogentry.getRevisionProperties().toString());
                        logger.info("getMessage "+svnlogentry.getMessage().toString());
                        logger.info("getChangedPaths "+svnlogentry.getChangedPaths().toString());
                        logger.info("");
                        logger.info("");
//                        commit.put("Date",svnlogentry.getDate().toString());
                        commit.put("Date",formatDate.format(svnlogentry.getDate()));
                        commit.put("Author",svnlogentry.getAuthor().toString());
                        commit.put("Revision",svnlogentry.getRevision()+"");
                        commit.put("Message",svnlogentry.getMessage().toString());

                        // 将Map Key 转化为Path List
                        List<String> pathKeyList = new ArrayList<String>(svnlogentry.getChangedPaths().keySet());
                        String pathsString = StringUtils.join(pathKeyList, ",");
                        commit.put("Paths",pathsString);
                        commit.put("FileCount",pathKeyList.size()+"");
                        List<Object> pathValueList = new ArrayList<Object>(svnlogentry.getChangedPaths().values());
                        String pathsValueString = StringUtils.join(pathValueList, ",");
                        commit.put("PathsProperties",pathsValueString);
                        commit.put("startDate",startDateString);
                        commit.put("endDate",endDateString);
                        commitList.add(commit);
                        logger.info("");

                    }
                });
        res.add("1");
        return  res;
    }

}