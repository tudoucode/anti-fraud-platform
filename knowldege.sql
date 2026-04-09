-- =======================================================
-- 补充数据脚本 (Add Data Script) - V2.0 增强版
-- 新增：知识库表结构及数据
-- 包含：原有的案例、题目、用户数据
-- =======================================================

SET NAMES utf8mb4;

-- ----------------------------
-- 1. 新增表结构：反诈知识库 (Knowledge Item)
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_item`;
CREATE TABLE `knowledge_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题',
  `type` varchar(20) NOT NULL COMMENT '类型: VIDEO(视频), TIP(锦囊)',
  `content` text COMMENT '内容(视频链接或文字内容)',
  `duration` varchar(20) DEFAULT NULL COMMENT '视频时长',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标样式',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反诈知识库';

-- ----------------------------
-- 2. 插入知识库数据
-- ----------------------------
INSERT INTO `knowledge_item` (`title`, `type`, `content`, `duration`, `icon`) VALUES
('防范杀猪盘骗局', 'VIDEO', 'https://image2url.com/r2/bucket2/videos/1766805820213-2b31c947-bccf-4123-bd76-62038e5f59a5.mp4', '02:39', 'fas fa-play'),
('假冒公检法识别', 'VIDEO', 'https://image2url.com/r2/bucket2/videos/1766805898690-22314dd1-4d11-4db8-810f-98fb730d9da1.mp4', '02:11', 'fas fa-play'),
('保健品推销内幕', 'VIDEO', 'https://image2url.com/r2/bucket2/videos/1766806385093-5b89a9f4-1d40-4071-b982-13456d735241.mp4', '03:07', 'fas fa-play'),
('绝不转账', 'TIP', '任何要求汇款的电话都是诈骗！', NULL, 'fas fa-money-bill-wave'),
('身份保密', 'TIP', '不向陌生人提供银行卡和身份证号。', NULL, 'fas fa-user-secret'),
('官方核实', 'TIP', '收到公检法电话，请去当地派出所核实。', NULL, 'fas fa-building-shield');

-- ----------------------------
-- 3. (保留) 批量插入：诈骗案例
-- ----------------------------
-- 先清空旧数据防止重复（可选）
TRUNCATE TABLE `fraud_case`;
INSERT INTO `fraud_case` (`title`, `category`, `summary`, `content`, `video_url`, `view_count`, `is_published`, `create_time`) VALUES 
('警惕“AI换脸”视频通话诈骗', 'FAMILY', '眼见不一定为实！骗子利用AI技术合成子女面孔。', '详细内容...', 'https://image2url.com/r2/bucket2/videos/1766805471803-c5160b2a-2b13-4d9e-b19f-9bcee8c17c3c.mp4', 1250, 1, NOW()),
('“百万保障”扣费陷阱', 'FINANCE', '谎称您的微信/支付宝“百万保障”服务到期。', '详细内容...', 'https://image2url.com/r2/bucket2/videos/1766806853136-94b7d399-16cf-42ed-98cc-9f82aad1c162.mp4', 980, 1, NOW()),
('特效“降糖神药”不可信', 'HEALTH', '不仅根治糖尿病，还能延年益寿？别信！', '详细内容...', 'https://image2url.com/r2/bucket2/videos/1766807119298-73020f86-de89-4a18-998a-c3dabd157d80.mp4', 856, 1, NOW()),
('冒充公检法：涉嫌“洗黑钱”', 'GOV', '警察不会在网上办案，更没有所谓的“安全账户”！', '详细内容...', 'http://url', 1502, 1, NOW()),
('投资理财：日赚千元不是梦？', 'FINANCE', '高回报往往伴随高风险。', '详细内容...', 'http://url', 670, 1, NOW()),
('免费旅游的背后', 'HEALTH', '几十元就能参加“豪华港澳游”？其实是强制购物。', '详细内容...', 'http://url', 430, 1, NOW());

-- ----------------------------
-- 4. (保留) 批量插入：测试题 (前5题示例，完整版请保留原有的30题)
-- ----------------------------
TRUNCATE TABLE `quiz_question`;
INSERT INTO `quiz_question` (`content`, `option_a`, `option_b`, `correct_option`, `analysis`) VALUES 
('陌生人要求您开启“屏幕共享”来指导您取消业务，您该怎么做？', '照做', '挂断', 'B', '开启屏幕共享相当于把手机交给了别人！'),
('收到短信说ETC过期了，点击链接即可续费。', '点链接', '不点', 'B', '官方机构不会发短信链接要银行卡信息。'),
('网上认识的“外国大兵”说给您寄了包裹，需要交关税。', '帮他交', '拉黑', 'B', '这是典型的杀猪盘诈骗。'),
('有人上门推销代办“养老金提前领取”业务。', '交钱办', '拒绝', 'B', '养老政策有严格规定，花钱办不了。'),
('接到电话自称是“公安局”，要您把钱转到“安全账户”。', '配合转账', '挂断报警', 'B', '公检法绝不会设立安全账户！');

-- ----------------------------
-- 5. 验证
-- ----------------------------
SELECT * FROM knowledge_item;