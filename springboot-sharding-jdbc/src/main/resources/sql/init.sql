 CREATE TABLE `position`(
 id bigint(36) NOT NULL AUTO_INCREMENT,
 name varchar(256) DEFAULT NULL,
 salary varchar(50) DEFAULT NULL,
 city varchar(256) DEFAULT NULL,
 PRIMARY KEY( Id))
ENGINE=Innodb DEFAULT CHARSET=utf8mb4;

CREATE TABLE `position_detail`(
Id bigint(36) NOT NULL AUTO_INCREMENT,
 pid bigint(36) NOT NULL DEFAULT '0',
 description text DEFAULT NULL,
 PRIMARY KEY (Id)
 )
ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `b_order` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `is_del` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否被删除',
    `company_id` INT(11) NOT NULL COMMENT '公司id',
    `position_id` INT(11) NOT NULL COMMENT '职位id',
    `user_id` INT(11) NOT NULL COMMENT '用户id',
    `publish_user_id` INT(11) NOT NULL COMMENT '职位发布者id',
    `resume_type` INT(2) NOT NULL DEFAULT 0 COMMENT '简历类型:0附件1在线',
    `status` VARCHAR(256) NOT NULL COMMENT '投递状态投递状态WAITー待处理 AUTO FILTERー自动过滤 PREPARE CONTACT-待沟通 REFUSE-拒绝 ARRANGE INTERVIEW-通知面试',
    `create_time` DATETIME(1) NOT NULL COMMENT '创建时间',
    `operate_time` DATETIME COMMENT '操作时间',
    `work_year` VARCHAR(100) DEFAULT NULL COMMENT '工作年限',
    `name` VARCHAR(256) DEFAULT NULL COMMENT '投递简历人名字',
    `position_name` VARCHAR(256) DEFAULT NULL COMMENT '职位名称',
    `resumed_id` INT(10) DEFAULT NULL COMMENT '投递的简历id(在线和附件都记录,通过 resumelype进行区别在线还是附件)',
    PRIMARY KEY (`id`),
    KEY `index_createtime` (`create_time`),
    KEY `index_companyId_status` (`company_id` , `status` (255) , `is_del`),
    KEY `i_comid_pub_ctime` (`company_id` , `publish_user_id` , `is_del`),
    KEY `index_companyId_positionId` (`company_id` , `position_id`) USING BTREE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4
