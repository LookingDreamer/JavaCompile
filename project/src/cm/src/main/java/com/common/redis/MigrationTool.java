package com.common.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by @Author Chen Haoming on 2016/6/21.
 * <p>
 * Migrate the old keys in Redis to new formatted-keys
 */
public class MigrationTool {

    private static final transient Logger LOG = Logger.getLogger(MigrationTool.class);

    private static final Pattern TARGET_KEY = Pattern.compile("[$](\\d+)");

    private Pattern originalKeyPattern;

    private int maxReplacementNum;

    private String targetKeyPattern;

    public MigrationTool(String originalKey, String targetKey) {
        originalKeyPattern = Pattern.compile(originalKey);
        this.targetKeyPattern = targetKey;
        Matcher matcher = TARGET_KEY.matcher(targetKey);

        while (matcher.find()) {
            String group = matcher.group(1);
            int max = Integer.parseInt(group);
            if (max > maxReplacementNum) {
                maxReplacementNum = max;
            }
        }
    }

    @Override
    public String toString() {
        return "{originalKeyPattern:" + originalKeyPattern + ",targetKeyPattern:" + targetKeyPattern + ",maxReplacementNum:" + maxReplacementNum + "}";
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        System.out.println("We are going to load context");
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:config/spring-redis-migration.xml");

        System.out.println("We are going to get bean: " + (List<MigrationTool>) context.getBean("keyMaps"));
        List<MigrationTool> migrationTools = (List<MigrationTool>) context.getBean("keyMaps");
        IRedisClient client = context.getBean(IRedisClient.class);

        String[] keyss = getKeys();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(keyss.length, 1000, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

        int totalkeys = 0;

        for (String _key : keyss) {
            System.out.println("Going to search key:" + _key);
            Set<String> keys = client.keys(_key);
            List<String> filteredKeys = keys.stream().filter(key -> !key.startsWith("cm:")).collect(Collectors.toList());
            totalkeys += filteredKeys.size();
            System.out.println("We are going to rename " + filteredKeys.size() + "/" + totalkeys + " old keys.");

            executor.execute(() -> {
                int keysize = filteredKeys.size();
                for(int j=0; j<keysize; j+=1) {
                    String oldKey = filteredKeys.get(j);
                    try {
                        for (MigrationTool tool : migrationTools) {
                            Matcher matcher = tool.originalKeyPattern.matcher(oldKey);
                            if (matcher.find()) {
                                String newKey = tool.targetKeyPattern;

                                if (tool.maxReplacementNum > 0) {
                                    for (int i = 1; i <= tool.maxReplacementNum; ++i) {
                                        String group = matcher.group(i);
                                        if (null != group) {
                                            newKey = newKey.replace("$" + i, group);
                                        }
                                    }
                                }

                                client.rename(oldKey, newKey);
                                System.out.println("Renamed key successfully:[oldKey:" + oldKey + ",newKey:" + newKey + "] " + (j+1) + "/" + keysize);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception occurred when handling old key:" + oldKey);
                        e.printStackTrace();
                    }
                }
            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!executor.isTerminated()) {
            try {
                Thread.sleep(10000);
                if (executor.getCompletedTaskCount() >= totalkeys) {
                    executor.shutdown();
                    System.out.println("All old keys are renamed. This tool is shutting down.");
                }
            } catch (InterruptedException e) {
                System.out.println("Current thread was interrupted. It should never happen.");
                e.printStackTrace();
            }
        }
    }

    private static String[] getKeys() {
        return new String[]{
                "typeid",
                "id",
                "flow_*",
                "TimerJob",
                "*@*@quotequery",
                "*@*@insurequery",
                "*@*@approvedquery",
                "*@*@quote",
                "*@*@insure",
                "*@*@approved",
                "Zmonitor:*",
                "supplementInfoVO_*",
                "temp_agent_job_no",
                "onlineUsers",
                "epay",
                "claim_*",
                "hotKeyList",
                "agentuserid:*",
                "tokenid:*",
                "validateCode:*",
                "ZZB_SPPLEMENT_ITEM_RULEID_*",
                "channel:payment:ChannelPayment_*",
                "channel:token:AccessToken_*",
                "channel:innerToken_*",
                "dadikey:*",
                "userLoginKey_*",
                "ChannelPayment_*",
                "*@*@INSBApplicant@*",
                "*@*@INSBInsured@*",
                "*@*@INSBCarowneinfo@*",
                "*_online",
                "m*m*",
                "*_*_*",
                "*-*-*-*-*",
                "[0-9]*"
        };
    }
}
