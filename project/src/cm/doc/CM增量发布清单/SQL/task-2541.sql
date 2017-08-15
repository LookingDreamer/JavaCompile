/*==============================================================*/
/* Table: insbyoubaoliancommission                              */
/*==============================================================*/
create table insbyoubaoliancommission
(
   checkedId            varchar(32) comment '核保编号',
   checkedState         int comment '11:核保中 异步回调通知结果 12:核保通过 14:核保失败',
   ret                  bool comment 'true:成功 false:失败',
   code                 int comment '成功:0 其他参照错误码说明',
   msg                  varchar(200) comment '描述信息 例如:Success',
   salesmanSettleFee    float comment '业务员结算积分值',
   platformSettleFee    float comment '平台结算积分值',
   supplierSettleFee    float comment '政策供应商结算积分值',
   supplierAccount      varchar(32) comment '政策供应商结算账号',
   platformAccount      varchar(32) comment '平台结算账号',
   biSettlePoint        float comment '商业险积分率',
   ciSettlePoint        float comment '交强险积分率',
   vvTaxSettlePoint     float comment '车船税积分率',
   createtime           datetime comment '创建时间',
   modifytime           datetime comment '修改时间',
   id                   varchar(32) not null comment '主键id',
   primary key (id)
);

alter table insbyoubaoliancommission comment '优保联佣金信息';

/*==============================================================*/
/* Index: idx_youbaolian_taskid                                 */
/*==============================================================*/
create index idx_youbaolian_taskid on insbyoubaoliancommission
(
   checkedId
);

/*==============================================================*/
/* Table: insbyoubaolianprvmap                                  */
/*==============================================================*/
create table insbyoubaolianprvmap
(
   prvId                varchar(50) comment '保网供应商id',
   supplierId           int comment '政策供应商',
   supplierName         varchar(100) comment '政策供应商名称',
   prvAccountName       varchar(32) comment '政策供应商出单工号',
   createtime           datetime comment '创建时间',
   modifytime           datetime comment '修改时间',
   id                   varchar(32) not null comment '主键id',
   outPurchaseOrgId     varchar(32) comment '外部采购商唯一编号（采购平台分开统计需填写）',
   outPurchaseOrgName   varchar(100) comment '外部采购商名称（采购平台分开统计需填写）',
   primary key (id)
);

alter table insbyoubaolianprvmap comment '优保联政策供应商映射';

/*==============================================================*/
/* Index: idx_youbaolianprvmap_prvid                            */
/*==============================================================*/
create index idx_youbaolianprvmap_prvid on insbyoubaolianprvmap
(
   prvId
);

INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2005130198', '20018', '诚信', '20018', NULL, NULL, '1', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('20071301', '20027', '给力', '20027', NULL, NULL, '2', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2007130183', '20027', '给力', '20027', NULL, NULL, '3', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('20111301', '20027', '给力', '20027', NULL, NULL, '4', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2045130101', '20027', '给力', '20027', NULL, NULL, '5', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2005130183', '20027', '给力', '20027', NULL, NULL, '6', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2005130198', '20043', '圣源祥', '20043', NULL, NULL, '7', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2005130101', '20043', '圣源祥', '20043', NULL, NULL, '8', 'HBFL037311', '河北泛联');
INSERT INTO insbyoubaolianprvmap (`prvId`, `supplierId`, `supplierName`, `prvAccountName`, `createtime`, `modifytime`, `id`, `outPurchaseOrgId`, `outPurchaseOrgName`) VALUES ('2005130103', '20043', '圣源祥', '20043', NULL, NULL, '9', 'HBFL037311', '河北泛联');
