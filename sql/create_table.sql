# 数据库初始化
-- 创建库
create database if not exists xunjieapi;

-- 切换库
use xunjieapi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    userProfile  varchar(256)                           null comment '用户简介',
    `accessKey` varchar(512) not null comment 'accessKey',
    `secretKey` varchar(512) not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';

-- 接口信息
create table if not exists xunjieapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text not null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';

-- 用户调用接口关系表
create table if not exists xunjieapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('许擎宇', '薛聪健', 'www.cary-king.net', '请求参数1', '潘博涛', '谭聪健', 0, '石炫明', 9500534531);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('陆弘文', '白志强', 'www.leslee-kuhn.net', '请求参数2', '潘懿轩', '马鸿涛', 0, '陈峻熙', 3982575846);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('毛建辉', '罗文', 'www.rosaria-kilback.io', '请求参数3', '冯子默', '彭哲瀚', 0, '赵远航', 121776355);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('彭雨泽', '蔡煜祺', 'www.norris-bergstrom.biz', '请求参数4', '董思源', '田晓博', 0, '潘擎宇', 740);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('傅志强', '陈梓晨', 'www.jordan-reinger.com', '请求参数5', '金志强', '熊锦程', 0, '邓睿渊', 35542559);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('吕黎昕', '孔越彬', 'www.fe-okon.info', '请求参数6', '万伟宸', '林昊然', 0, '孟荣轩', 1445);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('夏雪松', '许子骞', 'www.lashawna-legros.co', '请求参数7', '蔡昊然', '胡鹏涛', 0, '钟立辉', 34075514);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('严钰轩', '阎志泽', 'www.kay-funk.biz', '请求参数8', '莫皓轩', '郭黎昕', 0, '龚天宇', 70956);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('萧嘉懿', '曹熠彤', 'www.margarette-lindgren.biz', '请求参数9', '田泽洋', '邓睿渊', 0, '梁志强', 98);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('杜驰', '冯思源', 'www.vashti-auer.org', '请求参数10', '黎健柏', '武博文', 0, '李伟宸', 9);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('史金鑫', '蔡鹏涛', 'www.diann-keebler.org', '请求参数11', '徐烨霖', '阎建辉', 0, '李烨伟', 125);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('林炫明', '贾旭尧', 'www.dotty-kuvalis.io', '请求参数12', '梁雨泽', '龙伟泽', 0, '许智渊', 79998);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('何钰轩', '赖智宸', 'www.andy-adams.net', '请求参数13', '崔思淼', '白鸿煊', 0, '邵振家', 7167482751);

INSERT INTO xunjieapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) VALUES
    ('魏志强', '于立诚', 'www.ione-aufderhar.biz', '请求参数14', '朱懿轩', '万智渊', 0, '唐昊强', 741098);
