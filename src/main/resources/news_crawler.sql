/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : news_crawler

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-07-06 21:21:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for catelog
-- ----------------------------
DROP TABLE IF EXISTS `catelog`;
CREATE TABLE `catelog` (
  `cid` int(11) NOT NULL,
  `catelogName` varchar(20) DEFAULT NULL COMMENT '分类名称',
  `parentCatelog` int(11) DEFAULT NULL COMMENT '父分类ID',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `title` varchar(300) DEFAULT NULL COMMENT '新闻标题，最大长度100字',
  `subtitle` varchar(300) DEFAULT NULL COMMENT '新闻子标题，可无，最大长度100字',
  `content` text COMMENT '新闻正文',
  `resource` varchar(20) NOT NULL COMMENT '新闻来源hostname',
  `link` varchar(100) NOT NULL COMMENT '新闻链接，应有唯一性',
  `newsTime` datetime NOT NULL,
  `createTime` datetime NOT NULL,
  `ishidden` int(1) NOT NULL DEFAULT '0' COMMENT '是否显示该条新闻：0显示，1不显示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `link` (`link`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_tag
-- ----------------------------
DROP TABLE IF EXISTS `news_tag`;
CREATE TABLE `news_tag` (
  `newsId` int(11) NOT NULL COMMENT '新闻ID',
  `tagId` int(11) NOT NULL COMMENT 'TagID',
  PRIMARY KEY (`newsId`,`tagId`),
  KEY `tag` (`tagId`),
  CONSTRAINT `news` FOREIGN KEY (`newsId`) REFERENCES `news` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tag` FOREIGN KEY (`tagId`) REFERENCES `tag` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tid` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tagName` varchar(20) NOT NULL COMMENT '标签名称',
  `parentTag` int(11) DEFAULT NULL COMMENT '父标签ID',
  `catelogId` int(11) DEFAULT NULL COMMENT '标签所属的分类ID，链接与表catelog',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
