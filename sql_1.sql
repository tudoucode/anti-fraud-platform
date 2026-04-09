/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 80000
 Source Host           : localhost:3306
 Source Schema         : antifraud_db

 Target Server Type    : MySQL
 Target Server Version : 80000
 File Encoding         : 65001

 Date: 03/12/2025 12:00:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for family_binding
-- ----------------------------
DROP TABLE IF EXISTS `family_binding`;
CREATE TABLE `family_binding`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `child_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '子女ID(可能是微信OpenID或注册用户ID)',
  `child_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子女手机号',
  `status` int NULL DEFAULT 1 COMMENT '状态: 0-解绑, 1-绑定',
  `bind_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_elder`(`elder_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '家庭成员绑定关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for fraud_case
-- ----------------------------
DROP TABLE IF EXISTS `fraud_case`;
CREATE TABLE `fraud_case`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '案例标题',
  `category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类: HEALTH(保健), FINANCE(理财), GOV(公检法), FAMILY(亲情)',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简短描述(用于列表展示)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细图文内容',
  `video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频链接',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览量',
  `is_published` tinyint(1) NULL DEFAULT 1 COMMENT '是否发布',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '诈骗案例库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fraud_case
-- ----------------------------
INSERT INTO `fraud_case` VALUES (1, '免费领鸡蛋背后的陷阱', 'HEALTH', '警惕保健品推销', '详细内容...', NULL, 102, 1, '2023-10-01 10:00:00');
INSERT INTO `fraud_case` VALUES (2, '冒充孙子车祸急需钱', 'FAMILY', '遇到急事要先核实', '详细内容...', NULL, 89, 1, '2023-10-02 14:30:00');

-- ----------------------------
-- Table structure for quiz_question
-- ----------------------------
DROP TABLE IF EXISTS `quiz_question`;
CREATE TABLE `quiz_question`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题目内容',
  `option_a` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '是/给' COMMENT '选项A',
  `option_b` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '否/不给' COMMENT '选项B',
  `correct_option` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正确选项: A 或 B',
  `analysis` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '解析(语音朗读文本)',
  `difficulty` int NULL DEFAULT 1 COMMENT '难度: 1-简单, 2-困难',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '反诈测试题库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of quiz_question
-- ----------------------------
INSERT INTO `quiz_question` VALUES (1, '接到自称公安局的电话，说您涉嫌洗黑钱，要求将资金转入安全账户，您转吗？', '转，配合调查', '不转，是诈骗', 'B', '公检法机关没有安全账户！凡是要求转账的都是诈骗。', 1);
INSERT INTO `quiz_question` VALUES (2, '收到短信链接说您中了大奖，需要点进去填写银行卡领奖，您点吗？', '点进去看看', '直接删除', 'B', '不要点击不明链接，中奖通常是陷阱。', 1);

-- ----------------------------
-- Table structure for study_log
-- ----------------------------
DROP TABLE IF EXISTS `study_log`;
CREATE TABLE `study_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `case_id` bigint NOT NULL,
  `duration` int NULL DEFAULT 0 COMMENT '学习时长(秒)',
  `is_completed` tinyint(1) NULL DEFAULT 0 COMMENT '是否完成学习',
  `study_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户学习记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名/昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号(作为登录账号)',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像URL',
  `guardian_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急联系人/子女电话',
  `risk_score` int NULL DEFAULT 0 COMMENT '反诈风险分(0-100)',
  `risk_level` int NULL DEFAULT 0 COMMENT '风险等级: 0-低, 1-中, 2-高',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'USER' COMMENT '角色: USER-老人, ADMIN-管理员',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '李建国', '13800138000', NULL, 68, NULL, '13900139000', 85, 2, 'USER', '2023-10-01 09:00:00', '2023-10-01 09:00:00');
INSERT INTO `sys_user` VALUES (2, '王秀兰', '13800138001', NULL, 72, NULL, '13900139001', 20, 0, 'USER', '2023-10-02 10:00:00', '2023-10-02 10:00:00');

SET FOREIGN_KEY_CHECKS = 1;