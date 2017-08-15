# 修改 “暂住证” 为 “居住证”。上一版本之前就有更新，不知准生产是被回滚还是执行不成功。
UPDATE insccode SET codename='居住证' WHERE parentcode='insuranceimage' AND codename='暂住证';

# 重新执行排序语句
UPDATE insccode SET codeorder=20 WHERE parentcode='insuranceimage' AND codename='居住证';


